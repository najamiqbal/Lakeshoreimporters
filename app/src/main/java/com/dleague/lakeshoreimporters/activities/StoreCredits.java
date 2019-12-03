package com.dleague.lakeshoreimporters.activities;

import android.os.AsyncTask;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.adapters.StoreCreditsAdapter;
import com.dleague.lakeshoreimporters.dtos.responsedto.customer_credit_response.CustomerCreditDTO;
import com.dleague.lakeshoreimporters.httpcalls.HttpNetworkCall;
import com.dleague.lakeshoreimporters.httpcalls.HttpResponseCallbacks;
import com.dleague.lakeshoreimporters.listeners.ItemClickListener;
import com.dleague.lakeshoreimporters.utils.DialogBuilder;
import com.dleague.lakeshoreimporters.utils.MessageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_ID;

public class StoreCredits extends AppCompatActivity implements HttpResponseCallbacks, ItemClickListener {

    @BindView(R.id.toolbar_simple)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_simple)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_earn_credits)
    TextView tvEarnCredits;
    @BindView(R.id.tv_spent_credits)
    TextView tvSpentCredits;
    @BindView(R.id.tv_current_credits)
    TextView tvCurrentCredits;
    @BindView(R.id.recyclerview_store_credits)
    RecyclerView recyclerView;
    @BindView(R.id.viewflipper_sc)
    ViewFlipper viewFlipper;
    @BindView(R.id.layout_sc_detail)
    LinearLayout layoutSCDetail;
    private View rootView;
    private HttpNetworkCall httpNetworkCall;
    private DialogBuilder dialogBuilder;
    private StoreCreditsAdapter adapter;
    private boolean isViewFlip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_credits);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvToolbarTitle.setText("Store Credits");

        init();
        if (AppSpace.sharedPref.readValue(CUSTOMER_ID, "0").equals("0")) {

        } else {
            getStoreCredits();
        }
    }

    private void init() {
        rootView = findViewById(android.R.id.content);
        httpNetworkCall = new HttpNetworkCall(this, this);
        dialogBuilder = new DialogBuilder(this);
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

    private void getStoreCredits() {
        showLoadingDialog();
        new StoreCreditsTask().execute();
    }


    private void handleCallbackResponse(Object object) {
        CustomerCreditDTO data = (CustomerCreditDTO) object;
        if (data.isStatus()) {
            updateDataInUI(data);
        } else {
            MessageUtil.showSnackbarMessage(rootView, "Invalid Store Credit Response!");
        }
    }

    private void updateDataInUI(CustomerCreditDTO data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                double spent = data.getCustomer().getTotalSpentCredits() / 100;
                tvEarnCredits.setText("Earned Credits " + data.getCustomer().getTotalEarnedCredits() / 100 + "$");
                tvSpentCredits.setText("Spent Credits " + roundTo2DecimalPlaces(spent) + "$");
                tvCurrentCredits.setText("Current Credits " + roundTo2DecimalPlaces(data.getCustomer().getCredits() / 100 ) + "$");
                if (data.getCustomer().getCreditLog().size() > 0) {
                    adapter = new StoreCreditsAdapter(StoreCredits.this, data.getCustomer().getCreditLog(), StoreCredits.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    flipView();
                } else {

                }
            }
        });
    }

    private String roundTo2DecimalPlaces(Double d) {
        return String.format("%.2f", d);
    }

    @Override
    public void onSuccess(int responseCode, Object object) {
        hideLoadingDialog();
        handleCallbackResponse(object);
    }

    @Override
    public void onFailure(int responseCode, String message) {
        hideLoadingDialog();
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
    public void onItemClick(int position, Object object) {

    }

    public class StoreCreditsTask extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            httpNetworkCall.getCustomerCredit(AppSpace.sharedPref.readValue(CUSTOMER_ID, "0"));
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
