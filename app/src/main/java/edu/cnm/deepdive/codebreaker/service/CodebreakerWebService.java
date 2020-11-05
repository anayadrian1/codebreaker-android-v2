package edu.cnm.deepdive.codebreaker.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.codebreaker.BuildConfig;
import edu.cnm.deepdive.codebreaker.model.entity.User;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CodebreakerWebService {

  @GET("users/me")
  Single<User> getProfile(@Header("Authorization") String bearerToken); // when we invoke this method we want retrofit to GET request and bring an object back.

  static CodebreakerWebService getInstance() {
    return InstanceHolder.INSTANCE;
  }

  class InstanceHolder {

    private static final CodebreakerWebService INSTANCE;

    static {
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
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
