package com.pulu.dividend.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Stock implements Serializable {
    private static final long serialVersionUID = 7868491819504739803L;

    private final String paper;
    private final BigDecimal price;
    private final BigDecimal dividend;
    private final LocalDate exDate;
    private final LocalDate paymentDate;

    public Stock(String paper, BigDecimal price, BigDecimal dividend, LocalDate exDate, LocalDate paymentDate) {
        this.paper = paper;
        this.price = price;
        this.dividend = dividend;
        this.exDate = exDate;
        this.paymentDate = paymentDate;
    }

    public String getPaper() {
        return paper;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getDividend() {
        return dividend;
    }

    public LocalDate getExDate() {
        return exDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public BigDecimal getValue() {
        return getDividend().divide(getPrice(), 5, RoundingMode.DOWN);
    }

    @Override
    public String toString() {
        return getPaper() + ", " + getDividend() + ", " + getExDate().format(DateTimeFormatter.ofPattern("MMM/dd/yyyy")) + ", " + getPrice() + ", " + getPaymentDate().format(DateTimeFormatter.ofPattern("MMM/dd/yyyy")) + ", " + getValue();
    }
}
