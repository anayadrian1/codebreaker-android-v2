package edu.cnm.deepdive.codebreaker.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.codebreaker.BuildConfig;
import edu.cnm.deepdive.codebreaker.model.entity.Match;
import edu.cnm.deepdive.codebreaker.model.entity.User;
import io.reactivex.Single;
import java.util.UUID;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CodebreakerWebService {

  @GET("users/me")
  Single<User> getProfile(@Header("Authorization") String bearerToken); // when we invoke this method we want retrofit to GET request and bring an object back.

  @POST("matches")
  Single<Match> startMatch(@Header("Authorization") String bearerToken, @Body Match match);

  @GET("matches/{matchId}")
  Single<Match> getMatch(@Header("Authorization") String bearerToken, @Path("matchId") UUID matchId);

  // all the things we do in postman, we will implement in the interface
  // going to define all the requests we can send to the webservice

  static CodebreakerWebService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  class InstanceHolder {

    private static final CodebreakerWebService INSTANCE;

    static {
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
          .create();
      HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
      interceptor.setLevel(BuildConfig.DEBUG ? Level.BODY : Level.NONE); // based on the status of this build, build everything, or build nothing
      OkHttpClient client = new OkHttpClient.Builder()
          .addInterceptor(interceptor) // any traffic uses this interceptor for logging
          .build();
      Retrofit retrofit = new Retrofit.Builder()
          .addConverterFactory(GsonConverterFactory.create(gson)) // add converter to retrofit object
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // specifies the connection to reactivex
          .client(client)
          .baseUrl(BuildConfig.BASE_URL)
          .build();
      INSTANCE = retrofit.create(CodebreakerWebService.class);
    }

  }

}
