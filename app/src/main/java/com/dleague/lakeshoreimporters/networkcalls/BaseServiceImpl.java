package com.dleague.lakeshoreimporters.networkcalls;

import android.annotation.SuppressLint;
import android.content.Context;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.response.CustomTypeAdapter;
import com.apollographql.apollo.response.CustomTypeValue;
import com.dleague.lakeshoreimporters.type.CustomType;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.dleague.lakeshoreimporters.utils.Constants.SHOP_URL;

public class BaseServiceImpl {
    private Context context;
    private Query query;
    private Mutation mutation;
    private ApolloClient apolloClient;
    private OkHttpClient okHttpClient;
    private NetworkCallbacks networkCallbacks;
    private int responseCode;
    private boolean isMutationCall;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public BaseServiceImpl(Context context) {
        this.context = context;
        okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder builder = original.newBuilder().method(original.method(), original.body());
                    builder.header("User-Agent", "Android Apollo Client");
                    builder.header("X-Shopify-Storefront-Access-Token", "2d9d8dbc9c047259229be881bc991271");
                    return chain.proceed(builder.build());
                })
                .build();


        CustomTypeAdapter<Date> dateCustomTypeAdapter = new CustomTypeAdapter<Date>() {

            public Date decode(CustomTypeValue value) {
                try {
                    return DATE_FORMAT.parse(value.value.toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            public CustomTypeValue encode(Date value) {
                return new CustomTypeValue.GraphQLString(DATE_FORMAT.format(value));
            }
        };

        apolloClient = ApolloClient.builder()
                .serverUrl("https://" + SHOP_URL + "/api/graphql")
                .okHttpClient(okHttpClient)
                .addCustomTypeAdapter(CustomType.DATETIME, dateCustomTypeAdapter)
                .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
                .build();
    }

    //////////////// PROTECTED METHODS ///////////////////////

    protected void baseServiceQueryCall(Query query, NetworkCallbacks networkCallbacks, int responseCode) {
        isMutationCall = false;
        this.query = query;
        this.networkCallbacks = networkCallbacks;
        this.responseCode = responseCode;
        initiateCall();
    }

    protected void baseServiceMutationCall(Mutation mutation, NetworkCallbacks networkCallbacks, int responseCode) {
        isMutationCall = true;
        this.mutation = mutation;
        this.networkCallbacks = networkCallbacks;
        this.responseCode = responseCode;
        initiateCall();
    }

    /////////////////////////////////////////////////////////

    //////////////// PRIVATE METHODS ///////////////////////

    private void initiateCall() {
        networkCallbacks.onPreServiceCall();
        if (isMutationCall) {
            makeMutationCall();
        } else {
            makeQueryCall();
        }
    }

    private void makeQueryCall() {
        apolloClient.query(query).enqueue(new ApolloCall.Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                networkCallbacks.onSuccess(responseCode, response);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                networkCallbacks.onFailure(responseCode, e);
            }
        });
    }

    private void makeMutationCall() {

        apolloClient.mutate(mutation).enqueue(new ApolloCall.Callback() {
            @Override
            public void onResponse(@NotNull Response response) {
                networkCallbacks.onSuccess(responseCode, response);
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                networkCallbacks.onFailure(responseCode, e);
            }
        });
    }


    ///////////////////////////////////////////////////////
}