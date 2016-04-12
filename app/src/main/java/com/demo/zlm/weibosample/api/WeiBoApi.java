package com.demo.zlm.weibosample.api;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.demo.zlm.weibosample.model.Status;
import com.demo.zlm.weibosample.model.Timeline;
import com.demo.zlm.weibosample.model.User;
import com.demo.zlm.weibosample.model.Token;
import com.demo.zlm.weibosample.model.WeiBoUser;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by malinkang on 2016/4/10.
 */
public class WeiBoApi {

    private Retrofit mRetrofit;
    private Retrofit mRetrofit2;

    private WeiBoService mWeiBoService;
    private WeiBoService mWeiBoService2;

    public WeiBoApi() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.weibo.com") //设置baseurl
                .addConverterFactory(GsonConverterFactory.create()) //添加
                .client(client) // 配置Okhttp
                .build();

        mRetrofit2 = new Retrofit.Builder()
                .baseUrl("https://upload.api.weibo.com") //设置baseurl
                .addConverterFactory(GsonConverterFactory.create()) //添加
                .client(client) // 配置Okhttp
                .build();

        mWeiBoService = mRetrofit.create(WeiBoService.class);
        mWeiBoService2 = mRetrofit2.create(WeiBoService.class);
    }

    public interface WeiBoService {

        @FormUrlEncoded
        @POST("/oauth2/access_token")
        Call<Token> getToken(@Field("client_id") String client_id, @Field("client_secret") String client_secret,
                             @Field("grant_type") String grant_type, @Field("code") String code, @Field("redirect_uri") String redirect_uri);

        @GET("/2/users/show.json")
        Call<User> getUser(@Query("access_token") String access_token, @Query("uid") String uid);

        @GET("/2/statuses/friends_timeline.json")
        Call<Timeline> getFriendsTimeline(@Query("access_token") String access_token, @Query("since_id") String since_id, @Query("max_id") String max_id, @Query("count") String count, @Query("page") int page);

        @GET("/2/statuses/public_timeline.json")
        Call<Timeline> getPublicTimeline(@Query("access_token") String access_token, @Query("count") int count, @Query("page") int page);

        @GET("/2/statuses/user_timeline.json")
        Call<Timeline> getUserTimeline(@Query("access_token") String access_token, @Query("uid") String uid, @Query("page") int page);


        @Multipart
        @POST("/2/statuses/upload.json")
        Call<Status> upload(@Part("access_token") RequestBody access_token, @Part("status") RequestBody status, @Part MultipartBody.Part pic);

    }

    /**
     * 获取token
     *
     * @param client_id
     * @param client_secret
     * @param grant_type
     * @param code
     * @param redirect_uri
     */
    public void getToken(String client_id, String client_secret, String grant_type, String code, String redirect_uri) {
        Call<Token> call = mWeiBoService.getToken(client_id, client_secret, grant_type, code, redirect_uri);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Token token = response.body();
                System.out.println(token.toString());
                new Delete().from(Token.class).execute();
                token.save();
                getUser(token.getToken(), token.getUid());
                getFriendsTimeline(token.getToken(), "0", "0", "20", 1, true);
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                //
                Log.e("WeiBoApi", "Error" + t.getMessage());
            }
        });
    }

    /**
     * 获取登录用户信息
     *
     * @param access_token
     * @param uid
     */
    public void getUser(String access_token, String uid) {
        Call<User> call = mWeiBoService.getUser(access_token, uid);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                new Delete().from(User.class).execute();
                user.save();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    //请求用户关注人发送的微博
    public void getFriendsTimeline(String access_token, String since_id, String max_id, String count, int page, final boolean frag) {
        Call<Timeline> call = mWeiBoService.getFriendsTimeline(access_token, since_id, max_id, count, page);
        getTimeline(call, frag);
    }

    //删除 存储等
    public void getTimeline(Call<Timeline> call, final boolean frag) {
        call.enqueue(new Callback<Timeline>() {
            @Override
            public void onResponse(Call<Timeline> call, Response<Timeline> response) {
                Timeline timeline = response.body();
                List<Status> statuses = timeline.getStatuses();
                if (frag) {
                    new Delete().from(Status.class).execute();
                    new Delete().from(WeiBoUser.class).execute();
                }
                ActiveAndroid.beginTransaction();

                try {
                    for (Status status : statuses) {
                        WeiBoUser user = new WeiBoUser();
                        WeiBoUser user1 = status.getWeiBoUser();
                        if (user1 != null) {
                            user.location = user1.getLocation();
                            user.name = user1.getName();
                            user.profile_image_url = user1.getProfile_image_url();
                            user.save();
                        }
                        status.user = user;
                        status.save();
                    }
                    ActiveAndroid.setTransactionSuccessful();
                } finally {
                    ActiveAndroid.endTransaction();
                }
            }

            @Override
            public void onFailure(Call<Timeline> call, Throwable t) {
                Log.e("OKHttp:----", t.getMessage());
            }
        });
    }

    //请求公众号发送的微博
    public void getPublicTimeline(String access_token, int count, int page, boolean frag) {
        Call<Timeline> call = mWeiBoService.getPublicTimeline(access_token, count, page);
        getTimeline(call, frag);
    }

    //请求用户自己发送的微博
    public void getUserTimeline(String access_token, String uid, int page, boolean frag) {
        Call<Timeline> call = mWeiBoService.getUserTimeline(access_token, uid, page);
        getTimeline(call, frag);

    }

    //上传文件   发送一个带有图片的微博
    public void upload(String access_token, String status, File pic) {
        final RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), pic);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("pic", pic.getName(), requestFile);
        Call<Status> call = mWeiBoService2.upload(RequestBody.create(MediaType.parse("multipart/form-data"), access_token),
                RequestBody.create(MediaType.parse("multipart/form-data"), status), body);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Log.e("WeiBoApi", "上传成功.." + response.body());
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.e("WeiBoApi", "上传失败.." + t.getMessage());
            }
        });
    }


}
