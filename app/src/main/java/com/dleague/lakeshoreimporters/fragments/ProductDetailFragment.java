package com.dleague.lakeshoreimporters.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bumptech.glide.Glide;
import com.dleague.lakeshoreimporters.GetProductByHandleQuery;
import com.dleague.lakeshoreimporters.R;
import com.dleague.lakeshoreimporters.activities.AppSpace;
import com.dleague.lakeshoreimporters.activities.MainActivity;
import com.dleague.lakeshoreimporters.dtos.CartDTO;
import com.dleague.lakeshoreimporters.dtos.ProductDTO;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCallbacks;
import com.dleague.lakeshoreimporters.networkcalls.NetworkCalls;
import com.dleague.lakeshoreimporters.utils.DialogBuilder;
import com.dleague.lakeshoreimporters.utils.HelperMethods;
import com.dleague.lakeshoreimporters.utils.MessageUtil;
import com.dleague.lakeshoreimporters.utils.Validations;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.animations.DescriptionAnimation;
import com.glide.slider.library.slidertypes.BaseSliderView;
import com.glide.slider.library.slidertypes.DefaultSliderView;
import com.glide.slider.library.slidertypes.TextSliderView;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zolad.zoominimageview.ZoomInImageViewAttacher;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dleague.lakeshoreimporters.activities.AppSpace.cartItemCount;
import static com.dleague.lakeshoreimporters.utils.Constants.GET_PRODUCT_BY_HANDLE;
import static com.dleague.lakeshoreimporters.utils.Constants.LOG_TAG;

public class ProductDetailFragment extends Fragment implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener, NetworkCallbacks {

    @BindView(R.id.product_images_slider)
    SliderLayout slider;
    @BindView(R.id.tv_pd_name)
    TextView tvProductName;
    @BindView(R.id.tv_pd_price)
    TextView tvProductPrice;
    RelativeLayout ok;
    //    @BindView(R.id.tv_pd_description)
//    HtmlTextView tvProductDescription;
    @BindView(R.id.tv_pd_tellme_more)
    TextView tvTemmMeMore;
    @BindView(R.id.iv_pd_count_plus)
    ImageView ivAddCount;
    @BindView(R.id.iv_pd_count_subtract)
    ImageView ivSubtractCount;
    @BindView(R.id.tv_pd_count_nuber)
    TextView tvQuantity;
    @BindView(R.id.tv_pd_add_to_cart)
    TextView tvAddToCart;
    @BindView(R.id.viewflipper_availability)
    ViewFlipper viewFlipper;
    @BindView(R.id.tv_pd_selected_title_1)
    TextView tvSpinnerTitle1;
    @BindView(R.id.tv_pd_selected_title_2)
    TextView tvSpinnerTitle2;
    @BindView(R.id.spinner_pd_1)
    AppCompatSpinner spinner1;
    @BindView(R.id.spinner_pd_2)
    AppCompatSpinner spinner2;
    @BindView(R.id.layout_spinner_1)
    LinearLayout layoutSpinner1;
    @BindView(R.id.layout_spinner_2)
    LinearLayout layoutSpinner2;
    private View rootView;
    private ProductDTO productDTO;
    private int quantity;
    private boolean isViewFlip;
    private NetworkCalls networkCalls;
    private DialogBuilder dialogBuilder;
    private List<String> variantListOne;
    private List<String> variantListTwo;
    private Map<String, String> varintMapOne;
    private Map<String, String> varintMapTwo;
    private Map<String, String> varintIdsByValue;
    private String variantNameOne, variantNameTwo;
    private Map<String, String> varintIdsByImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_product_detail, container, false);
        ButterKnife.bind(this, rootView);
        getExtras();
        init();
        getProductByHandle();

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String price = varintMapOne.get(variantListOne.get(position));
                tvProductPrice.setText(price);
                productDTO.setProductPriceMin(price);
                productDTO.setProductPriceMax(price);
                productDTO.setVariantsId(varintIdsByValue.get(variantListOne.get(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String price = varintMapTwo.get(variantListTwo.get(position));
                tvProductPrice.setText(price);
                productDTO.setProductPriceMin(price);
                productDTO.setProductPriceMax(price);
                productDTO.setVariantsId(varintIdsByValue.get(variantListTwo.get(position)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    public void _setImageSlider() {
        if (productDTO.isAvailableForSale()) {
            isViewFlip = false;
            viewFlipper.setDisplayedChild(0);
        } else {
            isViewFlip = true;
            viewFlipper.setDisplayedChild(1);
        }
        DefaultSliderView sliderView;
        for (String str : productDTO.getImagesUrl()) {
            sliderView = new DefaultSliderView(getContext());
            sliderView.image(str);
            slider.addSlider(sliderView);

            sliderView.setOnSliderClickListener(this);
            // initialize SliderLayout
        }
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);
        slider.addOnPageChangeListener(this);
        slider.stopCyclingWhenTouch(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.tv_pd_add_to_cart)
    void setTvAddToCart() {
        String imageURL = productDTO.getImagesUrl().get(0);
        if (Validations.isObjectNotEmptyAndNull(varintIdsByImage)) {
            imageURL = varintIdsByImage.get(productDTO.getVariantsId());
        }
        CartDTO cartDTO = new CartDTO(productDTO.getProductId(), productDTO.getVariantsId(), productDTO.getCursor(),
                productDTO.getProductName(), productDTO.getDescription(),
                productDTO.getProductPriceMin(), productDTO.getProductPriceMax(), imageURL,
                productDTO.getCurrencyCode(), quantity);
        AppSpace.localDbOperations.storeCartItem(cartDTO);
        quantity = 1;
        cartItemCount++;
        ((MainActivity) Objects.requireNonNull(getActivity())).updateCart();
        updateQuantity();
        MessageUtil.showSnackbarMessage(rootView, "Product added to cart successfully.");
    }

    @OnClick(R.id.iv_pd_count_plus)
    void setAddItemCount() {
        if (!isViewFlip) {
            quantity++;
            updateQuantity();
        }
    }

    @OnClick(R.id.iv_pd_count_subtract)
    void setSubItemCount() {
        if (!isViewFlip) {
            if (quantity == 1) {

            } else {
                quantity--;
                updateQuantity();
            }
        }
    }

/*    @OnClick(R.id.iv_pd_back)
    void setIvBack() {
        ((MainActivity) Objects.requireNonNull(getActivity())).onBackPressed();
    }*/

    private void getExtras() {
        Bundle arguments = getArguments();
        productDTO = (ProductDTO) arguments.getSerializable("productToView");
        if (!Validations.isObjectNotEmptyAndNull(productDTO)) {
            MessageUtil.showToastMessage(getContext(), "ISSUE SHOWING PRODUCT");
        }
    }

    private void init() {
        quantity = 1;
        dialogBuilder = new DialogBuilder(getContext());
        networkCalls = new NetworkCalls(getContext(), this);
        variantListOne = new ArrayList<>();
        variantListTwo = new ArrayList<>();
        varintMapOne = new HashMap<>();
        varintMapTwo = new HashMap<>();
        varintIdsByValue = new HashMap<>();
        varintIdsByImage = new HashMap<>();

    }

    private void getProductByHandle() {
        networkCalls.getProductByHandle(productDTO.getHandle());
    }


    private void updateQuantity() {
        tvQuantity.setText(String.valueOf(quantity));
    }

    @Override
    public void onPreServiceCall() {
        showLoadingDialog();
    }

    @Override
    public void onSuccess(int responseCode, Response response) {
        hideLoadingDialog();
        if (responseCode == GET_PRODUCT_BY_HANDLE) {
            handleCallbackResponse(response);
        }
    }

    @Override
    public void onFailure(int responseCode, ApolloException exception) {
        hideLoadingDialog();
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


    private void handleCallbackResponse(Response response) {
        if (Validations.isObjectNotEmptyAndNull(response)) {
            GetProductByHandleQuery.Data data = (GetProductByHandleQuery.Data) response.data();
            if (Validations.isObjectNotEmptyAndNull(data)) {
                parseData(data.productByHandle());
            }
        }
    }

    private void parseData(GetProductByHandleQuery.ProductByHandle productByHandle) {
        List<SelectedOptions> selectedOptionsList = new ArrayList<>();
        if (Validations.isObjectNotEmptyAndNull(productByHandle)) {
            ProductDTO obj = new ProductDTO();
            obj.setProductId(productByHandle.id());
            obj.setDescription(productByHandle.descriptionHtml());
            obj.setProductName(productByHandle.title());
            obj.setProductPriceMax(String.valueOf(productByHandle.priceRange().maxVariantPrice()));
            obj.setProductPriceMin(String.valueOf(productByHandle.priceRange().minVariantPrice()));
            if (Validations.isObjectNotEmptyAndNull(productByHandle.variants())) {
                for (GetProductByHandleQuery.Edge1 edge : productByHandle.variants().edges()) {
                    varintIdsByImage.put(edge.node().id(), edge.node().image().originalSrc());
                    if (Validations.isObjectNotEmptyAndNull(edge.node().selectedOptions())) {
                        for (GetProductByHandleQuery.SelectedOption temp : edge.node().selectedOptions()) {
                            selectedOptionsList.add(new SelectedOptions(temp.name(), temp.value(), edge.node().price(), edge.node().id()));
                        }
                    }
                }
            } else {

            }
            if (!selectedOptionsList.isEmpty()) {
                sortSelectedOptions(selectedOptionsList);
            }

            setDataInUI();

        } else {

        }
    }

    private void sortSelectedOptions(List<SelectedOptions> selectedOptionsList) {
        String name1 = "";
        String name2 = "";
        for (SelectedOptions option : selectedOptionsList) {
            if (name1.isEmpty()) {
                name1 = option.name;
            } else {
                if (name1.equals(option.name)) {
                    continue;
                } else if (name2.isEmpty()) {
                    name2 = option.name;
                } else if (name2.equals(option.name)) {
                    continue;
                }
            }
        }

        if (!name1.isEmpty()) {
            for (SelectedOptions options : selectedOptionsList) {
                if (name1.equals(options.name)) {
                    variantListOne.add(options.value);
                    varintMapOne.put(options.value, options.price);
                    varintIdsByValue.put(options.value, options.variantId);
                }
            }
        }

        if (!name2.isEmpty()) {
            for (SelectedOptions options : selectedOptionsList) {
                if (name2.equals(options.name)) {
                    variantListTwo.add(options.value);
                    varintMapTwo.put(options.value, options.price);
                    varintIdsByValue.put(options.value, options.variantId);
                }
            }
        }

        if (!variantListOne.isEmpty()) {
            variantListOne = removeDuplicateValues(variantListOne);
        }

        if (!variantListTwo.isEmpty()) {
            variantListTwo = removeDuplicateValues(variantListTwo);
        }
        variantNameOne = name1;
        variantNameTwo = name2;
    }

    private List<String> removeDuplicateValues(List<String> list) {
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(list);
        list.clear();
        list.addAll(hashSet);
        return list;
    }

    private void setDataInUI() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String displayPrice;

                if (productDTO.isAvailableForSale()) {
                    isViewFlip = false;
                    viewFlipper.setDisplayedChild(0);
                } else {
                    isViewFlip = true;
                    viewFlipper.setDisplayedChild(1);
                }
                _setImageSlider();
//                DefaultSliderView sliderView;
//                for (String str : productDTO.getImagesUrl()) {
//                    sliderView = new DefaultSliderView(getContext());
//                    sliderView.image(str);
//                    slider.addSlider(sliderView);
//                }
//                slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//                slider.setPresetTransformer("ZoomIn");
//
//                slider.setCustomAnimation(new DescriptionAnimation());
//                slider.setDuration(4000);
//                //slider.addOnPageChangeListener(this);
//                slider.stopCyclingWhenTouch(false);

                tvProductName.setText(productDTO.getProductName());
                if (productDTO.getProductPriceMax().equals(productDTO.getProductPriceMin())) {
                    displayPrice = "$" + productDTO.getProductPriceMax();
                } else {
                    displayPrice = "From $" + productDTO.getProductPriceMin();
                }
                tvProductPrice.setText(displayPrice);
//                tvProductDescription.setHtml(productDTO.getDescription(),
//                        new HtmlHttpImageGetter(tvProductDescription));
                tvTemmMeMore.setText(Html.fromHtml(productDTO.getDescription(), new ImageGetter(), null));
                setProductDetail();
                if (!variantNameOne.isEmpty() && !variantListOne.isEmpty()) {
                    tvSpinnerTitle1.setText(variantNameOne);
                    spinner1.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, variantListOne));
                    layoutSpinner1.setVisibility(View.VISIBLE);
                }

                if (!variantNameTwo.isEmpty() && !variantListTwo.isEmpty()) {
                    tvSpinnerTitle2.setText(variantNameTwo);
                    spinner2.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, variantListTwo));
                    layoutSpinner2.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {


        Log.i("Testing here", slider.getUrl());
        showPopup(slider.getUrl());
        //Toast.makeText(getContext(), slider.getUrl(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
            int id;
            if (source.equals("https://cdn.shopify.com/s/files/1/1789/3041/files/icone-calendario_844e86c5-abb6-4be6-a501-c6098a64a0c0_large.png?v=1546731922")) {
                id = R.drawable.ic_tell_me_more;
            } else if (source.equals("https://cdn.shopify.com/s/files/1/1789/3041/files/icone-calendario_b9af8aca-4c70-4c59-a5ac-63edde98d9f7_large.png?v=1546731776")) {
                id = R.drawable.ic_estimated_delivery;
            } else if (source.equals("https://cdn.shopify.com/s/files/1/1789/3041/files/icone-calendario_11ed603c-b8cb-4419-89c8-fb3eaf77d8d9_large.png?v=1546732038")) {
                id = R.drawable.ic_client_faq;
            } else if (source.equals("https://cdn.shopify.com/s/files/1/1789/3041/files/icone-calendario_bd8db3ba-e721-4b16-ae1f-21adc16e4e61_large.png?v=1546731976")) {
                id = R.drawable.ic_warranty_period;
            } else {
                return null;
            }

            Drawable d = getResources().getDrawable(id);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());

            return d;
        }
    }

    ;

    private void setProductDetail() {
        List<String> description = new ArrayList<>();
        description = HelperMethods.filterDescription(productDTO.getDescription());
        Log.i(LOG_TAG, description.toString());
    }

    private class SelectedOptions {
        String name;
        String value;
        String price;
        String variantId;

        public SelectedOptions() {
        }

        public SelectedOptions(String name, String value, String price, String variantId) {
            this.name = name;
            this.value = value;
            this.price = price;
            this.variantId = variantId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getVariantId() {
            return variantId;
        }

        public void setVariantId(String variantId) {
            this.variantId = variantId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public void showPopup(String url) {

        final View popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_rest_window, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        final ImageButton skipbtn = (ImageButton) popupView.findViewById(R.id.cross_popup);
        final ImageView popup_image=(ImageView) popupView.findViewById(R.id.popu_image);
        if (url != null) {
            Glide.with(getContext()).load(url).dontAnimate().fitCenter().placeholder(R.drawable.logo).into(popup_image);
        }
        ZoomInImageViewAttacher mIvAttacter = new ZoomInImageViewAttacher();
        mIvAttacter.attachImageView(popup_image);
        //final ProgressBar mProgress = popupView.findViewById(R.id.circularProgressbar);


        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

    }

   /* public void showImage(String imageUri) {
        Dialog builder = new Dialog(getContext());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(getContext());
        //imageView.setImageURI(imageUri);
        if (imageUri != null) {
            Glide.with(getContext()).load(imageUri).dontAnimate().fitCenter().placeholder(R.drawable.logo).into(imageView);
        }
        //Picasso.get().load(imageUri).placeholder(R.drawable.logo).into(imageView);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }*/
}
