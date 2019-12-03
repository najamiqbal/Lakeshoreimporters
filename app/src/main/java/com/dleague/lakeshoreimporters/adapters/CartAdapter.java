package com.dleague.lakeshoreimporters.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.dtos.AddressDTO;
import com.dleague.lakeshoreimporters.dtos.CartDTO;
import com.dleague.lakeshoreimporters.listeners.ItemClickListener;
import com.dleague.lakeshoreimporters.utils.MessageUtil;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ProductViewHolder> {

    public int currentItemPosition, totalSize;
    private Context context;
    private List<CartDTO> cartDTOList;
    private ItemClickListener itemClickListener;
    public CartAdapter(Context context, List<CartDTO> cartDTOList, ItemClickListener itemClickListener) {
        this.context = context;
        this.cartDTOList = cartDTOList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cart_layout, viewGroup, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        currentItemPosition = position;
        CartDTO obj = cartDTOList.get(position);

        holder.tvTitle.setText(obj.getTitle());
        holder.tvQuantity.setText(String.valueOf(obj.getQuantity()));
        String displayPrice = null;
        if(obj.getMaxPrice().equals(obj.getMinPirce())){
            displayPrice = "$" + obj.getMaxPrice();
        }else{
            displayPrice = "From $" + obj.getMinPirce();
        }


        holder.tvPrice.setText(displayPrice);
        Glide.with(context).load(obj.getImageURL()).into(holder.ivImage);
        holder.ivAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = obj.getQuantity() + 1;
                obj.setQuantity(quantity);
                holder.tvQuantity.setText(String.valueOf(obj.getQuantity()));
                itemClickListener.onItemClick(-1, obj);
            }
        });

        holder.ivSubtractQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(obj.getQuantity() == 1){

                }else {
                    int quantity = obj.getQuantity() - 1;
                    obj.setQuantity(quantity);
                    holder.tvQuantity.setText(String.valueOf(obj.getQuantity()));
                    itemClickListener.onItemClick(-2, obj);
                }
            }
        });

        holder.ibDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(position, obj);
            }
        });
    }
    @Override
    public int getItemCount() {
        return cartDTOList.size();
    }

    public void increaseCountOfShowing() {

    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        // init the item view's
        TextView tvTitle, tvPrice, tvQuantity;
        ImageView ivAddQuantity, ivSubtractQuantity, ivImage;
        ImageButton ibDeleteItem;
        public ProductViewHolder(View view) {
            super(view);
            ivImage = view.findViewById(R.id.iv_cart_item_image);
            tvTitle = view.findViewById(R.id.tv_cart_item_title);
            tvPrice = view.findViewById(R.id.tv_cart_item_price);
            tvQuantity = view.findViewById(R.id.tv_cart_item_quantity);
            ivAddQuantity = view.findViewById(R.id.iv_cart_quantity_plus);
            ivSubtractQuantity = view.findViewById(R.id.iv_cart_quantity_subtract);
            ibDeleteItem = view.findViewById(R.id.btn_cart_item_delete);
        }
    }
}
