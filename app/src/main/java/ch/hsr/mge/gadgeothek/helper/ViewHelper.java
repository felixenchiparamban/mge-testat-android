package ch.hsr.mge.gadgeothek.helper;

import android.graphics.Color;
import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import ch.hsr.mge.gadgeothek.domain.Gadget;

public class ViewHelper {
    public static int getRandomColor() {
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);

        return Color.rgb(r, g, b);
    }

    public static String formatShortDate(Date date) {
        return DateFormat.format("dd MMM yyyy HH:MM", date).toString();
    }

    public static long getRemainingDays(Date date) {
        long msDiff = date.getTime() - new Date().getTime();
        return TimeUnit.MILLISECONDS.toDays(msDiff);
    }

    public static ViewTimeUnit getAppropriateTimeUnit(long days) {
        long remaining = Math.abs(days);
        if (remaining < 100) {
            return ViewTimeUnit.DAYS;
        }
        remaining = remaining / 30;
        if (remaining < 12) {
            return ViewTimeUnit.MONTHS;
        }
        return ViewTimeUnit.YEARS;
    }

    public static long getApproximate(long days, ViewTimeUnit unit) {
        switch (unit) {
            case DAYS:
                return days;
            case MONTHS:
                return days / 30;
            case YEARS:
                return days / 365;
            default:
                throw new IllegalArgumentException(String.format("%s is not valid", unit.toString()));
        }
    }

    public static String[] getGadgetsForDialog(List<Gadget> gadgets) {
        List<String> gadgetsList = new ArrayList<>();
        for (Gadget g : gadgets) {
            gadgetsList.add(g.getName() + " - " + g.getCondition());
        }
        return gadgetsList.toArray(new String[gadgets.size()]);
    }

    public static String formatPrice(double price){
        return String.format("Fr. %.2f", price);
    }
}
