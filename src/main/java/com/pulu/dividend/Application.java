package com.pulu.dividend;

import com.pulu.dividend.core.Worker;

import java.io.IOException;

public class Application {

    public static void main(String[] args) {
        System.out.println("Started crawling operation.");
        long begin = System.currentTimeMillis();

        Worker worker = new Worker();
        worker.processReits();
        worker.processSP500();

        System.out.println("Operation finished in " + (System.currentTimeMillis() - begin) / 1000 + "s");
    }
}
