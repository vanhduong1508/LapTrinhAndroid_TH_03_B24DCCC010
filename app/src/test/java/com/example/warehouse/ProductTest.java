package com.example.warehouse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ProductTest {

    @Test
    public void testStockValueCalculation() {
        Product p = new Product("Bàn phím cơ", 450000, 3, "Điện tử", true, "Mô tả");
        assertEquals(1350000L, p.getStockValue());
    }

    @Test
    public void testLowStockThreshold() {
        Product lowStockProduct = new Product("Bàn phím cơ", 450000, 3, "Điện tử", true, "");
        assertTrue(lowStockProduct.isLowStock());

        Product normalStockProduct = new Product("Chuột không dây", 150000, 20, "Điện tử", true, "");
        assertFalse(normalStockProduct.isLowStock());

        Product boundaryProduct = new Product("Sản phẩm x", 10000, 5, "Khác", true, "");
        assertFalse(boundaryProduct.isLowStock());
    }
}
