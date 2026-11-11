package com.example.warehouse;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class AddProductActivity extends AppCompatActivity {

    private EditText etName, etPrice, etQuantity, etDescription;
    private Spinner spCategory;
    private SwitchCompat swActive;
    private Button btnSave;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        setTitle(R.string.title_add);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        etQuantity = findViewById(R.id.etQuantity);
        etDescription = findViewById(R.id.etDescription);
        spCategory = findViewById(R.id.spCategory);
        swActive = findViewById(R.id.swActive);
        btnSave = findViewById(R.id.btnSave);

        swActive.setChecked(true);
        btnSave.setOnClickListener(v -> saveProduct());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveProduct() {
        String name = etName.getText().toString().trim();
        String priceText = etPrice.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        boolean valid = true;

        if (name.isEmpty()) {
            etName.setError(getString(R.string.err_name));
            valid = false;
        }

        long price;
        try {
            price = Long.parseLong(priceText);
        } catch (NumberFormatException e) {
            price = 0;
        }
        if (price <= 0) {
            etPrice.setError(getString(R.string.err_price));
            valid = false;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            quantity = -1;
        }
        if (quantity < 0) {
            etQuantity.setError(getString(R.string.err_quantity));
            valid = false;
        }

        if (!valid) return;

        String category = spCategory.getSelectedItem().toString();
        Product product = new Product(name, price, quantity, category,
                swActive.isChecked(), description);

        if (db.insertProduct(product) != -1) {
            Toast.makeText(this, R.string.msg_saved, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
