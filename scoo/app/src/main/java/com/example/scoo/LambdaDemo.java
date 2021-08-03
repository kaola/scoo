package com.example.scoo;

import android.os.Build;


import androidx.annotation.RequiresApi;

import java.util.Arrays;

public class LambdaDemo {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void lambdanote() {
        Arrays.asList("a","b","c").forEach(e ->System.out.println(e));
        Arrays.asList("a","b","c").forEach((String e) ->System.out.println(e));

        Arrays.asList("a","b","c").forEach(e -> {
            System.out.print(e);
            System.out.print(e);
        });
      String separtor = "";
      Arrays.asList("a","b","c").forEach((String e) -> System.out.print(e + separtor));
       Arrays.asList("a","b","c").sort((e1, e2) -> e1.compareTo(e2));
    }


}
