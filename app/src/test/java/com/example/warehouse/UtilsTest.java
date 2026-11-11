package com.example.warehouse;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilsTest {

    @Test
    public void testFormatMoneyZero() {
        assertEquals("0 đ", Utils.formatMoney(0));
    }

    @Test
    public void testFormatMoneyStandard() {
        assertEquals("150.000 đ", Utils.formatMoney(150000));
    }

    @Test
    public void testFormatMoneyLarge() {
        assertEquals("5.350.000 đ", Utils.formatMoney(5350000));
    }
}
