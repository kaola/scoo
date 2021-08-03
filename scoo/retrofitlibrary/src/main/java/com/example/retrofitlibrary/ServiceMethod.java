package com.example.retrofitlibrary;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

abstract class ServiceMethod<T> {
     static <T> ServiceMethod<T> parseAnnotations(Retrofit retrofit, Method method){
       RequestFactory requestFactory = RequestFactory.parseAnnotations(retrofit,method);

         Type returnType = method.getGenericReturnType();
         if (Utils)
     }
}
