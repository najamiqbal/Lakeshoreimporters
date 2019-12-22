package com.dleague.lakeshoreimporters.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.cedarsoftware.util.io.JsonWriter;
import com.dleague.lakeshoreimporters.ApplyDiscountMutation;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.dtos.AddressDTO;
import com.dleague.lakeshoreimporters.dtos.UserDTO;
import com.dleague.lakeshoreimporters.dtos.responsedto.CreateCheckoutResponseDTO;
import com.dleague.lakeshoreimporters.dtos.responsedto.ItemsItem;
import com.dleague.lakeshoreimporters.dtos.responsedto.apply_spent_rule_response.ApplySpentRulesDTO;
import com.dleague.lakeshoreimporters.dtos.responsedto.spent_rule_response.GetSpentRuleDTO;
import com.dleague.lakeshoreimporters.dtos.responsedto.spent_rule_response.RulesItem;
import com.dleague.lakeshoreimporters.httpcalls.HttpNetworkCall;
import com.dleague.lakeshoreimporters.httpcalls.HttpResponseCallbacks;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCallbacks;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCalls;
import com.dleague.lakeshoreimporters.utils.DialogBuilder;
import com.dleague.lakeshoreimporters.utils.MessageUtil;
import com.dleague.lakeshoreimporters.utils.Validations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.dleague.lakeshoreimporters.activities.AppSpace.isDoubleFinish;
import static com.dleague.lakeshoreimporters.utils.Constants.APPLY_DISCOUNT;
import static com.dleague.lakeshoreimporters.utils.Constants.APPLY_SPENT_RULE;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_EMAIL;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_ID;
import static com.dleague.lakeshoreimporters.utils.Constants.DEFAULT_ADDRESS_ID;
import static com.dleague.lakeshoreimporters.utils.Constants.GET_SPENT_RULE;
import static com.dleague.lakeshoreimporters.utils.Constants.LAST_CHECKOUT_ID;
import static com.dleague.lakeshoreimporters.utils.Constants.LAST_CHECKOUT_WEB_URL;
import static com.dleague.lakeshoreimporters.utils.Constants.LOG_TAG;

public class ShippingAddressActivity extends AppCompatActivity implements NetworkCallbacks, HttpResponseCallbacks {

    @BindView(R.id.toolbar_simple)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_simple)
    TextView toolbarTitle;
    @BindView(R.id.et_sad_discount_code)
    EditText etDiscountCode;
    @BindView(R.id.btn_sad_apply_discount)
    Button btnApplyDiscount;
    @BindView(R.id.btn_sad_next)
    Button btnNext;
    @BindView(R.id.tv_sad_subtotal)
    TextView tvSubTotal;
    @BindView(R.id.tv_sad_total)
    TextView tvTotal;
    @BindView(R.id.tv_sad_contact_info)
    TextView tvContactInfo;
    @BindView(R.id.tv_sad_ship_address_info)
    TextView tvShippingInfo;
    @BindView(R.id.et_sad_card_number)
    EditText etCardNumber;
    @BindView(R.id.et_sad_card_name)
    EditText etCardName;
    @BindView(R.id.tv_sad_security_code)
    EditText etCardSecurityCode;
    @BindView(R.id.tv_sad_expiration_date)
    TextView tvExpirationDate;
    @BindView(R.id.viewflipper_sad)
    ViewFlipper viewFlipper;
    @BindView(R.id.spinner_sd_store_credit)
    AppCompatSpinner spentRuleSpinner;
    @BindView(R.id.layout_spent_rule)
    LinearLayout layoutSpentRule;
    private View rootView;
    private NetworkCalls networkCalls;
    private DialogBuilder dialogBuilder;
    private UserDTO userDTO;
    private CreateCheckoutResponseDTO checkoutResponseDTO;
    private String discountCodeStr, spentRuleId, encodedCartData;
    private List<String> spentRuleList;
    private Map<Integer, Integer> spentRuleMap;
    private boolean isViewFlip, isSpentRuleAvailable;
    private HttpNetworkCall httpNetworkCall;
    private double totalCredits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        spentRuleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    isSpentRuleAvailable = false;
                    updateButtonForSpentRule(false);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isSpentRuleAvailable = true;
                            spentRuleId = spentRuleMap.get(position) + "";
                            updateButtonForSpentRule(true);
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getExtras();
        init();
        //new GetSpentRuleTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isDoubleFinish) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @OnClick(R.id.btn_sad_apply_discount)
    void setApplyDiscount() {
        if (Validations.isStringNotEmptyAndNull(etDiscountCode.getText().toString())) {
            discountCodeStr = etDiscountCode.getText().toString();
            applyDiscountCodeCall();
        }
    }

    @OnClick(R.id.btn_sad_next)
    void setNext() {
        if (isSpentRuleAvailable) {
            new ApplySpentRuleTask().execute();
        } else {
            String url = AppSpace.sharedPref.readValue(LAST_CHECKOUT_WEB_URL, "0");
            if (url.equals("0")) {
                MessageUtil.showSnackbarMessage(rootView, "Something Went Wrong!");
            } else {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                isDoubleFinish = true;
            }
        }
    }

    @OnTextChanged(R.id.et_sad_discount_code)
    void setEtDiscountCode() {
        if (etDiscountCode.getText().toString().isEmpty()) {
            btnApplyDiscount.setBackgroundColor(getResources().getColor(R.color.grey));
        } else {
            btnApplyDiscount.setBackgroundColor(getResources().getColor(R.color.themeBlue));
        }
    }

    @Override
    public void onPreServiceCall() {
        showLoadingDialog();
    }

    @Override
    public void onSuccess(int responseCode, Response response) {
        hideLoadingDialog();
        if (responseCode == APPLY_DISCOUNT) {
            handleCallbackResponse(response);
        }
    }

    @Override
    public void onFailure(int responseCode, ApolloException exception) {
        hideLoadingDialog();
        Log.e(LOG_TAG, exception.getMessage());
    }

    private void getExtras() {
        checkoutResponseDTO = (CreateCheckoutResponseDTO) getIntent().getExtras().getSerializable("checkoutDto");
    }

    private void init() {
        rootView = findViewById(android.R.id.content);
        networkCalls = new NetworkCalls(this, this);
        dialogBuilder = new DialogBuilder(this);
        spentRuleList = new ArrayList<>();
        spentRuleMap = new HashMap<>();
        httpNetworkCall = new HttpNetworkCall(this, this);
        userDTO = AppSpace.localDbOperations.getUser();
        makeCartDataObj();
        updateDataInUI();
    }

    private void flipView() {
        if (isViewFlip) {
            isViewFlip = false;
            viewFlipper.setDisplayedChild(0);
        } else {
            isViewFlip = true;
            viewFlipper.setDisplayedChild(1);
        }
    }

    private void updateButtonForSpentRule(boolean isActive) {
        if (isActive) {
            btnNext.setText("Apply Spent Rule");
        } else {
            btnNext.setText("Continue To Checkout");
        }
    }

    private void makeCartDataObj() {
        JSONObject jsonObject = new JSONObject();
        JSONArray JSONlineItems = new JSONArray();
        try {
            jsonObject.put("requires_shipping" , checkoutResponseDTO.requiresShipping);
            for (ItemsItem item : checkoutResponseDTO.items) {
                JSONObject obj = new JSONObject();
                obj.put("tags" , "");
                obj.put("id" , getDecoded(item.id));
                obj.put("quantity" , item.quantity);
                obj.put("price" , Double.parseDouble(String.format("%.2f", Double.parseDouble(item.price) * 100)));
                JSONlineItems.put(obj);
            }
            jsonObject.put("items" , JSONlineItems);
            jsonObject.put("total_price" , Double.parseDouble(String.format("%.2f", Double.parseDouble(checkoutResponseDTO.totalPrice) * 100)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        encodedCartData = getEncoded(JsonWriter.formatJson(jsonObject.toString()));
    }

    private void makeSpentRuleCall() {
        httpNetworkCall.get_available_spent_rule(AppSpace.sharedPref.readValue(CUSTOMER_ID, "0"), encodedCartData);
    }

    private void updateDataInUI() {
        if (Validations.isObjectNotEmptyAndNull(checkoutResponseDTO.shippingAddress)) {
            tvContactInfo.setText(userDTO.getEmail());
            tvShippingInfo.setText(checkoutResponseDTO.shippingAddress.getAddress1() + " ," + checkoutResponseDTO.shippingAddress.getAddress2() + " ," +
                    checkoutResponseDTO.shippingAddress.getCity() + " ," + checkoutResponseDTO.shippingAddress.getPostalCode() + " ," + checkoutResponseDTO.shippingAddress.getState() + " ," +
                    checkoutResponseDTO.shippingAddress.getCountry() + ".\n" + checkoutResponseDTO.shippingAddress.getPhone());
            tvSubTotal.setText(checkoutResponseDTO.totalPrice);
            tvTotal.setText(checkoutResponseDTO.totalPrice);
        }
    }

    private void applyDiscountCodeCall() {
        networkCalls.applyDiscount(AppSpace.sharedPref.readValue(LAST_CHECKOUT_ID, "0"), discountCodeStr);
    }

    private void handleCallbackResponse(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response)) {
            ApplyDiscountMutation.Data data = (ApplyDiscountMutation.Data) response.data();
            if (data.checkoutDiscountCodeApplyV2().checkoutUserErrors().size() > 0) {
                MessageUtil.showSnackbarMessage(rootView, data.checkoutDiscountCodeApplyV2().checkoutUserErrors().get(0).message());
            } else {
                if (Validations.isStringNotEmptyAndNull(data.checkoutDiscountCodeApplyV2().checkout().id())) {
                    AppSpace.sharedPref.writeValue(LAST_CHECKOUT_WEB_URL, data.checkoutDiscountCodeApplyV2().checkout().webUrl());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MessageUtil.showSnackbarMessage(rootView, "Discount Applied");
                            tvSubTotal.setText(data.checkoutDiscountCodeApplyV2().checkout().subtotalPrice());
                            tvTotal.setText(data.checkoutDiscountCodeApplyV2().checkout().totalPrice());
                        }
                    });

                } else {
                    MessageUtil.showSnackbarMessage(rootView, "Invalid Data!");
                }
            }
        }
    }

    private void showLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.loadingDialog("Loading...");
            }
        });
    }

    private void hideLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.dismissDialog();
            }
        });
    }

    @Override
    public void onSuccess(int responseCode, Object object) {
        if (responseCode == GET_SPENT_RULE) {
            handleSpentRuleCallbackResponse(object);
        }

        if (responseCode == APPLY_SPENT_RULE) {
            hideLoadingDialog();
            handleApplySpentRuleCallback(object);
        }
    }

    @Override
    public void onFailure(int responseCode, String message) {
        hideLoadingDialog();
        if (responseCode == GET_SPENT_RULE) {

        }
    }

    public class GetSpentRuleTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            makeSpentRuleCall();
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

    private void handleSpentRuleCallbackResponse(Object object) {
        if (Validations.isObjectNotEmptyAndNull(object)) {
            GetSpentRuleDTO data = (GetSpentRuleDTO) object;

            //totalCredits = data.code.totalCredits / 100;
            spentRuleList.clear();
            spentRuleMap.clear();

            if (Validations.isObjectNotEmptyAndNull(data.code.rules)) {
                spentRuleList.add("Select option to use store credit");
                int key = 1;
                for (RulesItem item : data.code.rules) {
                    if (item.isEligible) {
                        spentRuleList.add("You can use $" + item.applicableCredits / 100 + " out of $" + totalCredits);
                        spentRuleMap.put(key, item.rule.id);
                        key++;
                    }
                }
            } else {
                spentRuleList.add("Store credits not available");
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (spentRuleList.size() > 1) {
                        layoutSpentRule.setVisibility(View.VISIBLE);
                    } else {
                        layoutSpentRule.setVisibility(View.GONE);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ShippingAddressActivity.this, android.R.layout.simple_list_item_1, spentRuleList);
                    spentRuleSpinner.setAdapter(adapter);
                }
            });
        }
    }

    private String getDecoded(String value) {
        byte[] data = Base64.decode(value, Base64.DEFAULT);
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getEncoded(String value) {
        byte[] data = new byte[0];
        try {
            data = value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    private long trimAndDecodeId(String id) {
        String decodedStr = getDecoded(id);
        String trimedId = decodedStr.replace("gid://shopify/ProductVariant/", "");
        return Long.parseLong(trimedId);
    }

    public class ApplySpentRuleTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            makeApplySpentRuleCall();
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

    private void makeApplySpentRuleCall() {
        showLoadingDialog();
        httpNetworkCall.applySpentRule(AppSpace.sharedPref.readValue(CUSTOMER_ID, "0"), AppSpace.sharedPref.readValue(LAST_CHECKOUT_ID, "0"), spentRuleId, encodedCartData);
    }


    private void handleApplySpentRuleCallback(Object object) {
        if (Validations.isObjectNotEmptyAndNull(object)) {
            ApplySpentRulesDTO data = (ApplySpentRulesDTO) object;
            if (Validations.isObjectNotEmptyAndNull(data)) {
                if (data.status) {
                    isSpentRuleAvailable = false;
                    discountCodeStr = data.code;
                    applyDiscountCodeCall();
//                    String url = AppSpace.sharedPref.readValue(LAST_CHECKOUT_WEB_URL, "0");
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(browserIntent);
//                    isDoubleFinish = true;
                } else {
                    isSpentRuleAvailable = false;
                    MessageUtil.showSnackbarMessage(rootView, "Store credits can't applied.");
                }
                updateButtonForSpentRule(false);
            }
        }
    }
}
