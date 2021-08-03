package com.example.retrofitlibrary;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.HttpUrl;

public final class Retrofit {
    private final Map<Method,ServiceMethod<?>> serviceMethodCache = new ConcurrentHashMap<>();

    final Call.Factory callFactory;
    final HttpUrl baseUrl;
    final List<Converter.Factory> converterFactories;
    final List<CallAd.Factory> converterFactories;

    public Retrofit(Call.Factory callFactory, HttpUrl baseUrl, List<Converter.Factory> converterFactories) {
        this.callFactory = callFactory;
        this.baseUrl = baseUrl;
        this.converterFactories = converterFactories;
    }
}
