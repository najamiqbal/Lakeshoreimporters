package com.dleague.lakeshoreimporters.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.activities.AppSpace;
import com.dleague.lakeshoreimporters.dtos.AddressDTO;
import com.dleague.lakeshoreimporters.dtos.ProductDTO;
import com.dleague.lakeshoreimporters.listeners.ItemClickListener;
import com.dleague.lakeshoreimporters.utils.MessageUtil;
import com.dleague.lakeshoreimporters.utils.Validations;

import java.util.List;

import static com.dleague.lakeshoreimporters.utils.Constants.DEFAULT_ADDRESS_ID;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ProductViewHolder> {

    public int currentItemPosition, totalSize;
    private Context context;
    private List<AddressDTO> addressDTOS;
    private ItemClickListener itemClickListener;
    private String defaultAddressId;
    public AddressAdapter(Context context, List<AddressDTO> addressDTOS, ItemClickListener itemClickListener) {
        this.context = context;
        defaultAddressId = AppSpace.sharedPref.readValue(DEFAULT_ADDRESS_ID, "0");
        this.addressDTOS = addressDTOS;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout_address, viewGroup, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        currentItemPosition = position;
        AddressDTO obj = addressDTOS.get(position);
        if(obj.getCursor().equals(defaultAddressId)){
            holder.btnDefault.setVisibility(View.GONE);
        }
        holder.title.setText(obj.getFirstName() + " " + obj.getLastName());
        holder.subtitle.setText(obj.getAddress1() + ", " + obj.getAddress2() + ", " + obj.getCity()
                + ", " + obj.getState() + ", " + obj.getCountry());
        String str = String.valueOf(getItemId(position));

        holder.btnDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(1, obj);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(2, obj);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(3, obj);
            }
        });
    }
    @Override
    public int getItemCount() {
        return addressDTOS.size();
    }

    public void increaseCountOfShowing() {

    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        // init the item view's
        TextView title, subtitle;
        Button btnDelete, btnEdit, btnDefault;
        public ProductViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.item_address_title);
            subtitle = view.findViewById(R.id.item_address_subtitle);
            btnDelete = view.findViewById(R.id.btn_item_address_delete);
            btnEdit = view.findViewById(R.id.btn_item_address_edit);
            btnDefault = view.findViewById(R.id.btn_item_address_default);
        }
    }
}
