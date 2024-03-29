package com.dleague.lakeshoreimporters.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.dtos.ProductDTO;
import com.dleague.lakeshoreimporters.listeners.ItemClickListener;
import com.dleague.lakeshoreimporters.utils.Validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public int currentItemPosition, totalSize;
    private Context context;
    private List<ProductDTO> productDTOList;
    private ItemClickListener itemClickListener;
    private ArrayList<ProductDTO> arrayList;

    public ProductAdapter(Context context, List<ProductDTO> productDTOList, ItemClickListener itemClickListener) {
        this.context = context;
        this.productDTOList = productDTOList;
        this.itemClickListener = itemClickListener;
        this.arrayList=new ArrayList<ProductDTO>();
        this.arrayList.addAll(productDTOList);
    }

    public void updateProductsList(List<ProductDTO> productDTOList){
        this.productDTOList = productDTOList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_layout, viewGroup, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        currentItemPosition = position;
        ProductDTO productDTO = productDTOList.get(position);
        if(!productDTO.isAvailableForSale()){
            holder.availableForSale.setVisibility(View.VISIBLE);
        }else{
            holder.availableForSale.setVisibility(View.GONE);
        }
        String displayPrice = null;
        if(productDTO.getProductPriceMax().equals(productDTO.getProductPriceMin())){
            displayPrice = "$" + productDTO.getProductPriceMax();
        }else{
            displayPrice = "From $" + productDTO.getProductPriceMin();
        }
        holder.name.setText(productDTO.getProductName());
        Log.d("PRODUCT","NAME"+productDTO.getProductName());
        holder.price.setText(displayPrice);
        if(Validations.isObjectNotEmptyAndNull(productDTO.getImagesUrl()) &&  productDTO.getImagesUrl().size()>0) {
            if (Validations.isStringNotEmptyAndNull(productDTO.getImagesUrl().get(0))) {
                Glide.with(context).load(productDTO.getImagesUrl().get(0)).into(holder.image);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(position, productDTO);
            }
        });
    }
    @Override
    public int getItemCount() {
        return productDTOList.size();
    }

    public void increaseCountOfShowing() {

    }


    public class ProductViewHolder extends RecyclerView.ViewHolder{
        // init the item view's
        TextView name, price, availableForSale;
        ImageView image;
        public ProductViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.tv_item_home_name);
            price = view.findViewById(R.id.tv_item_home_price);
            image = view.findViewById(R.id.iv_item_home_image);
            availableForSale = view.findViewById(R.id.tv_item_home_soldout);
        }
    }
//    //filter
//    public void filter(String charText){
//        charText = charText.toLowerCase(Locale.getDefault());
//        Log.d("PRODUCT","NAME"+charText);
//        productDTOList.clear();
//        if (charText.length()==0){
//            Log.d("PRODUCT","NAME"+arrayList.size());
//            productDTOList.addAll(arrayList);
//        }
//        else {
//            for (ProductDTO model : arrayList){
//                Log.d("PRODUCT","NAME"+model.getProductName());
//                if (model.getProductName().toLowerCase(Locale.getDefault())
//                        .contains(charText)){
//                    productDTOList.add(model);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

    public void filterList(ArrayList<ProductDTO> filterdNames) {
        this.productDTOList = filterdNames;
        notifyDataSetChanged();
    }
}
