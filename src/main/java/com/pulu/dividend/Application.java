package com.pulu.dividend;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.pulu.dividend.core.Worker;

public class Application {

    private static final Logger LOGGER = Logger.getLogger(Application.class.getSimpleName());

    public static void main(String[] args) {
        LOGGER.log(Level.INFO, "Started crawling operation.");
        long begin = System.currentTimeMillis();

        Worker worker = new Worker();
        worker.processReits();
        worker.processSP500();

        LOGGER.log(Level.INFO, "Operation finished in {0}s", (System.currentTimeMillis() - begin) / 1000);
    }
}
