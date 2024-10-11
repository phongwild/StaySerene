package phongtaph31865.poly.stayserene.Api_service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import phongtaph31865.poly.stayserene.Model.Account;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api_service {
    //String BASE_URL = "http://192.168.10.103:3000/api/";
    String BASE_URL = "http://10.0.2.2:3000/api/";
    Gson gson = new GsonBuilder().create();
    Api_service service = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(Api_service.class);

    //Account
    @GET("account")
    Call<List<Account>> get_ds_account();
    @POST("register")
    Call<List<Account>> create_account(@Body Account ObjAccount);
}
