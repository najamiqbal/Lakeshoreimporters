package com.dleague.lakeshoreimporters.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.dleague.lakeshoreimporters.GetNextProductsQuery;
import com.dleague.lakeshoreimporters.GetProductsQuery;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.activities.MainActivity;
import com.dleague.lakeshoreimporters.adapters.ProductAdapter;
import com.dleague.lakeshoreimporters.dtos.ProductDTO;
import com.dleague.lakeshoreimporters.listeners.ItemClickListener;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCallbacks;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCalls;
import com.dleague.lakeshoreimporters.utils.DialogBuilder;
import com.dleague.lakeshoreimporters.utils.MessageUtil;
import com.dleague.lakeshoreimporters.utils.Validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dleague.lakeshoreimporters.utils.Constants.GET_NEXT_PRODUCTS;
import static com.dleague.lakeshoreimporters.utils.Constants.GET_PRODUCTS;
import static com.dleague.lakeshoreimporters.utils.Constants.LOG_TAG;

public class HomeFragment extends Fragment implements NetworkCallbacks, ItemClickListener,View.OnClickListener{


    private View rootView;
    private NetworkCalls networkCalls;
    private DialogBuilder dialogBuilder;
    private ProductAdapter productAdapter;
    private List<ProductDTO> productDTOList;
    private String lastCursor;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10;
    ImageView about_img,weekly_img;
    private int visibleItemCount, pastVisibleItems, totalItemCount;
    private GridLayoutManager gridLayoutManager;
    private boolean hasNextPage, hasPreviousPage, isLoading, ignoreOnce;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        init();


      //  getFirstProducts();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

  /*  @OnClick(R.id.btn_home_buy_now)
    void setBuyNow() {
        ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(1);
    }*/

    @Override
    public void onItemClick(int position, Object object) {
        ProductDTO selectedProduct = (ProductDTO) object;
        if (Validations.isObjectNotEmptyAndNull(selectedProduct)) {
            ((MainActivity) Objects.requireNonNull(getActivity())).launchProductViewFragment(selectedProduct);
        } else {
            MessageUtil.showSnackbarMessage(rootView, "Something went wrong");
        }
    }

    @Override
    public void onPreServiceCall() {
        showLoadingDialog();
    }

    @Override
    public void onSuccess(int responseCode, Response response) {
        hideLoadingDialog();
        if (responseCode == GET_PRODUCTS) {
            handleCallbackResponse(response);
        } else if (responseCode == GET_NEXT_PRODUCTS) {
            handleNextCallbackResponse(response);
        }
    }

    @Override
    public void onFailure(int responseCode, ApolloException exception) {
        hideLoadingDialog();
        Log.e(LOG_TAG, exception.getMessage());
        Log.d("GET_PRODUCTS: ", Log.getStackTraceString(exception));
    }

    private void initView() {
        weekly_img=rootView.findViewById(R.id.weekly_img);
        about_img=rootView.findViewById(R.id.about_img);
        tv1=rootView.findViewById(R.id.tv1);
        tv2=rootView.findViewById(R.id.tv2);
        tv3=rootView.findViewById(R.id.tv3);
        tv4=rootView.findViewById(R.id.tv4);
        tv5=rootView.findViewById(R.id.tv5);
        tv6=rootView.findViewById(R.id.tv6);
        tv7=rootView.findViewById(R.id.tv7);
        tv8=rootView.findViewById(R.id.tv8);
        tv9=rootView.findViewById(R.id.tv9);
        tv10=rootView.findViewById(R.id.tv10);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        tv7.setOnClickListener(this);
        tv8.setOnClickListener(this);
        tv9.setOnClickListener(this);
        tv10.setOnClickListener(this);
        weekly_img.setOnClickListener(this);
        about_img.setOnClickListener(this);

        gridLayoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
//        recyclerView.setLayoutManager(gridLayoutManager);
        productDTOList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productDTOList, this);
//        recyclerView.setAdapter(productAdapter);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0) //check for scroll down
//                {
//                    visibleItemCount = gridLayoutManager.getChildCount();
//                    totalItemCount = gridLayoutManager.getItemCount();
//                    pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();
//
//                    if (!isLoading) {
//                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
//                            isLoading = true;
//                            getNextProducts();
//                        }
//                    }
//                }
//            }
//        });
    }

    private void init() {
        networkCalls = new NetworkCalls(getContext(), this);
        dialogBuilder = new DialogBuilder(getContext());
        productDTOList = new ArrayList<>();
    }

    private void getFirstProducts() {
        networkCalls.getProducts(20);
    }

    private void getNextProducts() {
        networkCalls.getNextProducts(20, lastCursor);
    }

    private void handleCallbackResponse(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response)) {
            GetProductsQuery.Data data = (GetProductsQuery.Data) response.data();
            if (Validations.isObjectNotEmptyAndNull(data)) {
                hasNextPage = data.products().pageInfo().hasNextPage();
                hasPreviousPage = data.products().pageInfo().hasPreviousPage();
                parseData(data);
                setDataInAdapter();
            }
        }
    }

    private void handleNextCallbackResponse(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response)) {
            GetNextProductsQuery.Data data = (GetNextProductsQuery.Data) response.data();
            if (Validations.isObjectNotEmptyAndNull(data)) {
                hasNextPage = data.products().pageInfo().hasNextPage();
                hasPreviousPage = data.products().pageInfo().hasPreviousPage();
                parseNextData(data);
                setDataInAdapter();
            }
        }
    }

    private void parseNextData(GetNextProductsQuery.Data data) {
        List<GetNextProductsQuery.Edge2> responseProducts = data.products().edges().get(0).node().collections().edges().get(0).node().products().edges();
        for (GetNextProductsQuery.Edge2 product : responseProducts) {
            List<String> imagesUrl = new ArrayList<>();
            for (GetNextProductsQuery.Edge4 productImages : product.node().images().edges()) {
                imagesUrl.add(productImages.node().originalSrc());
            }
            lastCursor = product.cursor();
            productDTOList.add(new ProductDTO(product.cursor(), "", "", product.node().variants().edges().get(0).node().id(),
                    product.node().title(), product.node().description(),
                    product.node().priceRange().minVariantPrice().amount(), product.node().priceRange().maxVariantPrice().amount(),
                    product.node().priceRange().maxVariantPrice().currencyCode().rawValue(), product.node().availableForSale(), imagesUrl));
        }
    }

    private void parseData(GetProductsQuery.Data data) {
        List<GetProductsQuery.Edge2> responseProducts = data.products().edges().get(0).node().collections().edges().get(0).node().products().edges();
        for (GetProductsQuery.Edge2 product : responseProducts) {
            List<String> imagesUrl = new ArrayList<>();
            for (GetProductsQuery.Edge4 productImages : product.node().images().edges()) {
                imagesUrl.add(productImages.node().originalSrc());
            }
            lastCursor = product.cursor();
            productDTOList.add(new ProductDTO(product.cursor(), "", "", product.node().title(), product.node().variants().edges().get(0).node().id(),
                    product.node().description(),
                    product.node().priceRange().minVariantPrice().amount(), product.node().priceRange().maxVariantPrice().amount(),
                    product.node().priceRange().maxVariantPrice().currencyCode().rawValue(), product.node().availableForSale(), imagesUrl));
        }
    }

    private void setDataInAdapter() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                productAdapter.updateProductsList(productDTOList);
                productAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        });
    }

    private void showLoadingDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.loadingDialog("Loading...");
            }
        });
    }

    private void hideLoadingDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogBuilder.dismissDialog();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv1:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(28);
                break;
            case R.id.tv2:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(3);
                break;
            case R.id.tv3:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(20);
                break;
            case R.id.tv4:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(21);
                break;
            case R.id.tv5:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(22);
                break;
            case R.id.tv6:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(23);
                break;
            case R.id.tv7:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(24);
                break;
            case R.id.tv8:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(25);
                break;
            case R.id.tv9:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(26);
                break;
            case R.id.tv10:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(27);
                break;
            case R.id.weekly_img:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(28);
                break;
            case R.id.about_img:
                ((MainActivity) Objects.requireNonNull(getActivity())).launchFragment(5);
                break;

        }
    }
}
