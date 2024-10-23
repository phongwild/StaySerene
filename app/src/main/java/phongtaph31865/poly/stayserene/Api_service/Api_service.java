package phongtaph31865.poly.stayserene.Api_service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.Model.TypeRoom;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api_service {
    //Login http://localhost:3000/api/login
    //Register http://localhost:3000/api/register
    //Xem tk:  http://localhost:3000/api/account
    //Xem + thêm phòng: http://localhost:3000/api/rooms
    //Thêm + Xem Loại Phòng: http://localhost:3000/api/typeroom
    //Đặt phòng: http://localhost:3000/api/orderroom
    //Xem phòng đặt theo Uid: http://localhost:3000/api/orderroom/{Uid}
    //Thêm + xem khách sạn: http://localhost:3000/api/hotel
    String BASE_URL = "http://192.168.10.103:3000/api/";
    //String BASE_URL = "http://10.0.2.2:3000/api/";
    Gson gson = new GsonBuilder().create();
    Api_service service = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(Api_service.class);

    //Account
    @POST("register")
    Call<List<Account>> create_account(@Body Account ObjAccount);
    @POST("login")
    Call<List<Account>> login(@Body Account ObjAccount);
    @GET("account")
    Call<List<Account>> get_account();
    @GET("rooms")
    Call<List<Room>> get_rooms();
    @GET("typeroom")
    Call<List<TypeRoom>> get_typeroom();
}
