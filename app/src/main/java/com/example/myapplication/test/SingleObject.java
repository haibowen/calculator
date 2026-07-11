package com.example.myapplication.test;

public class SingleObject {

    private static volatile SingleObject singleObject = null;

    private SingleObject() {

    }

    public static SingleObject getInstance() {
        if (null == singleObject) {
            synchronized (SingleObject.class) {
                if (null == singleObject) {
                    singleObject = new SingleObject();
                }
            }
        }
        return singleObject;
    }
}
