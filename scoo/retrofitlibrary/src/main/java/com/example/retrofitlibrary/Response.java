package com.example.retrofitlibrary;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Objects;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;

public final class Response<T> {

    public static <T> Response<T> success(@Nullable T body){
      return success(body,new okhttp3.Response.Builder()
                             .code(200)
                             .message("OK")
                             .protocol(Protocol.HTTP_1_1)
                             .request(new Request.Builder().url("http://localhost/").build())
                             .build());
    }

    public static <T> Response<T> success(int code, @Nullable T body)  {
        if (code < 200 || code >= 300) {
            throw new IllegalArgumentException("code < 200 or >= 300:" + code);
        }
        return success(body,new okhttp3.Response.Builder()
                .code(code)
                .message("Response.success()")
                .protocol(Protocol.HTTP_1_1)
                .request(new Request.Builder().url("http://localhost/").build())
                .build());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static <T> Response<T> success(@Nullable T body, Headers headers) {
        Objects.requireNonNull(headers,"headers == null");
        return success(body,new okhttp3.Response.Builder()
                .code(200)
                .message("ok")
                .protocol(Protocol.HTTP_1_1)
                .headers(headers)
                .request(new Request.Builder().url("http://localhost/").build())
                .build());
    }

    public static <T> Response<T> success(@Nullable T body, okhttp3.Response rawResponse){
        Objects.requireNonNull(rawResponse,"rawResponse == null");
        if (!rawResponse.isSuccessful()) {
            throw new IllegalArgumentException("rawResponse must be successful response");
        }
        return new Response<>(rawResponse,body,null);
    }

    public static <T> Response<T> error(int code,ResponseBody body) {
      Objects.requireNonNull(body,"body == null");
      if (code <400) throw  new IllegalArgumentException("code < 400" +code);
      return error(body,new okhttp3.Response.Builder()
                         .body(new OkHttp)
                         .code(code))
    }

    private final okhttp3.Response rawResponse;
    private final @Nullable T body;
    private final @Nullable ResponseBody errorBody;

    private Response(okhttp3.Response rawResponse,@Nullable T body,@Nullable ResponseBody errorBody) {
       this.rawResponse = rawResponse;
       this.body = body;
       this.errorBody = errorBody;
    }

    public okhttp3.Response raw() {
        return rawResponse;
    }

    public int code() {
        return rawResponse.code();
    }

    public String message() {
        return rawResponse.message();
    }

    public Headers headers() {
        return rawResponse.headers();
    }

    public boolean isSuccessful() {
        return rawResponse.isSuccessful();
    }

    public @Nullable T body() {
        return body;
    }

    public @Nullable ResponseBody errorBody() {
        return errorBody;
    }

    @Override
    public String toString() {
        return rawResponse.toString();
    }
}
