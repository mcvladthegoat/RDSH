package com.example.rdshv40;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServerApi {

    @FormUrlEncoded
    //@POST("https://rdshtest.herokuapp.com/user/login")
    @POST("user/login")
    public Call <UserModel> authorization(@Field("login") String login, @Field("password") String password);

    //role -> teacher, parent, student
    //directionLeader, activeLeader
    //schoolAdmin
    @FormUrlEncoded
    @POST ("user/registration")
    public Call <UserModel> registration(@Field("lastName") String lastName,
                                         @Field("name") String name, @Field("email") String email,
                                         @Field("password") String password, @Field("role") String role);

    @FormUrlEncoded
    @POST ("user/updatetoken")
    public Call <ResponseBody> updateToken(@Field("uid") Long userId, @Field("token") String token);

    @FormUrlEncoded
    @POST ("event/checkuserrole")
    public Call <ResponseBody> checkUserRole (@Field("userId") Long userId, @Field("eventId") Long eventId);

    @GET("event/getactive")
    public Call <List<EventModel>> getActiveEvent();

    @FormUrlEncoded
    @POST("event/getinfo")
    public Call <EventModel> getEventInfo(@Field("userId") Long userId, @Field("eventId") Long eventId);

    @FormUrlEncoded
    @POST("event/add")
    public Call <EventModel> addEvent(@Field("userId") Long userId, @Field("eventName") String eventName, @Field("eventDate") String eventDate,
                  @Field("eventContent") String eventContent, @Field("eventDirection") ArrayList<Long> eventDirection);

    @GET("direction/getdirections")
    public Call <List<DirectionModel>> getDirections();

    @FormUrlEncoded
    @POST("direction/getinfo")
    public Call<DirectionModel> getDirectionInfo(@Field("directionId") Long directionId);

    @FormUrlEncoded
    @POST("task/getinevent")
    public Call<List<TaskModel>> getTaskInEvent(@Field("eventId") Long eventId);

    @FormUrlEncoded
    @POST("event/addtoevent")
    public Call<ResponseBody> addToEvent(@Field("eventId") Long eventId, @Field("userId") Long userId, @Field("role") String role);

    @FormUrlEncoded
    @POST("event/getactiveuserevent")
    public Call <List<EventModel>> getActiveUserEvent(@Field("userId") Long userId);
}
