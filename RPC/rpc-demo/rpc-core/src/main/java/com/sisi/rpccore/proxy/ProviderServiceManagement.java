package com.sisi.rpccore.proxy;

import com.google.common.base.Joiner;
import com.sisi.rpccore.anotation.ProviderService;
import com.sisi.rpccore.api.RpcRequest;
import com.sisi.rpccore.discovery.DiscoveryServer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * 服务提供者管理，扫描包获取实现类并注册到zk中
 */
@Slf4j
public class ProviderServiceManagement {
    /**
     * 通过服务名、分组、版本作为key，确定接口实现类的实例
     * service:group:version --> class
     */
    private static Map<String, Object> proxyMap = new HashMap<>();

    /**
     * 初始化：通过扫描包的路径，获取所有实现类，将其注册到ZK中
     * @param packageName
     * @param port
     */
    public static void init(String packageName, int port) throws Exception {
        System.out.println("\n-------- loader Rpc Provider class start -------\n");

        DiscoveryServer serviceRegister = new DiscoveryServer();
        Class[] classes = getClasses(packageName);
        for (Class c:classes){
            ProviderService annotation = (ProviderService)c.getAnnotation(ProviderService.class);
            if (annotation == null){
                continue;
            }
            String group = annotation.group();
            String version = annotation.version();
            List<String> tags = Arrays.asList(annotation.tags().split("."));
            String provider = Joiner.on(":").join(annotation.service(),group,version);
            int weight = annotation.weight();
            proxyMap.put(provider, c.newInstance());

            serviceRegister.registerService(annotation.service(), group, version, port, tags, weight);

            log.info("load provider class:" + annotation.service() + ":" + group + ":" + version + " :: " + c.getName());
        }
        System.out.println("\n-------- Loader Rpc Provider class end ----------------------\n");

    }

    /**
     * 返回接口实现类的实例
     * @param request
     * @return
     */
    public static Object getProviderService(RpcRequest request){
        String group = "default";
        String version = "default";
        String className = request.getServiceClass();
        if (request.getGroup() != null){
            group = request.getGroup();
        }
        if (request.getVersion() != null){
            version = request.getVersion();
        }
        return proxyMap.get(Joiner.on(":").join(className,group,version));
    }

    /**
     * 获取实现类
     * @param packageName
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace(".","/");
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()){
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs){
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[0]);
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()){
            return classes;
        }
        File[] files = directory.listFiles();
        assert files != null;
        for (File file: files){
            if (file.isDirectory()){
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "."+ file.getName()));
            } else if(file.getName().endsWith(".class")){
                classes.add(Class.forName(packageName + "." + file.getName().substring(0, file.getName().length()-6)));
            }
        }
        return classes;
    }
}
