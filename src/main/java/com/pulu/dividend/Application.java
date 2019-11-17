package com.pulu.dividend;

import com.pulu.dividend.core.Worker;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Started crawling operation.");
        long begin = System.currentTimeMillis();

        Worker worker = new Worker();
        worker.process();

        System.out.println("Operation finished in " + (System.currentTimeMillis() - begin) / 1000 + "s");
    }
}
