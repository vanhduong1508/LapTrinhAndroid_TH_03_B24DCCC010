package com.example.warehouse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class DatabaseHelperTest {

    private DatabaseHelper db;

    @Before
    public void setUp() {
        db = new DatabaseHelper(ApplicationProvider.getApplicationContext());
    }

    @Test
    public void testCheckLogin() {
        assertTrue(db.checkLogin("admin", "123456"));
        assertFalse(db.checkLogin("admin", "wrongpassword"));
        assertFalse(db.checkLogin("nonexistent", "123456"));
    }

    @Test
    public void testInitialSeedData() {
        List<Product> products = db.getAllProducts();
        assertEquals(3, products.size());

        // Initial total value: 150000*20 + 450000*3 + 25000*40 = 3000000 + 1350000 + 1000000 = 5350000
        assertEquals(5350000L, db.getTotalValue());

        // Low stock count (quantity < 5): "Bàn phím cơ" (quantity 3)
        assertEquals(1, db.countLowStock());
    }

    @Test
    public void testInsertProduct() {
        Product newProduct = new Product("Tai nghe Bluetooth", 250000, 10, "Điện tử", true, "Tai nghe không dây");
        long id = db.insertProduct(newProduct);
        assertTrue(id > 0);

        List<Product> products = db.getAllProducts();
        assertEquals(4, products.size());

        Product fetched = db.getProductById((int) id);
        assertNotNull(fetched);
        assertEquals("Tai nghe Bluetooth", fetched.getName());
        assertEquals(250000L, fetched.getPrice());
        assertEquals(10, fetched.getQuantity());
    }

    @Test
    public void testUpdateQuantity() {
        Product p = db.getProductById(2); // "Bàn phím cơ", qty 3
        assertNotNull(p);

        db.updateQuantity(p.getId(), 4); // Import 1 item (3 -> 4)
        Product updated = db.getProductById(p.getId());
        assertEquals(4, updated.getQuantity());
        assertTrue(updated.isLowStock()); // Still < 5

        db.updateQuantity(p.getId(), 5);
        Product updated2 = db.getProductById(p.getId());
        assertFalse(updated2.isLowStock()); // 5 is not low stock
    }

    @Test
    public void testDeleteProduct() {
        Product p = db.getProductById(1);
        assertNotNull(p);

        int rows = db.deleteProduct(p.getId());
        assertEquals(1, rows);

        assertNull(db.getProductById(p.getId()));
        assertEquals(2, db.getAllProducts().size());
    }
}
