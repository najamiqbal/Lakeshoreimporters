package com.dleague.lakeshoreimporters.adapters;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.dtos.LastOrderItemDTO;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersChildAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<LastOrderItemDTO> childData;

    public MyOrdersChildAdapter(List<LastOrderItemDTO> childData) {
        this.childData = childData;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView productName, productPrice, productQuantity;
        View bottomLine;
        public ViewHolder(View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_moi_image);
            productName = itemView.findViewById(R.id.tv_moi_title);
            productPrice = itemView.findViewById(R.id.tv_moi_price);
            productQuantity = itemView.findViewById(R.id.tv_moi_quantity);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_my_order_item, parent, false);

        MyOrdersChildAdapter.ViewHolder cavh = new MyOrdersChildAdapter.ViewHolder(itemLayoutView);
        return cavh;
    }


    final Handler handler = new Handler();
    Runnable collapseList = new Runnable() {
        @Override
        public void run() {
            if (getItemCount() > 1) {
                childData.remove(childData.size() - 1);
                notifyDataSetChanged();
                handler.postDelayed(collapseList, 50);
            }
        }
    };

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder vh = (ViewHolder) holder;
        LastOrderItemDTO object = childData.get(position);
        vh.productName.setText("Product Name:   " +object.getTitle());
        vh.productQuantity.setText("Quantity:   " + object.getQuantity());
    }

    @Override
    public int getItemCount() {
        return childData.size();
    }
}
