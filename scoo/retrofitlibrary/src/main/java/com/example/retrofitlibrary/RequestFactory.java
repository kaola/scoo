package com.example.retrofitlibrary;

import androidx.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.regex.Pattern;

import okhttp3.Headers;
import okhttp3.MediaType;

public final class RequestFactory {
   static RequestFactory parseAnnotations(Retrofit retrofit,Method method) {
       return new Builder(retrofit,method).
   }

   static final class Builder{
       private static final String PARAM ="[a-zA-Z][a-zA-Z0-9_-]*";
       private static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{(" + PARAM + ")\\}");
       private static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);

       final Retrofit retrofit;
       final Method method;
       final Annotation[] methodAnnotations;
       final Annotation[][] parameterAnnotationsArray;
       final Type[] parameterType;

       boolean gotField;
       boolean gotPart;
       boolean gotBody;
       boolean gotPath;
       boolean gotQuery;
       boolean gotQueryName;
       boolean gotQueryMap;
       boolean gotUrl;
       @Nullable String httpMethod;
       boolean hasBody;
       boolean isFormEncoded;
       boolean isMultipart;
       @Nullable String relativeUrl;
       @Nullable Headers headers;
       @Nullable
       MediaType contentType;
       @Nullable
       Set<String> relativeUrlParamNames;
       @Nullable ParameterHandler<?>[] parameterHandlers;
       boolean isKotlinSuspendFunction;

       Builder(Retrofit retrofit,Method method) {
           this.retrofit = retrofit;
           this.method = method;
           this.methodAnnotations = method.getAnnotations();
           this.parameterType = method.getGenericParameterTypes();
           this.parameterAnnotationsArray = method.getParameterAnnotations();
       }

       RequestFactory build(){
           for (Annotation annotation : methodAnnotations) {
               parseM
           }
       }

       private void parseMethodAnnotation(Annotation annotation) {
           if (annotation instanceof )
       }
   }
}
