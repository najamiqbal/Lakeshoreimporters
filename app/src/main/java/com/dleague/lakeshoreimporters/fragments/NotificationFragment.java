package com.dleague.lakeshoreimporters.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.dleague.lakeshoreimporters.GetLastOrdersQuery;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.activities.AppSpace;
import com.dleague.lakeshoreimporters.activities.MyOrdersActivity;
import com.dleague.lakeshoreimporters.adapters.MyNotificationAdapter;
import com.dleague.lakeshoreimporters.adapters.MyOrdersAdapter;
import com.dleague.lakeshoreimporters.listeners.ItemClickListener;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCallbacks;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCalls;
import com.dleague.lakeshoreimporters.utils.DialogBuilder;
import com.dleague.lakeshoreimporters.utils.Validations;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dleague.lakeshoreimporters.utils.Constants.LAST_ORDERS;
import static com.dleague.lakeshoreimporters.utils.Constants.LOG_TAG;
import static com.dleague.lakeshoreimporters.utils.Constants.SESSION;

public class NotificationFragment extends AppCompatActivity implements NetworkCallbacks, ItemClickListener {

    @BindView(R.id.toolbar_simple)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title_simple)
    TextView toolbarTitle;
    @BindView(R.id.recyclerview_my_notification)
    RecyclerView recyclerView;

    private View rootView;
    private MyNotificationAdapter myOrdersAdapter;
    private NetworkCalls networkCalls;
    private DialogBuilder dialogBuilder;
    private List<GetLastOrdersQuery.Edge> lastOrdersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_fragment);
        ButterKnife.bind(this);
        toolbarTitle.setText("Notifications");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();
        getOrders();
    }

    @Override
    public void onPreServiceCall() {
        showLoadingDialog();
    }

    @Override
    public void onSuccess(int responseCode, Response response) {
        hideLoadingDialog();
        if(responseCode == LAST_ORDERS){
            handleCalbackResponse(response);
        }
    }

    @Override
    public void onFailure(int responseCode, ApolloException exception) {
        hideLoadingDialog();
        Log.e(LOG_TAG, exception.getMessage());
    }

    @Override
    public void onItemClick(int position, Object object) {

    }

    private void init(){
        networkCalls = new NetworkCalls(this, this);
        dialogBuilder = new DialogBuilder(this);
        lastOrdersList = new ArrayList<>();
    }

    private void getOrders(){
        networkCalls.getLastOrders(AppSpace.sharedPref.readValue(SESSION, "0"));
    }

    private void handleCalbackResponse(Response response) {
        if(Validations.isObjectNotEmptyAndNull(response)){
            GetLastOrdersQuery.Data data = (GetLastOrdersQuery.Data) response.data();
            if(Validations.isObjectNotEmptyAndNull(data)){
                lastOrdersList = data.customer().orders().edges();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NotificationFragment.this, ""+lastOrdersList.get(0).node().name(), Toast.LENGTH_SHORT).show();
                        myOrdersAdapter = new MyNotificationAdapter(NotificationFragment.this, lastOrdersList, NotificationFragment.this);
                        recyclerView.setAdapter(myOrdersAdapter);
                        myOrdersAdapter.notifyDataSetChanged();
                    }
                });
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
