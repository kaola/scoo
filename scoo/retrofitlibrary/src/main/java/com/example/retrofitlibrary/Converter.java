package com.example.retrofitlibrary;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

public interface Converter <F,T>{
    @Nullable
    T convert(F value) throws IOException;

    abstract class Factory{
        public @Nullable Converter<ResponseBody,?> responseBodyConverter(Type type, Annotation[] annotation,Retr){

        }
    }
}
