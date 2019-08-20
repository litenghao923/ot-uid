package com.moma.otasset.config.ot;

import com.alibaba.fastjson.JSONObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AssetApi {

    @POST("/createSubuserTransfer")
    @Headers({"Content-Type: application/json"})
    Call<String> createSubuserTransfer(@Body JSONObject jsonData);


}
