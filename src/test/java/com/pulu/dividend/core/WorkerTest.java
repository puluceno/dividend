package com.pulu.dividend.core;

import com.pulu.dividend.model.Stock;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class WorkerTest {

    private Worker worker;

    @Before
    public void setUp() {
        worker = new Worker();
    }

    @Test
    public void testComparator() {
        List<Stock> list = new ArrayList<>();
        Stock a = new Stock("a", new BigDecimal(10), new BigDecimal(1), LocalDate.now().plus(6, ChronoUnit.DAYS), LocalDate.now().plus(10, ChronoUnit.DAYS));
        list.add(a);
        Stock b = new Stock("b", new BigDecimal(10), new BigDecimal(1), LocalDate.now().plus(5, ChronoUnit.DAYS), LocalDate.now().plus(10, ChronoUnit.DAYS));
        list.add(b);
        Stock c = new Stock("c", new BigDecimal(30), new BigDecimal(5), LocalDate.now().plus(6, ChronoUnit.DAYS), LocalDate.now().plus(10, ChronoUnit.DAYS));
        list.add(c);
        Stock d = new Stock("d", new BigDecimal(40), new BigDecimal(7), LocalDate.now().plus(4, ChronoUnit.DAYS), LocalDate.now().plus(10, ChronoUnit.DAYS));
        list.add(d);
        Stock e = new Stock("e", new BigDecimal(50), new BigDecimal(9), LocalDate.now().minus(4, ChronoUnit.DAYS), LocalDate.now().plus(10, ChronoUnit.DAYS));
        list.add(e);

        Comparator<Stock> comparator = Comparator.comparing((Stock s) -> s.getExDate().isAfter(LocalDate.now()))
                .thenComparing((s1, s2) -> s2.getExDate().compareTo(s1.getExDate()))
                .thenComparing(Stock::getValue).reversed();

        list.sort(comparator);

        assertEquals(list.get(0), d);
        assertEquals(list.get(1), b);
        assertEquals(list.get(2), c);
        assertEquals(list.get(3), a);
        assertEquals(list.get(4), e);

    }
}
