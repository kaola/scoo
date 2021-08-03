package com.example.retrofitlibrary.http;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.METHOD;

@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface PATCH {
    String value() default "";
}
