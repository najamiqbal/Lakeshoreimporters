package com.dleague.lakeshoreimporters.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dleague.lakeshoreimporters.GetLastOrdersQuery;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.dtos.LastOrderItemDTO;
import com.dleague.lakeshoreimporters.dtos.ProductDTO;
import com.dleague.lakeshoreimporters.listeners.ItemClickListener;
import com.dleague.lakeshoreimporters.utils.NestedRecyclerLinearLayoutManager;
import com.dleague.lakeshoreimporters.utils.TimeUtils;
import com.dleague.lakeshoreimporters.utils.Validations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    List<GetLastOrdersQuery.Edge> ordersList;
    Context context;
    TimeUtils timeUtils;
    ItemClickListener itemClickListener;
    public MyOrdersAdapter(Context context, List<GetLastOrdersQuery.Edge> ordersList, ItemClickListener itemClickListener) {
        this.ordersList = ordersList;
        this.itemClickListener = itemClickListener;
        this.context = context;
        timeUtils = new TimeUtils();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout_my_orders, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        GetLastOrdersQuery.Edge object = ordersList.get(i);
        viewHolder.tvOrderNumber.setText("Order " + String.valueOf(object.node().name()));
        Date date = (Date) object.node().processedAt();
        viewHolder.tvOrderDate.setText(timeUtils.getDate(date.getTime()/1000));
        viewHolder.tvArrivalDateEstimation.setText("Estimate Host Arrival Date: " + timeUtils.getDateForNext7weeks(date.getTime()/1000));
        if(Validations.isObjectNotEmptyAndNull(object.node().shippingAddress())){
            viewHolder.tvShippingAddress.setText(object.node().shippingAddress().firstName() + " " +
                    object.node().shippingAddress().lastName() + "\n" + object.node().shippingAddress().address1() + " "
                    + object.node().shippingAddress().address2() + object.node().shippingAddress().city() + " "+
                    object.node().shippingAddress().zip() + " " + object.node().shippingAddress().province() + " " +
                    object.node().shippingAddress().country());
        }

        String price = object.node().totalPrice();
        int quantity = 0;
        List<LastOrderItemDTO> itemsList = new ArrayList<>();
        if(object.node().lineItems().edges().size()>0) {
            itemsList = getSubProducts(object.node().lineItems());
        }

        for (LastOrderItemDTO item : itemsList){
            quantity = quantity + item.getQuantity();
        }
        viewHolder.tvTotalItems_Price.setText(quantity + " items Total:  " + price);
        if(!itemsList.isEmpty()) {
            initChildLayoutManager(viewHolder.recyclerViewChild, itemsList);
        }
    }

    private List<LastOrderItemDTO> getSubProducts(GetLastOrdersQuery.LineItems lineItems){

        List<LastOrderItemDTO> tempProductList = new ArrayList<>();
        for (GetLastOrdersQuery.Edge1 item: lineItems.edges()){
            tempProductList.add(new LastOrderItemDTO(item.node().title(), "", item.node().quantity(), ""));
        }

        return tempProductList;
    }

    private void initChildLayoutManager(RecyclerView rv_child, List<LastOrderItemDTO> childData) {
        rv_child.setLayoutManager(new NestedRecyclerLinearLayoutManager(context));
        MyOrdersChildAdapter childAdapter = new MyOrdersChildAdapter(childData);
        rv_child.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
       return ordersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvOrderNumber, tvOrderDate, tvShippingAddress, tvTotalItems_Price, tvArrivalDateEstimation;
        RecyclerView recyclerViewChild;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderNumber = itemView.findViewById(R.id.tv_mo_order_number);
            tvOrderDate = itemView.findViewById(R.id.tv_mo_order_date);
            tvShippingAddress = itemView.findViewById(R.id.tv_mo_order_address);
            tvTotalItems_Price = itemView.findViewById(R.id.tv_mo_order_total);
            tvArrivalDateEstimation = itemView.findViewById(R.id.tv_mo_order_arrival_date);
            recyclerViewChild = itemView.findViewById(R.id.recyclerview_my_order_items);
        }
    }
}
