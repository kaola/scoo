package com.example.retrofitlibrary;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;

public final class RequestBuilder {
     private static final char[] HEX_DIGITS = {
       '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

     private static final String PATH_SEGMENT_ALWAYS_ENCODE_SET = "\" \\\"<>^`{}|\\\\?#\";\n";

     private static final Pattern PATH_TRAVERSAL = Pattern.compile("(.*/)?(\\.|%2e|%2E){1,2}(/.*)?");

     private final String method;

     private final HttpUrl baseUrl;
     private @Nullable String relativeUrl;
     private @Nullable HttpUrl.Builder urlBuilder;

     private final Request.Builder requestBuilder;
     private final Headers.Builder headersBuilder;
     private @Nullable MediaType contentType;

     private final boolean hasBody;
     private @Nullable MultipartBody.Builder multipartBuilder;
     private @Nullable FormBody.Builder formBuilder;
     private @Nullable RequestBody body;

    RequestBuilder(
            String method,
            HttpUrl baseUrl,
            @Nullable String relativeUrl,
            @Nullable Headers headers,
            @Nullable MediaType contentType,
            boolean hasBody,
            boolean isFormEncoded,
            boolean isMultipart) {
        this.method = method;
        this.baseUrl = baseUrl;
        this.relativeUrl = relativeUrl;
        this.requestBuilder = new Request.Builder();
        this.contentType = contentType;
        this.hasBody = hasBody;

        if (headers != null) {
            headersBuilder = headers.newBuilder();
        }else {
            headersBuilder = new Headers.Builder();
        }

        if (isFormEncoded) {
            formBuilder = new FormBody.Builder();
        }else if (isMultipart) {
            multipartBuilder = new MultipartBody.Builder();
            multipartBuilder.setType(MultipartBody.FORM);
        }
    }

    void setRelativeUrl(Object relativeUrl) {
        this.relativeUrl = relativeUrl.toString();
    }

    void addHeader(String name, String value) {
      if ("Content-Type".equalsIgnoreCase(name)) {
          try {
              contentType = MediaType.get(value);
          }catch (IllegalArgumentException e) {
              throw  new IllegalArgumentException("Malformed content type: "+ value, e);
          }
      }else {
          headersBuilder.add(name,value);
      }
    }

    void addHeaders(Headers headers){
        headersBuilder.addAll(headers);
    }

    void addPathParam(String name, String value, boolean encoded) {
        if (relativeUrl == null) {
            throw new AssertionError();
        }
        String replacement = canonicalizeForPath(value,encoded);
    }

    private static String canonicalizeForPath(String input, boolean alreadyEncoded) {
        int codePoint;
        for (int i = 0, limit = input.length(); i<limit; i += Character.charCount(codePoint)){
            codePoint = input.codePointAt(i);
            if (codePoint <0x20 || codePoint >= 0x7f
                         || PATH_SEGMENT_ALWAYS_ENCODE_SET.indexOf(codePoint) != -1
                         || (!alreadyEncoded && (codePoint == '/' || codePoint == '%'))){
                  Buffer out = new Buffer() ;
                  out.writeUtf8(input,0,i);
                  canonicalizeForPath(out,input,i,limit,alreadyEncoded);
                  return out.readUtf8();
            }
        }
        return input;
    }

    private static void canonicalizeForPath(Buffer out,String input,int pos,int limit,boolean alreadyEncoded) {
        Buffer utf8Buffer = null;
        int codePoint;
        for (int i = pos; i < limit; i += Character.charCount(codePoint)) {
            codePoint = input.codePointAt(i);
            if (alreadyEncoded && (codePoint == '\t' || codePoint == '\n' || codePoint == '\f' || codePoint == '\r')) {
                // Skip this character.
            }else if (codePoint < 0x20
                    || codePoint >= 0x7f
                    || PATH_SEGMENT_ALWAYS_ENCODE_SET.indexOf(codePoint) != -1
                    || (!alreadyEncoded && (codePoint == '/' || codePoint == '%'))){
            if (utf8Buffer == null) {
                utf8Buffer = new Buffer();
            }
            utf8Buffer.writeUtf8CodePoint(codePoint);
            while (!utf8Buffer.exhausted()) {
                int b = utf8Buffer.readByte() & 0xff;
                out.writeByte('%');
                out.writeByte(HEX_DIGITS[(b >> 4) & 0xf]);
                out.writeByte(HEX_DIGITS[b & 0xf]);
            }
        }else {
               out.writeUtf8CodePoint(codePoint);
            }
        }
    }
    void addQueryParam(String name,@Nullable String value,boolean encoded){
        if (relativeUrl != null) {
            urlBuilder = baseUrl.newBuilder(relativeUrl);
            if (urlBuilder == null) {
                throw new IllegalArgumentException( "Malformed URL. Base: " + baseUrl + ", Relative: " + relativeUrl);
            }
          relativeUrl = null;
        }
        if (encoded) {
            urlBuilder.addEncodedQueryParameter(name,value);
        }else {
            urlBuilder.addQueryParameter(name,value);
        }
    }
   @SuppressWarnings("ConstantConditions")
    void addFormField(String name, String value, boolean encoded){
      if (encoded) {
          formBuilder.addEncoded(name,value);
      }else {
          formBuilder.add(name,value);
      }
    }

    @SuppressWarnings("ConstantConditions") // Only called when isMultipart was true.
    void addPart(Headers headers, RequestBody body){
        multipartBuilder.addPart(headers,body);
    }

    @SuppressWarnings("ConstantConditions") // Only called when isMultipart was true.
    void addPart(MultipartBody.Part part) {
        multipartBuilder.addPart(part);
    }

    void setBody(RequestBody body){
        this.body = body;
    }

    <T> void addTag(Class<T> cls,@Nullable T value){
        requestBuilder.tag(cls,value);
    }

    Request.Builder get(){
        HttpUrl url;
        HttpUrl.Builder urlBuilder = this.urlBuilder;
        if (urlBuilder != null) {
            url = urlBuilder.build();
        }else {
            // No query parameters triggered builder creation, just combine the relative URL and base URL.
            //noinspection ConstantConditions Non-null if urlBuilder is null.
            url = baseUrl.resolve(relativeUrl);
           if (url == null) {
             throw new IllegalArgumentException("Malformed URL. Base: " + baseUrl + ", Relative: " + relativeUrl);
           }
        }
        RequestBody body = this.body;
        if (body == null) {
            if (formBuilder != null) {
                body = formBuilder.build();
            }else if (multipartBuilder != null){
                body = multipartBuilder.build();
            }else if (hasBody) {
                body = RequestBody.create(null,new byte[0]);
            }
        }

        MediaType contentType = this.contentType;
        if (contentType != null) {
            if (body != null) {
                body = new ContentTypeOverridingRequestBody(body,contentType);
            }else {
                headersBuilder.add("Content-Type",contentType.toString());
            }
        }

        return requestBuilder.url(url).headers(headersBuilder.build()).method(method, body);
    }

    private static class ContentTypeOverridingRequestBody extends RequestBody {
        private final RequestBody delegate;
        private final MediaType contentType;

        ContentTypeOverridingRequestBody(RequestBody delegate, MediaType contentType){
            this.delegate = delegate;
            this.contentType = contentType;
        }

        @Override
        public MediaType contentType() {
            return null;
        }

        @Override
        public long contentLength() throws IOException {
            return delegate.contentLength();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
           delegate.writeTo(sink);
        }
    }
}
