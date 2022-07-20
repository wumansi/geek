package com.demo.cache.utils;

public enum  EventType {
    CALCULATE(0),
    COPYFILE(1),
    MAIL(2);

    private int value;
    public int getValue(){
        return value;
    }
    EventType(int value){
        this.value = value;
    }
}
