package com.yuhtin.commission.afelia.commandshop.utils;

import com.yuhtin.commission.afelia.commandshop.AfeliaCommandShop;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumberUtils {

    private static final Pattern PATTERN = Pattern.compile("^(\\d+\\.?\\d*)(\\D+)");

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.#");
    private static final DecimalFormat NUMBER_FORMAT = new DecimalFormat("#.##");

    public static String format(double value) {
        if (isInvalid(value)) return "0";

        val formatType = getFormatType();
        if (formatType.equalsIgnoreCase("DECIMAL")) {
            return DECIMAL_FORMAT.format(value);
        }

        int index = 0;
        val format = getCurrencyFormat();

        double tmp;
        while ((tmp = value / 1000) >= 1) {
            if (index + 1 == format.size()) break;
            value = tmp;
            ++index;
        }

        return NUMBER_FORMAT.format(value) + format.get(index);
    }

    private static List<String> getCurrencyFormat() {
        return AfeliaCommandShop.getInstance().getConfig().getStringList("currency-format");
    }

    private static String getFormatType() {
        return AfeliaCommandShop.getInstance().getConfig().getString("format-type");
    }

    public static double parse(String string) {
        try {

            val value = Double.parseDouble(string);
            return isInvalid(value) ? 0 : value;

        } catch (Exception ignored) {
        }

        if (getFormatType().equalsIgnoreCase("DECIMAL")) return 0;

        Matcher matcher = PATTERN.matcher(string);
        if (!matcher.find()) return -1;

        double amount = Double.parseDouble(matcher.group(1));
        String suffix = matcher.group(2);
        String fixedSuffix = suffix.equalsIgnoreCase("k") ? suffix.toLowerCase() : suffix.toUpperCase();

        int index = getCurrencyFormat().indexOf(fixedSuffix);

        val value = amount * Math.pow(1000, index);
        return isInvalid(value) ? 0 : value;
    }

    public static boolean isInvalid(double value) {
        return value < 0 || Double.isNaN(value) || Double.isInfinite(value);
    }

}
