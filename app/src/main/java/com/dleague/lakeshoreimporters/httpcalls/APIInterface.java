package com.dleague.lakeshoreimporters.httpcalls;

import com.dleague.lakeshoreimporters.dtos.responsedto.CustomerDTO;
import com.dleague.lakeshoreimporters.dtos.responsedto.UpdatePasswordResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {
    @GET("/search.json?")
    Call<Object> getCuctomer(@Query("query") String email);

    @POST("250/{customer_id}/update_password?8be4cd7329747d6c206eda522e05e6a6")
    Call<UpdatePasswordResponse> updatePassword(@Path("customer_id") String customer_id, @Body Object object);

}