package com.pulu.dividend.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Stock implements Serializable {

    private final String stock;
    private final BigDecimal price;
    private final BigDecimal dividend;
    private final LocalDate exDate;
    private final LocalDate paymentDate;

    public Stock(String stock, BigDecimal price, BigDecimal dividend, LocalDate exDate, LocalDate paymentDate) {
        this.stock = stock;
        this.price = price;
        this.dividend = dividend;
        this.exDate = exDate;
        this.paymentDate = paymentDate;
    }

    public String getStock() {
        return stock;
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
        return getStock() + ", " + getDividend() + ", " + getExDate().format(DateTimeFormatter.ofPattern("MMM/dd/yyyy")) + ", " + getPrice() + ", " + getPaymentDate().format(DateTimeFormatter.ofPattern("MMM/dd/yyyy")) + ", " + getValue();
    }
}
