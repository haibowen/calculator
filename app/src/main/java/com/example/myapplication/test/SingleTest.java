package com.example.myapplication.test;

public class SingleTest {

    private volatile static SingleTest singleTest;

    private SingleTest() {

    }

    public static SingleTest getInstance() {
        if (null == singleTest) {
            synchronized (SingleTest.class) {
                if (null == singleTest) {
                    singleTest = new SingleTest();
                }
            }
        }
        return singleTest;
    }
}

