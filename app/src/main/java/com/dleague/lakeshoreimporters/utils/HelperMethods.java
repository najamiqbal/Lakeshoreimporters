package com.dleague.lakeshoreimporters.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperMethods {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getFormattedNumberPlate(String plate) {
        plate = plate.substring(0, 2) + "-" + plate.substring(2, 5) + "-" + plate.substring(5);
        return plate;
    }

    public static List<String> filterLinks(String response) {
        List<String> containedUrls = new ArrayList<>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(response);

        while (urlMatcher.find()) {
            containedUrls.add(response.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    public static List<String> filterDescription(String response) {
        List<String> containedDescritption = new ArrayList<>();
        String noHTMLString = response.replaceAll("\\<.*?>", "\n");
        String arr[] = noHTMLString.split("\n");
        for (String str : arr) {
            if (!str.equals("")) {
                containedDescritption.add(str);
            }
        }
        return containedDescritption;
    }
}
