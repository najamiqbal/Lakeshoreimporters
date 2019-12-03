package com.dleague.lakeshoreimporters.httpcalls;

import android.content.Context;
import android.util.Log;

import com.dleague.lakeshoreimporters.activities.AppSpace;
import com.dleague.lakeshoreimporters.dtos.BinReqDTO;
import com.dleague.lakeshoreimporters.dtos.responsedto.CustomerDTO;
import com.dleague.lakeshoreimporters.dtos.responsedto.UpdatePasswordResponse;
import com.dleague.lakeshoreimporters.dtos.responsedto.apply_spent_rule_response.ApplySpentRulesDTO;
import com.dleague.lakeshoreimporters.dtos.responsedto.customer_credit_response.CustomerCreditDTO;
import com.dleague.lakeshoreimporters.dtos.responsedto.spent_rule_response.GetSpentRuleDTO;
import com.google.gson.Gson;


import org.apache.http.HttpHeaders;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;

import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.dleague.lakeshoreimporters.utils.Constants.APPLY_SPENT_RULE;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_EMAIL;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_ID;
import static com.dleague.lakeshoreimporters.utils.Constants.CUSTOMER_NAME;
import static com.dleague.lakeshoreimporters.utils.Constants.GET_SPENT_RULE;
import static com.dleague.lakeshoreimporters.utils.Constants.LOG_TAG;
import static com.dleague.lakeshoreimporters.utils.Constants.SEND_BIN_REQ;
import static com.dleague.lakeshoreimporters.utils.Constants.STORE_CREDITS;
import static com.dleague.lakeshoreimporters.utils.Constants.UPDATE_PASSWORD;

public class HttpNetworkCall {
    Context context;
    private String credentials;
    private HttpResponseCallbacks networkResponseCallbacks;

    public HttpNetworkCall(Context context, HttpResponseCallbacks networkResponseCallbacks) {
        this.context = context;
        this.credentials = Credentials.basic("0c286a505741a5e2495e243a649315c3", "ab98fa89b6e8cb917945884618c8f2a6");
        this.networkResponseCallbacks = networkResponseCallbacks;
    }

    public void updateCustomerPassword(String customerId, String password) {
        OkHttpClient client = new OkHttpClient();
        String baseUrl = "https://app.getflits.com/api/1/250/" + customerId + "/update_password?token=a88e3ae968399d2e2f8fa266cb8ba652";
        RequestBody body = new FormBody.Builder()
                .add("password", password)
                .add("password_confirmation", password)
                .build();
        Request request = new Request.Builder().url(baseUrl).post(body).build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            String data = response.body().string();
            Gson gson = new Gson();
            UpdatePasswordResponse object = gson.fromJson(data, UpdatePasswordResponse.class);
            networkResponseCallbacks.onSuccess(UPDATE_PASSWORD, object);
        } catch (IOException e) {
            networkResponseCallbacks.onFailure(UPDATE_PASSWORD, "Update Password Failed!");
            e.printStackTrace();
        }

    }

    public void getCustomerId(String email) {
        OkHttpClient client = new OkHttpClient();
        String baseUrl = "https://lakeshore-importers.myshopify.com/admin/customers/search.json?";
        HttpUrl.Builder httpBuider = HttpUrl.parse(baseUrl).newBuilder();
        httpBuider.addQueryParameter("query", "email:" + email);
        Request request = new Request.Builder().url(httpBuider.build()).addHeader(HttpHeaders.AUTHORIZATION, credentials).build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            String data = response.body().string();
            Gson gson = new Gson();
            CustomerDTO object = gson.fromJson(data, CustomerDTO.class);
            AppSpace.sharedPref.writeValue(CUSTOMER_ID, String.valueOf(object.customers.get(0).id));
            AppSpace.sharedPref.writeValue(CUSTOMER_EMAIL, String.valueOf(object.customers.get(0).email));
            AppSpace.sharedPref.writeValue(CUSTOMER_NAME, String.valueOf(object.customers.get(0).firstName + " " + object.customers.get(0).lastName));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getCustomerCredit(String customerId) {
        OkHttpClient client = new OkHttpClient();
        String baseUrl = "https://app.getflits.com/api/1/250/" + customerId + "/credit/get_credit?token=8be4cd7329747d6c206eda522e05e6a6";
        Request request = new Request.Builder().url(baseUrl).build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            String data = response.body().string();
            Gson gson = new Gson();
            CustomerCreditDTO object = gson.fromJson(data, CustomerCreditDTO.class);
            networkResponseCallbacks.onSuccess(STORE_CREDITS, object);
        } catch (IOException e) {
            networkResponseCallbacks.onFailure(STORE_CREDITS, "Getting Store Credits Failed!");
            e.printStackTrace();
        }
    }

    public void get_available_spent_rule(String customerId, String cartID) {
        OkHttpClient client = new OkHttpClient();
        String baseUrl = "https://app.getflits.com/api/1/250/" + customerId + "/credit/get_spent_rules";
        RequestBody body = new FormBody.Builder()
                .add("token", "8be4cd7329747d6c206eda522e05e6a6")
                .add("cart", cartID)
                .build();

        Request request = new Request.Builder().url(baseUrl).post(body).build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            String data = response.body().string();
            Gson gson = new Gson();
            GetSpentRuleDTO object = gson.fromJson(data, GetSpentRuleDTO.class);
            networkResponseCallbacks.onSuccess(GET_SPENT_RULE, object);
        } catch (IOException e) {
            networkResponseCallbacks.onFailure(GET_SPENT_RULE, "Failed getting spent rule");
            e.printStackTrace();
        }
    }

    public void applySpentRule(String customerId, String cartID, String spentRuleId, String cartData) {
        OkHttpClient client = new OkHttpClient();
        String baseUrl = "https://app.getflits.com/api/1/250/" + customerId + "/credit/apply_credit?token=8be4cd7329747d6c206eda522e05e6a6";
        RequestBody body = new FormBody.Builder()
                .add("data", cartData)
                .add("spent_rule_id", spentRuleId)
                .add("cart_token", cartID)
                .build();

        Request request = new Request.Builder().url(baseUrl).post(body).build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            String data = response.body().string();
            Gson gson = new Gson();
            ApplySpentRulesDTO object = gson.fromJson(data, ApplySpentRulesDTO.class);
            networkResponseCallbacks.onSuccess(APPLY_SPENT_RULE, object);
        } catch (IOException e) {
            networkResponseCallbacks.onFailure(APPLY_SPENT_RULE, "Failed getting spent rule");
            e.printStackTrace();
        }

    }

    public void sendBinRequest(BinReqDTO binReqDTO) {
        OkHttpClient client = new OkHttpClient();
        String baseUrl = "https://script.google.com/macros/s/AKfycbxp43AD2tWtYk0eD9-OMAzcBzqEJK00ldL4IPT5nKtQjOL3Gvmb/exec";
        RequestBody body = new FormBody.Builder()
                .add("action", binReqDTO.action)
                .add("firstName", binReqDTO.firstName)
                .add("lastName", binReqDTO.lastName)
                .add("email", binReqDTO.email)
                .add("location", binReqDTO.location)
                .add("description", binReqDTO.description)
                .add("pickDate", binReqDTO.pickDate)
                .add("sheet", binReqDTO.sheet)
                .add("agreement1", binReqDTO.agreement1)
                .add("agreement2", binReqDTO.agreement2)
                .add("agreement3", binReqDTO.agreement3)
                .add("agreement4", binReqDTO.agreement4)
                .build();

        Request request = new Request.Builder().url(baseUrl).post(body).build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            String data = response.body().string();
            networkResponseCallbacks.onSuccess(SEND_BIN_REQ, "");
        } catch (IOException e) {
            networkResponseCallbacks.onFailure(SEND_BIN_REQ, "Failed posting bin request");
            e.printStackTrace();
        }

    }

}