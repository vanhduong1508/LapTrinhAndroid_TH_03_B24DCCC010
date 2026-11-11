package com.example.warehouse;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private int productId;
    private Product product;

    private TextView tvName, tvCategory, tvPrice, tvQuantity,
            tvStockValue, tvWarning, tvDescription;
    private Button btnImport, btnSell, btnBack, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        setTitle(R.string.title_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = new DatabaseHelper(this);
        productId = getIntent().getIntExtra(MainActivity.EXTRA_PRODUCT_ID, -1);

        tvName = findViewById(R.id.tvName);
        tvCategory = findViewById(R.id.tvCategory);
        tvPrice = findViewById(R.id.tvPrice);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvStockValue = findViewById(R.id.tvStockValue);
        tvWarning = findViewById(R.id.tvWarning);
        tvDescription = findViewById(R.id.tvDescription);
        btnImport = findViewById(R.id.btnImport);
        btnSell = findViewById(R.id.btnSell);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);

        btnImport.setOnClickListener(v -> importOne());
        btnSell.setOnClickListener(v -> sellOne());
        btnBack.setOnClickListener(v -> finish());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProduct();
    }

    private void loadProduct() {
        product = db.getProductById(productId);
        if (product == null) {
            Toast.makeText(this, R.string.msg_not_found, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvName.setText(product.getName());
        tvCategory.setText(product.getCategory());
        tvPrice.setText(Utils.formatMoney(product.getPrice()));
        tvQuantity.setText(String.valueOf(product.getQuantity()));
        tvStockValue.setText(Utils.formatMoney(product.getStockValue()));

        tvWarning.setVisibility(product.isLowStock() ? View.VISIBLE : View.GONE);

        String desc = product.getDescription();
        if (desc == null || desc.trim().isEmpty()) {
            tvDescription.setText(R.string.detail_no_description);
        } else {
            tvDescription.setText(desc);
        }
    }

    private void importOne() {
        db.updateQuantity(productId, product.getQuantity() + 1);
        loadProduct();
    }

    private void sellOne() {
        if (product.getQuantity() <= 0) {
            Toast.makeText(this, R.string.msg_out_of_stock, Toast.LENGTH_SHORT).show();
            return;
        }
        db.updateQuantity(productId, product.getQuantity() - 1);
        loadProduct();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_msg)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    db.deleteProduct(productId);
                    Toast.makeText(this, R.string.msg_deleted, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
