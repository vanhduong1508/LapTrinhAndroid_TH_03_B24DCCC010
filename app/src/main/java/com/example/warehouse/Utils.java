package com.example.warehouse;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Utils {

    /** Định dạng tiền: 1500000 -> "1.500.000 đ". */
    public static String formatMoney(long value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setGroupingSeparator('.');
        return new DecimalFormat("#,###", symbols).format(value) + " đ";
    }
}
