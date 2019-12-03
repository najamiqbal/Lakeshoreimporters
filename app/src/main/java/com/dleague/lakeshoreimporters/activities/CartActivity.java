package com.dleague.lakeshoreimporters.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.dleague.lakeshoreimporters.CreateCheckoutMutation;
import com.dleague.lakeshoreimporters.GetCustomerAddressesQuery;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.adapters.CartAdapter;
import com.dleague.lakeshoreimporters.dtos.AddressDTO;
import com.dleague.lakeshoreimporters.dtos.CartDTO;
import com.dleague.lakeshoreimporters.dtos.UserDTO;
import com.dleague.lakeshoreimporters.dtos.responsedto.CreateCheckoutResponseDTO;
import com.dleague.lakeshoreimporters.dtos.responsedto.Data;
import com.dleague.lakeshoreimporters.dtos.responsedto.ItemsItem;
import com.dleague.lakeshoreimporters.listeners.ItemClickListener;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCallbacks;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCalls;
import com.dleague.lakeshoreimporters.type.CheckoutCreateInput;
import com.dleague.lakeshoreimporters.type.CheckoutLineItemInput;
import com.dleague.lakeshoreimporters.type.MailingAddressInput;
import com.dleague.lakeshoreimporters.utils.DialogBuilder;
import com.dleague.lakeshoreimporters.utils.MessageUtil;
import com.dleague.lakeshoreimporters.utils.Validations;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dleague.lakeshoreimporters.activities.AppSpace.cartItemCount;
import static com.dleague.lakeshoreimporters.activities.AppSpace.isDoubleFinish;
import static com.dleague.lakeshoreimporters.utils.Constants.CART_COUNT;
import static com.dleague.lakeshoreimporters.utils.Constants.CREATE_CHECKOUT;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_EMAIL;
import static com.dleague.lakeshoreimporters.utils.Constants.GET_CUSTOMER_ADDRESSES;
import static com.dleague.lakeshoreimporters.utils.Constants.LAST_CHECKOUT_ID;
import static com.dleague.lakeshoreimporters.utils.Constants.LAST_CHECKOUT_WEB_URL;
import static com.dleague.lakeshoreimporters.utils.Constants.LOG_TAG;
import static com.dleague.lakeshoreimporters.utils.Constants.SESSION;

public class CartActivity extends AppCompatActivity implements ItemClickListener, NetworkCallbacks {

    @BindView(R.id.cart_toolbar)
    Toolbar toolbar;

    @BindView(R.id.viewflipper_cart)
    ViewFlipper viewFlipper;
    @BindView(R.id.recyclerview_cart)
    RecyclerView recyclerView;
    @BindView(R.id.tv_pd_products_count)
    TextView tvProductsTotalCount;
    @BindView(R.id.tv_pd_total_price)
    TextView tvProductsTotalPrice;
    @BindView(R.id.tv_pd_continue_shopping)
    TextView tvContinueShopping;
    @BindView(R.id.tv_pd_checkout)
    TextView tvCheckout;
    @BindView(R.id.et_cart_order_note)
    EditText etOrderNote;
    @BindView(R.id.cb1_cart)
    CheckBox cb1;
    @BindView(R.id.cb2_cart)
    CheckBox cb2;

    private View rootView;
    private CartAdapter cartAdapter;
    private List<CartDTO> cartDTOList;
    private int isViewFlipped;
    private NetworkCalls networkCalls;
    private DialogBuilder dialogBuilder;
    private UserDTO userDTO;
    private List<AddressDTO> addressDTOList;
    private AddressDTO shippingAddress;
    private CreateCheckoutResponseDTO checkoutResponseDTO;
    private boolean isDefualtAddressAdded;
    private int cartItemsQuantity;
    private double totalPrice;
    private String currencyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();
        getDefaultAddress();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isDoubleFinish) {
            cartItemCount = 0;
            isDoubleFinish = false;
            AppSpace.sharedPref.writeValue(CART_COUNT, 0);
            AppSpace.sharedPref.writeValue(LAST_CHECKOUT_ID, "0");
            AppSpace.sharedPref.writeValue(LAST_CHECKOUT_WEB_URL, "0");
            AppSpace.localDbOperations.deleteAllCartItems();
            finish();
        }
    }

    private void getDefaultAddress() {
        networkCalls.getCustomerAddresses(AppSpace.sharedPref.readValue(SESSION, "0"));
    }

    @OnClick(R.id.tv_pd_continue_shopping)
    void setContinueShopping() {
        finish();
    }

    @OnClick(R.id.tv_pd_checkout)
    void setCheckout() {
        if (cb1.isChecked()) {
            if (cb2.isChecked()) {
                if(isDefualtAddressAdded) {
                    List<CheckoutLineItemInput> lineItemInputList = new ArrayList<>();
                    for (CartDTO cart : cartDTOList) {
                        CheckoutLineItemInput input = CheckoutLineItemInput.builder()
                                .quantity(cart.getQuantity())
                                .variantId(cart.getVariantsId())
                                .build();
                        lineItemInputList.add(input);
                    }
                    MailingAddressInput addressInput = MailingAddressInput.builder()
                            .firstName(shippingAddress.getFirstName())
                            .lastName(shippingAddress.getLastName())
                            .company(shippingAddress.getCompany())
                            .address1(shippingAddress.getAddress1())
                            .address2(shippingAddress.getAddress2())
                            .city(shippingAddress.getCity())
                            .province(shippingAddress.getState())
                            .country(shippingAddress.getCountry())
                            .zip(shippingAddress.getPostalCode())
                            .phone(shippingAddress.getPhone()).build();
                    CheckoutCreateInput input = CheckoutCreateInput.builder()
                            .email(AppSpace.sharedPref.readValue(CUSTOMER_EMAIL, "0"))
                            .note(etOrderNote.getText().toString())
                            .shippingAddress(addressInput)
                            .lineItems(lineItemInputList).build();
                    checkoutCall(input);
                }else{
                    MessageUtil.showSnackbarMessage(rootView, "Please set your default address first.");
                }

            } else {
                MessageUtil.showSnackbarMessage(rootView, "Please agree with Liabilities policies.");
            }
        } else {
            MessageUtil.showSnackbarMessage(rootView, "Please agree with privacy policies.");
        }
    }

    @Override
    public void onItemClick(int position, Object object) {
        CartDTO item = (CartDTO) object;

        if (position == -1) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    totalPrice = totalPrice + Double.parseDouble(item.maxPrice);
                    tvProductsTotalPrice.setText(currencyCode + " $" + String.format("%.2f", totalPrice));
                }
            });
        } else if (position == -2) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    totalPrice = totalPrice - Double.parseDouble(item.maxPrice);
                    tvProductsTotalPrice.setText(currencyCode + " $" + String.format("%.2f", totalPrice));
                }
            });
        } else {
            AppSpace.localDbOperations.deleteCartItem(item.cursor);
            cartDTOList.remove(position);
            AppSpace.cartItemCount--;
            updateCart();
            updateUI();
            cartAdapter.notifyDataSetChanged();
        }
    }

    private void init() {
        rootView = findViewById(android.R.id.content);
        networkCalls = new NetworkCalls(this, this);
        dialogBuilder = new DialogBuilder(this);
        userDTO = AppSpace.localDbOperations.getUser();

        if (AppSpace.cartItemCount > 0) {
            cartDTOList = AppSpace.localDbOperations.getAllCartItems();
            cartAdapter = new CartAdapter(this, cartDTOList, this);
            recyclerView.setAdapter(cartAdapter);
            cartAdapter.notifyDataSetChanged();
            updateUI();
        } else {
            cartDTOList = new ArrayList<>();
            flipView(1);
        }
    }

    private void checkoutCall(CheckoutCreateInput checkoutData) {
        networkCalls.createCheckoout(checkoutData);
    }

    private void flipView(int index) {
        if (index == 0) {
            isViewFlipped = 0;
            viewFlipper.setDisplayedChild(index);
        } else if (index == 1) {
            isViewFlipped = 1;
            viewFlipper.setDisplayedChild(index);
        }
    }

    public void updateCart() {
        AppSpace.sharedPref.writeValue(CART_COUNT, cartItemCount);
    }

    private void updateUI() {
        currencyCode = "";
        cartItemsQuantity = cartDTOList.size();
        tvProductsTotalCount.setText("Total products in cart " + cartItemsQuantity);
        totalPrice = 0;
        for (CartDTO obj : cartDTOList) {
            totalPrice = totalPrice + (Double.parseDouble(obj.getMaxPrice()) * obj.quantity);
            currencyCode = obj.getCurrenceyCode();
        }
        tvProductsTotalPrice.setText(currencyCode + " $" + String.format("%.2f", totalPrice));
    }

    @Override
    public void onPreServiceCall() {
        showLoadingDialog();
    }

    @Override
    public void onSuccess(int responseCode, Response response) {
        hideLoadingDialog();
        if (responseCode == CREATE_CHECKOUT) {
            handleCallbackResponse(response);
        }

        if (responseCode == GET_CUSTOMER_ADDRESSES) {
            handleAddressCallback(response);
        }
    }

    @Override
    public void onFailure(int responseCode, ApolloException exception) {
        hideLoadingDialog();
        Log.i(LOG_TAG, exception.getMessage());
        MessageUtil.showSnackbarMessage(rootView, exception.getMessage());
    }

    private void handleCallbackResponse(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response)) {
            CreateCheckoutMutation.Data data = (CreateCheckoutMutation.Data) response.data();
            if (data.checkoutCreate().checkoutUserErrors().size() > 0) {
                MessageUtil.showSnackbarMessage(rootView, data.checkoutCreate().checkoutUserErrors().get(0).message());
            } else {
                if (Validations.isObjectNotEmptyAndNull(data) && Validations.isObjectNotEmptyAndNull(data.checkoutCreate().checkout())) {
                    if (!Validations.isStringEmpty(data.checkoutCreate().checkout().id())) {
                        AppSpace.sharedPref.writeValue(LAST_CHECKOUT_ID, data.checkoutCreate().checkout().id());
                        AppSpace.sharedPref.writeValue(LAST_CHECKOUT_WEB_URL, data.checkoutCreate().checkout().webUrl());
                        List<ItemsItem> lineItems = new ArrayList<>();
                        if (Validations.isObjectNotEmptyAndNull(data.checkoutCreate().checkout().lineItems())) {
                            for (CreateCheckoutMutation.Edge item : data.checkoutCreate().checkout().lineItems().edges()) {
                                lineItems.add(new ItemsItem(item.node().quantity(), item.node().variant().price(), item.node().variant().id(), ""));
                            }
                        }
                        checkoutResponseDTO = new CreateCheckoutResponseDTO(String.format("%.2f", totalPrice),
                                data.checkoutCreate().checkout().requiresShipping(), shippingAddress, lineItems);
                        Intent intent = new Intent(CartActivity.this, ShippingAddressActivity.class);
                        intent.putExtra("checkoutDto", checkoutResponseDTO);
                        startActivity(intent);
                    }
                } else {
                    MessageUtil.showSnackbarMessage(rootView, "Checkout call failed!");
                }
            }
        }
    }

    private void handleAddressCallback(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response)) {
            GetCustomerAddressesQuery.Data data = (GetCustomerAddressesQuery.Data) response.data();
            if (Validations.isObjectNotEmptyAndNull(data.customer()) && Validations.isObjectNotEmptyAndNull(data.customer().defaultAddress())) {
                isDefualtAddressAdded = true;
                shippingAddress = new AddressDTO(data.customer().defaultAddress().id(), data.customer().defaultAddress().firstName(),
                        data.customer().defaultAddress().lastName(), data.customer().defaultAddress().company(), data.customer().defaultAddress().address1(),
                        data.customer().defaultAddress().address2(), data.customer().defaultAddress().city(), data.customer().defaultAddress().province(),
                        data.customer().defaultAddress().country(), data.customer().defaultAddress().zip(), data.customer().defaultAddress().phone(),
                        data.customer().defaultAddress().id());
            } else {
                isDefualtAddressAdded = false;
                MessageUtil.showSnackbarMessage(rootView, "Please set your default address first.");
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
}
