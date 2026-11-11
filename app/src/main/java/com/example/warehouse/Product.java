package com.example.warehouse;

/** Lớp biểu diễn một sản phẩm trong kho. */
public class Product {
    /** Ngưỡng cảnh báo sắp hết hàng: số lượng nhỏ hơn giá trị này. */
    public static final int LOW_STOCK_THRESHOLD = 5;

    private int id;
    private String name;
    private long price;
    private int quantity;
    private String category;
    private boolean active;
    private String description;

    public Product(int id, String name, long price, int quantity,
                   String category, boolean active, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.active = active;
        this.description = description;
    }

    /** Dùng khi tạo sản phẩm mới (chưa có id). */
    public Product(String name, long price, int quantity,
                   String category, boolean active, String description) {
        this(0, name, price, quantity, category, active, description);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public long getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getCategory() { return category; }
    public boolean isActive() { return active; }
    public String getDescription() { return description; }

    public void setId(int id) { this.id = id; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public boolean isLowStock() {
        return quantity < LOW_STOCK_THRESHOLD;
    }

    /** Giá trị tồn kho = giá bán x số lượng. */
    public long getStockValue() {
        return price * quantity;
    }
}
