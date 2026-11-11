package com.example.warehouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.item_product, parent, false);
        }

        Product p = getItem(position);
        if (p == null) return row;

        TextView tvName = row.findViewById(R.id.tvItemName);
        TextView tvPrice = row.findViewById(R.id.tvItemPrice);
        TextView tvCategory = row.findViewById(R.id.tvItemCategory);
        TextView tvQuantity = row.findViewById(R.id.tvItemQuantity);
        TextView tvLow = row.findViewById(R.id.tvItemLow);

        tvName.setText(p.getName());
        tvPrice.setText(getContext().getString(R.string.item_price, Utils.formatMoney(p.getPrice())));
        tvQuantity.setText("SL: " + p.getQuantity());
        tvCategory.setText(p.getCategory());

        if (p.isLowStock()) {
            tvQuantity.setTextColor(getContext().getColor(R.color.danger));
            tvLow.setVisibility(View.VISIBLE);
        } else {
            tvQuantity.setTextColor(getContext().getColor(R.color.text_primary));
            tvLow.setVisibility(View.GONE);
        }
        return row;
    }
}
