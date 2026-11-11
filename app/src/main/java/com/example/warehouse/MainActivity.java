package com.example.warehouse;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "product_id";

    private DatabaseHelper db;
    private TextView tvTotalProducts, tvTotalValue, tvLowStock, tvEmpty;
    private ListView listView;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.title_main);

        db = new DatabaseHelper(this);
        tvTotalProducts = findViewById(R.id.tvTotalProducts);
        tvTotalValue = findViewById(R.id.tvTotalValue);
        tvLowStock = findViewById(R.id.tvLowStock);
        tvEmpty = findViewById(R.id.tvEmpty);
        listView = findViewById(R.id.listProducts);
        btnAdd = findViewById(R.id.btnAddProduct);

        listView.setEmptyView(tvEmpty);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Product p = (Product) parent.getItemAtPosition(position);
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra(EXTRA_PRODUCT_ID, p.getId());
            startActivity(intent);
        });

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddProductActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        List<Product> products = db.getAllProducts();
        listView.setAdapter(new ProductAdapter(this, products));

        tvTotalProducts.setText(String.valueOf(products.size()));
        tvTotalValue.setText(Utils.formatMoney(db.getTotalValue()));
        tvLowStock.setText(String.valueOf(db.countLowStock()));
    }
}
