package com.pulu.dividend.core;

import java.time.LocalDate;

public class Constants {

    private Constants() {
    }

    public static final String REITS_FILENAME = "reits.csv";
    public static final String SP500_FILENAME = "sp500.csv";
    public static final String DIVIDENDS_REITS_FILENAME = "dividends_REITS_" + LocalDate.now() + ".csv";
    public static final String DIVIDENDS_SP500_FILENAME = "dividends_SP500_" + LocalDate.now() + ".csv";
}
