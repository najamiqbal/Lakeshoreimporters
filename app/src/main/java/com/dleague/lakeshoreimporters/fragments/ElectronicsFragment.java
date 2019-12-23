package com.dleague.lakeshoreimporters.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.dleague.lakeshoreimporters.GetCollectionsByIdNextQuery;
import com.dleague.lakeshoreimporters.GetCollectionsByIdQuery;
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

import static com.dleague.lakeshoreimporters.utils.Constants.CURRENT_BUY;
import static com.dleague.lakeshoreimporters.utils.Constants.ELECTRONICS_BUY;
import static com.dleague.lakeshoreimporters.utils.Constants.GET_COLLECTION_BY_ID;
import static com.dleague.lakeshoreimporters.utils.Constants.GET_COLLECTION_BY_ID_NEXT;
import static com.dleague.lakeshoreimporters.utils.Constants.LOG_TAG;

public class ElectronicsFragment extends Fragment implements NetworkCallbacks, ItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerview_buy_now)
    RecyclerView recyclerView;

    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NetworkCalls networkCalls;
    private DialogBuilder dialogBuilder;
    private ProductAdapter productAdapter;
    private List<ProductDTO> productDTOList;
    private String lastCursor;
    private int visibleItemCount, pastVisibleItems, totalItemCount;
    private GridLayoutManager gridLayoutManager;
    private boolean hasNextPage, hasPreviousPage, isLoading;
    EditText simpleSearchView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_electronics, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        init();
        getFirstProducts();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

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
        if (responseCode == GET_COLLECTION_BY_ID) {
            handleCallbackResponse(response);
        } else if (responseCode == GET_COLLECTION_BY_ID_NEXT) {
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
        gridLayoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        productDTOList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productDTOList, this);
        recyclerView.setAdapter(productAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisibleItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (!isLoading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            isLoading = true;
                            getNextProducts();
                        }
                    }
                }
            }
        });
    }

    private void init() {
        networkCalls = new NetworkCalls(getContext(), this);
        dialogBuilder = new DialogBuilder(getContext());
        productDTOList = new ArrayList<>();
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(this);
        simpleSearchView =  rootView.findViewById(R.id.simpleSearchView);
        simpleSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
   /*     swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                    }
                                }
        );*/
    }
    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<ProductDTO> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (ProductDTO s : productDTOList) {
            //if the existing elements contains the search input
            if (s.getProductName().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        productAdapter.filterList(filterdNames);
    }

    private void getFirstProducts() {
        networkCalls.getFirstCollectionsById(ELECTRONICS_BUY, 20);
    }

    private void getNextProducts() {
        if (hasNextPage) {
            networkCalls.getNextCollectionsById(ELECTRONICS_BUY, 20, lastCursor);
        }
    }

    private void handleCallbackResponse(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response)) {
            GetCollectionsByIdQuery.Data data = (GetCollectionsByIdQuery.Data) response.data();
            if (Validations.isObjectNotEmptyAndNull(data)) {
                hasNextPage = data.collectionByHandle().products().pageInfo().hasNextPage();
                hasPreviousPage = data.collectionByHandle().products().pageInfo().hasPreviousPage();
                parseData(data);
                setDataInAdapter();
            }
        }
    }

    private void handleNextCallbackResponse(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response)) {
            GetCollectionsByIdNextQuery.Data data = (GetCollectionsByIdNextQuery.Data) response.data();
            if (Validations.isObjectNotEmptyAndNull(data)) {
                hasNextPage = data.collectionByHandle().products().pageInfo().hasNextPage();
                hasPreviousPage = data.collectionByHandle().products().pageInfo().hasPreviousPage();
                parseNextData(data);
                setDataInAdapter();
            }
        }
    }

    private void parseNextData(GetCollectionsByIdNextQuery.Data data) {
        List<GetCollectionsByIdNextQuery.Edge> responseProducts = data.collectionByHandle().products().edges();
        for (GetCollectionsByIdNextQuery.Edge product : responseProducts) {
            List<String> imagesUrl = new ArrayList<>();
            for (GetCollectionsByIdNextQuery.Edge2 productImages : product.node().images().edges()) {
                imagesUrl.add(productImages.node().originalSrc());
            }
            lastCursor = product.cursor();
            productDTOList.add(new ProductDTO(product.cursor(), product.node().handle(), product.node().id(), product.node().variants().edges().get(0).node().id(),
                    product.node().title(), product.node().descriptionHtml(),
                    product.node().priceRange().minVariantPrice().amount(), product.node().priceRange().maxVariantPrice().amount(),
                    product.node().priceRange().maxVariantPrice().currencyCode().rawValue(), product.node().availableForSale(), imagesUrl));
        }
    }

    private void parseData(GetCollectionsByIdQuery.Data data) {
        List<GetCollectionsByIdQuery.Edge> responseProducts = data.collectionByHandle().products().edges();
        for (GetCollectionsByIdQuery.Edge product : responseProducts) {
            List<String> imagesUrl = new ArrayList<>();
            for (GetCollectionsByIdQuery.Edge2 productImages : product.node().images().edges()) {
                imagesUrl.add(productImages.node().originalSrc());
            }
            lastCursor = product.cursor();
            productDTOList.add(new ProductDTO(product.cursor(), product.node().handle(), product.node().id(), product.node().variants().edges().get(0).node().id(),
                    product.node().title(), product.node().descriptionHtml(),
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
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 5000);
    }
}
