package com.moma.otasset.config.ot;

import com.alibaba.fastjson.JSONObject;
import com.moma.otasset.web.WebResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AssetApi {

    @POST("app/open/checkPassword")
    @Headers({"Content-Type: application/json"})
    Call<WebResult> createSubuserTransfer(@Body JSONObject jsonData);


}
