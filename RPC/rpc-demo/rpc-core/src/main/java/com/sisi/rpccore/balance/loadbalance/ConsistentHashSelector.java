package com.sisi.rpccore.balance.loadbalance;

import com.sisi.rpccore.api.ProviderInfo;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性哈希负载均衡器
 */
public class ConsistentHashSelector {

    private final static SortedMap<Integer, String> virtualInvokers = new TreeMap<>();
    private static List<String> realNodes = new LinkedList<String>();
    private static String[] servers = {"127.0.0.1:9080","127.0.0.1:9081"};

    private static final int VIRTUAL_NODES = 5;

    static {
        for (int i = 0; i<servers.length; i++){
            realNodes.add(servers[i]);
        }

        for (String str : realNodes){
            for (int i = 0; i<VIRTUAL_NODES; i++){
                String virtualNodeName = str + "&&VN" + String.valueOf(i);
                int hash = getHash(virtualNodeName);
                virtualInvokers.put(hash, virtualNodeName);
            }
        }
    }
    ConsistentHashSelector(List<ProviderInfo> providers){
        for (ProviderInfo provider : providers){
            String address = provider.getUrl();

        }

    }

    private static int getHash(String str){
        final int p = 16777619;
        int hash = (int)2166136261L;
        for (int i = 0; i < str.length();i++){
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        if (hash < 0){
            hash = Math.abs(hash);
        }
        return hash;
    }
}
