package com.example.myapplication.test;

import androidx.annotation.NonNull;

public class NewTest {


    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
