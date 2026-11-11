package com.example.warehouse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "warehouse.db";
    private static final int DB_VERSION = 2;

    // Bảng users
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_PASSWORD = "password";

    // Bảng products
    private static final String TABLE_PRODUCTS = "products";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_PRICE = "price";
    private static final String COL_QUANTITY = "quantity";
    private static final String COL_CATEGORY = "category";
    private static final String COL_ACTIVE = "active";
    private static final String COL_DESCRIPTION = "description";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " ("
                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USERNAME + " TEXT NOT NULL UNIQUE, "
                + COL_PASSWORD + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_PRICE + " INTEGER NOT NULL, "
                + COL_QUANTITY + " INTEGER NOT NULL, "
                + COL_CATEGORY + " TEXT NOT NULL, "
                + COL_ACTIVE + " INTEGER NOT NULL, "
                + COL_DESCRIPTION + " TEXT)");

        // Dữ liệu mẫu
        insertUser(db, "admin", "123456");
        insertProduct(db, new Product("Chuột không dây", 150000, 20, "Điện tử", true, ""));
        insertProduct(db, new Product("Bàn phím cơ", 450000, 3, "Điện tử", true, "Bàn phím cơ switch xanh, có đèn nền"));
        insertProduct(db, new Product("Sổ tay A5", 25000, 40, "Văn phòng phẩm", true, ""));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Users
    private long insertUser(SQLiteDatabase db, String username, String password) {
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        return db.insert(TABLE_USERS, null, values);
    }

    public long insertUser(String username, String password) {
        return insertUser(getWritableDatabase(), username, password);
    }

    /** Trả về true nếu có dòng khớp trong bảng users. */
    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_ID},
                COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?",
                new String[]{username, password}, null, null, null);
        try {
            return cursor.getCount() > 0;
        } finally {
            cursor.close();
        }
    }

    // Products
    private ContentValues toValues(Product p) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, p.getName());
        values.put(COL_PRICE, p.getPrice());
        values.put(COL_QUANTITY, p.getQuantity());
        values.put(COL_CATEGORY, p.getCategory());
        values.put(COL_ACTIVE, p.isActive() ? 1 : 0);
        values.put(COL_DESCRIPTION, p.getDescription());
        return values;
    }

    private long insertProduct(SQLiteDatabase db, Product p) {
        return db.insert(TABLE_PRODUCTS, null, toValues(p));
    }

    /** Thêm sản phẩm, trả về id vừa thêm (-1 nếu lỗi). */
    public long insertProduct(Product p) {
        return insertProduct(getWritableDatabase(), p);
    }

    private Product cursorToProduct(Cursor c) {
        return new Product(
                c.getInt(c.getColumnIndexOrThrow(COL_ID)),
                c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                c.getLong(c.getColumnIndexOrThrow(COL_PRICE)),
                c.getInt(c.getColumnIndexOrThrow(COL_QUANTITY)),
                c.getString(c.getColumnIndexOrThrow(COL_CATEGORY)),
                c.getInt(c.getColumnIndexOrThrow(COL_ACTIVE)) == 1,
                c.getString(c.getColumnIndexOrThrow(COL_DESCRIPTION)));
    }

    /** Lấy toàn bộ sản phẩm để hiển thị danh sách. */
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, null, null, null, null, null, COL_ID + " ASC");
        try {
            while (cursor.moveToNext()) {
                list.add(cursorToProduct(cursor));
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    /** Lấy một sản phẩm theo id, trả về null nếu không tồn tại. */
    public Product getProductById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS, null, COL_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                return cursorToProduct(cursor);
            }
            return null;
        } finally {
            cursor.close();
        }
    }

    /** Cập nhật số lượng (dùng cho Nhập thêm / Bán). */
    public int updateQuantity(int id, int quantity) {
        ContentValues values = new ContentValues();
        values.put(COL_QUANTITY, quantity);
        return getWritableDatabase().update(TABLE_PRODUCTS, values,
                COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    /** Xóa sản phẩm theo id. */
    public int deleteProduct(int id) {
        return getWritableDatabase().delete(TABLE_PRODUCTS,
                COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    /** Tổng giá trị kho: SUM(price * quantity). */
    public long getTotalValue() {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT COALESCE(SUM(" + COL_PRICE + " * " + COL_QUANTITY + "), 0) FROM "
                        + TABLE_PRODUCTS, null);
        try {
            return cursor.moveToFirst() ? cursor.getLong(0) : 0;
        } finally {
            cursor.close();
        }
    }

    /** Số sản phẩm sắp hết hàng: quantity < 5. */
    public int countLowStock() {
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_PRODUCTS + " WHERE " + COL_QUANTITY + " < ?",
                new String[]{String.valueOf(Product.LOW_STOCK_THRESHOLD)});
        try {
            return cursor.moveToFirst() ? cursor.getInt(0) : 0;
        } finally {
            cursor.close();
        }
    }
}
