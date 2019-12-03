package com.dleague.lakeshoreimporters.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.dtos.ProductDTO;
import com.dleague.lakeshoreimporters.fragments.AboutUsFragment;
import com.dleague.lakeshoreimporters.fragments.BeautyFragment;
import com.dleague.lakeshoreimporters.fragments.BinRequestsFragment;
import com.dleague.lakeshoreimporters.fragments.BuyNowFragment;
import com.dleague.lakeshoreimporters.fragments.CaseBuysFragment;
import com.dleague.lakeshoreimporters.fragments.CategoriesFragment;
import com.dleague.lakeshoreimporters.fragments.ClothingFragment;
import com.dleague.lakeshoreimporters.fragments.ElectronicsFragment;
import com.dleague.lakeshoreimporters.fragments.HomeAndKitchenFragment;
import com.dleague.lakeshoreimporters.fragments.JackBuyFragment;
import com.dleague.lakeshoreimporters.fragments.BuyStatusFragment;
import com.dleague.lakeshoreimporters.fragments.ExtrasFragment;
import com.dleague.lakeshoreimporters.fragments.HomeFragment;
import com.dleague.lakeshoreimporters.fragments.HowToFragment;
import com.dleague.lakeshoreimporters.fragments.JewelryFragment;
import com.dleague.lakeshoreimporters.fragments.NotificationFragment;
import com.dleague.lakeshoreimporters.fragments.OutDoorFragment;
import com.dleague.lakeshoreimporters.fragments.PoliciesFragment;
import com.dleague.lakeshoreimporters.fragments.ProductDetailFragment;
import com.dleague.lakeshoreimporters.fragments.ProfileFragment;
import com.dleague.lakeshoreimporters.fragments.SeasonalFragment;
import com.dleague.lakeshoreimporters.fragments.ToysAndGamesFragment;
import com.dleague.lakeshoreimporters.fragments.WeeklyDeals;
import com.dleague.lakeshoreimporters.httpcalls.HttpNetworkCall;
import com.dleague.lakeshoreimporters.httpcalls.HttpResponseCallbacks;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dleague.lakeshoreimporters.activities.AppSpace.cartItemCount;
import static com.dleague.lakeshoreimporters.utils.Constants.CART_COUNT;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_EMAIL;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_ID;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_NAME;
import static com.dleague.lakeshoreimporters.utils.Constants.SESSION;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HttpResponseCallbacks,View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_cart)
    ImageView ivCart;
    @BindView(R.id.iv_cart_items)
    TextView tvCartItems;
    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation)
    NavigationView navigationView;

    private ProductDTO productToView;
    private FragmentManager fragmentManager;
    private List<String> tagsList;
    private Fragment fragment;
    private String Fragment_TAG;
    Button btn_me,btn_home,btn_alert,btn_category;
    private HttpNetworkCall httpNetworkCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initObj();
        if (AppSpace.sharedPref.readValue(CUSTOMER_ID, "0").equals("0") ||
                AppSpace.sharedPref.readValue(CUSTOMER_NAME, "0").equals("0") ||
                AppSpace.sharedPref.readValue(CUSTOMER_EMAIL, "0").equals("0")) {
            new CustomerIdTask().execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCart();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
            tagsList.remove(tagsList.size() - 1);
            toolbarTitle.setText(tagsList.get(tagsList.size() - 1));
        } else {
            moveTaskToBack(false);
        }
    }

    @OnClick(R.id.iv_cart)
    void setIvCart() {
        startActivity(new Intent(this, CartActivity.class));
    }

    private void initView() {
        btn_home=findViewById(R.id.btn_home);
        btn_category=findViewById(R.id.btn_category);
        btn_alert=findViewById(R.id.btn_alert);
        btn_me=findViewById(R.id.btn_me);
        btn_alert.setOnClickListener(this);
        btn_category.setOnClickListener(this);
        btn_home.setOnClickListener(this);
        btn_me.setOnClickListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {

            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initObj() {
        tagsList = new ArrayList<>();
        httpNetworkCall = new HttpNetworkCall(this, this);
        fragmentManager = getSupportFragmentManager();
        selectItem(0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_home) {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            selectItem(0);
        } else if (id == R.id.nav_buy_now) {
            selectItem(1);
        }
     /*   else if (id == R.id.nav_jake_spice) {
            selectItem(100);
        }*/
        else if (id == R.id.nav_case_extravaganza) {
            selectItem(101);
        }
/*        else if (id == R.id.nav_buy_status) {
            selectItem(2);
        }*/
        else if (id == R.id.nav_extras) {
            selectItem(3);
        } else if (id == R.id.nav_bin_requests) {
            selectItem(4);
        } else if (id == R.id.nav_about_us) {
            selectItem(5);
        } else if (id == R.id.nav_warranty_claim) {
            startActivity(new Intent(MainActivity.this, WarrantyClaimActivity.class));
        } else if (id == R.id.nav_profile) {
            selectItem(8);
        } else if (id == R.id.nav_policies) {
            selectItem(10);
        } else if (id == R.id.nav_logout) {
            logoutUser();
        } else {
            selectItem(0);
        }
        drawerLayout.closeDrawers();
        return true;
    }

    private void logoutUser() {
        AppSpace.sharedPref.writeValue(SESSION, "0");
        AppSpace.sharedPref.writeValue(CUSTOMER_ID, "0");
        AppSpace.sharedPref.writeValue(CUSTOMER_EMAIL, "0");
        AppSpace.sharedPref.writeValue(CUSTOMER_NAME, "0");
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    private void selectItem(int i) {
        Class fragmentClass = null;
        switch (i) {
            case 0:
                Fragment_TAG = getString(R.string.home);
                fragmentClass = HomeFragment.class;
                break;
            case 1:
                Fragment_TAG = getString(R.string.faceboo_buys);
                fragmentClass = BuyNowFragment.class;
                break;
            case 100:
                Fragment_TAG = getString(R.string.jake_spice);
                fragmentClass = JackBuyFragment.class;
                break;
            case 101:
                Fragment_TAG = getString(R.string.case_extravaganza);
                fragmentClass = CaseBuysFragment.class;
                break;
            case 2:
                Fragment_TAG = getString(R.string.past_buys);
                fragmentClass = BuyStatusFragment.class;
                break;
            case 3:
                Fragment_TAG = getString(R.string.extra_extra);
                fragmentClass = ExtrasFragment.class;
                break;
            case 4:
                Fragment_TAG = getString(R.string.bin_requests);
                fragmentClass = BinRequestsFragment.class;
                break;
            case 5:
                Fragment_TAG = getString(R.string.about_us);
                fragmentClass = AboutUsFragment.class;
                break;
            case 6:
                Fragment_TAG = getString(R.string.how_to);
                fragmentClass = HowToFragment.class;
                break;
            case 8:
                Fragment_TAG = getString(R.string.profile);
                fragmentClass = ProfileFragment.class;
                break;
            case 9:
                Fragment_TAG = getString(R.string.product_detail);
                fragmentClass = ProductDetailFragment.class;
                break;
            case 10:
                Fragment_TAG = getString(R.string.policies);
                fragmentClass = PoliciesFragment.class;
                break;
            case 20:
                Fragment_TAG = getString(R.string.seasonal);
                fragmentClass = SeasonalFragment.class;
                break;
            case 21:
                Fragment_TAG = getString(R.string.electronics);
                fragmentClass = ElectronicsFragment.class;
                break;
            case 22:
                Fragment_TAG = getString(R.string.outdoor);
                fragmentClass = OutDoorFragment.class;
                break;
            case 23:
                Fragment_TAG = getString(R.string.jewelry);
                fragmentClass = JewelryFragment.class;
                break;
            case 24:
                Fragment_TAG = getString(R.string.homeKitchen);
                fragmentClass = HomeAndKitchenFragment.class;
                break;
            case 25:
                Fragment_TAG = getString(R.string.clothing);
                fragmentClass = ClothingFragment.class;
                break;
            case 26:
                Fragment_TAG = getString(R.string.beauty);
                fragmentClass = BeautyFragment.class;
                break;
            case 27:
                Fragment_TAG = getString(R.string.toys);
                fragmentClass = ToysAndGamesFragment.class;
                break;
            case 28:
                Fragment_TAG = getString(R.string.weekly);
                fragmentClass = WeeklyDeals.class;
                break;
            case 29:
                Fragment_TAG = getString(R.string.category);
                fragmentClass = CategoriesFragment.class;
                break;
            case 30:
                Fragment_TAG = getString(R.string.alert);
                fragmentClass = NotificationFragment.class;
                break;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (i == 9) {
            Bundle arguments = new Bundle();
            arguments.putSerializable("productToView", productToView);
            fragment.setArguments(arguments);
        }
        toolbarTitle.setText(Fragment_TAG);
        tagsList.add(Fragment_TAG);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, Fragment_TAG)
                .addToBackStack(null)
                .commit();
    }

    public void launchFragment(int number) {
        selectItem(number);
    }

    public void launchProductViewFragment(ProductDTO productToView) {
        this.productToView = productToView;
        selectItem(9);
    }

    public void updateCart() {
        AppSpace.sharedPref.writeValue(CART_COUNT, cartItemCount);
        if (cartItemCount > 0) {
            tvCartItems.setText(String.valueOf(cartItemCount));
            tvCartItems.setVisibility(View.VISIBLE);
        } else {
            tvCartItems.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccess(int responseCode, Object object) {

    }

    @Override
    public void onFailure(int responseCode, String message) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_home:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                selectItem(0);
                break;
            case R.id.btn_category:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                selectItem(29);
                break;
            case R.id.btn_alert:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                selectItem(30);
                break;
            case R.id.btn_me:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                selectItem(8);
                break;
        }
    }

    public class CustomerIdTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            httpNetworkCall.getCustomerId(AppSpace.sharedPref.readValue(CUSTOMER_EMAIL, "0"));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}
