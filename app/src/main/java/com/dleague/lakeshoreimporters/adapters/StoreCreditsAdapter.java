package com.dleague.lakeshoreimporters.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.dtos.responsedto.customer_credit_response.CreditLogsDTO;
import com.dleague.lakeshoreimporters.dtos.responsedto.customer_credit_response.CustomerCreditDTO;
import com.dleague.lakeshoreimporters.listeners.ItemClickListener;

import java.util.List;

public class StoreCreditsAdapter extends RecyclerView.Adapter<StoreCreditsAdapter.ProductViewHolder> {

    private Context context;
    private List<CreditLogsDTO> creditLogsDTOList;
    private ItemClickListener itemClickListener;
    private String defaultAddressId;

    public StoreCreditsAdapter(Context context, List<CreditLogsDTO> creditLogsDTOList, ItemClickListener itemClickListener) {
        this.context = context;

        this.creditLogsDTOList = creditLogsDTOList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_store_credit_item, viewGroup, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        double earned = creditLogsDTOList.get(position).getCredits() / 100;
        holder.tv1.setText("Earned $" + roundTo2DecimalPlaces(earned));
        holder.tv2.setText(creditLogsDTOList.get(position).getComment());
        holder.tv3.setText(creditLogsDTOList.get(position).getCreatedAt());
    }

    private String roundTo2DecimalPlaces(Double d) {
        return String.format("%.2f", d);
    }

    @Override
    public int getItemCount() {
        return creditLogsDTOList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView tv1, tv2, tv3;

        public ProductViewHolder(View view) {
            super(view);
            tv1 = view.findViewById(R.id.tv_item_sc_1);
            tv2 = view.findViewById(R.id.tv_item_sc_2);
            tv3 = view.findViewById(R.id.tv_item_sc_3);
        }
    }
}
