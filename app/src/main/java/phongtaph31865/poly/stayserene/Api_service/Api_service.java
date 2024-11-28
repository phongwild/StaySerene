package phongtaph31865.poly.stayserene.Api_service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import phongtaph31865.poly.stayserene.Model.Account;
import phongtaph31865.poly.stayserene.Model.ChangePassRequest;
import phongtaph31865.poly.stayserene.Model.Hotel;
import phongtaph31865.poly.stayserene.Model.Messenger;
import phongtaph31865.poly.stayserene.Model.Order_Room;
import phongtaph31865.poly.stayserene.Model.PhanHoi;
import phongtaph31865.poly.stayserene.Model.RecoveyPassRequest;
import phongtaph31865.poly.stayserene.Model.Room;
import phongtaph31865.poly.stayserene.Model.Service;
import phongtaph31865.poly.stayserene.Model.TypeRoom;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api_service {
    //Login http://localhost:3000/api/login
    //Register http://localhost:3000/api/register
    //Xem tk:  http://localhost:3000/api/account
    //Xem + thêm phòng: http://localhost:3000/api/rooms
    //Thêm + Xem Loại Phòng: http://localhost:3000/api/typeroom
    //Đặt phòng: http://localhost:3000/api/orderroom
    //Xem phòng đặt theo Uid: http://localhost:3000/api/orderroom/{Uid}
    //Thêm + xem khách sạn: http://localhost:3000/api/hotel
    //String BASE_URL = "http://192.168.10.103:3000/api/";
    String BASE_URL = "http://192.168.10.103:3000/api/";
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
    @GET("account/{id}")
    Call<List<Account>> get_account_byId(@Path("id") String id);
    @PUT("account/{id}")
    Call<List<Account>> update_account(@Path("id") String id, @Body Account ObjAccount);
    @GET("checkusergoogle")
    Call<Boolean> check_user_google(@Query("id") String id);
    @POST("change-pass")
    Call<ChangePassRequest> change_password(@Body ChangePassRequest ObjChangePassRequest);
    @POST("recovery-pass")
    Call<RecoveyPassRequest> recovery_password(@Body RecoveyPassRequest ObjRecoveyPassRequest);
    //Room
    @GET("rooms")
    Call<List<Room>> get_rooms();
    @GET("rooms/{id}")
    Call<List<Room>> get_rooms_byId(@Path("id") String id);
    @GET("roombytyperoom/{id}")
    Call<List<Room>> get_rooms_byId_typeRoom(@Path("id") String id);
    @PUT("rooms/{id}")
    Call<List<Room>> update_rooms(@Path("id") String id, @Body Room ObjRoom);
    //TypeRoom
    @GET("typeroombyidhotel/{id}")
    Call<List<TypeRoom>> get_typeroom_byId_hotel(@Path("id") String id);
    @GET("typeroom")
    Call<List<TypeRoom>> get_typeroom();
    @GET("typeroom/{id}")
    Call<List<TypeRoom>> get_typeroom_byId(@Path("id") String id);
    //Hotel
    @GET("hotel/{id}")
    Call<List<Hotel>> get_hotel_byId(@Path("id") String id);
    @GET("hotel")
    Call<List<Hotel>> get_hotel();
    //OrderRoom
    @POST("orderroom")
    Call<List<Room>> order_room(@Body Order_Room ObjOrder_Room);
    @GET("orderroom/status/01/{id}")
    Call<List<Order_Room>> get_orderroom_status01(@Path("id") String id);
    @GET("orderroom/status/2/{id}")
    Call<List<Order_Room>> get_orderroom_status2(@Path("id") String id);
    @GET("orderroom/status/3/{id}")
    Call<List<Order_Room>> get_orderroom_status3(@Path("id") String id);
    @PUT("orderroom/{id}")
    Call<List<Order_Room>> update_orderroom(@Path("id") String id, @Body Order_Room ObjOrder_Room);
    //Phan hoi
    @POST("phanhoiuser")
    Call <List<PhanHoi>> phan_hoi(@Body PhanHoi ObjPhanHoi);
    //messenger
    @GET("messenger/{hotelId}/{Uid}")
    Call<List<Messenger>> getMessengersForHotel(@Path("hotelId") String IdKhachSan, @Path("Uid") String Uid);
    @POST("messenger")
    Call<Messenger> createMessenger(@Body Messenger messenger);
    @POST("messenger")
    Call<Void> sendMessage(@Body Messenger messenger);
    //service
    @GET("dichvu")
    Call<List<Service>> get_service();
}