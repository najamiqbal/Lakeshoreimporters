package com.dleague.lakeshoreimporters.activities;

import android.app.Application;
import com.dleague.lakeshoreimporters.utils.LocalDbOperations;
import com.dleague.lakeshoreimporters.utils.SharedPref;

import static com.dleague.lakeshoreimporters.utils.Constants.CART_COUNT;

public class AppSpace extends Application {

    public static SharedPref sharedPref;
    public static LocalDbOperations localDbOperations;
    public static int cartItemCount = 0;
    public static boolean isDoubleFinish;
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        isDoubleFinish = false;
        sharedPref = new SharedPref(this);
        localDbOperations = new LocalDbOperations(this);
        cartItemCount = sharedPref.readValue(CART_COUNT, 0);
    }
}
