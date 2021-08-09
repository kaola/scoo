package com.example.lib.thread;

import com.example.lib.HeroLol;
import com.example.lib.HeroLolTwo;

public class TestThreadEight {
    public static void main(String[] args) {
        final HeroLolTwo gareen = new HeroLolTwo();
        gareen.name = "gareen";
        gareen.hp = 616;

        Thread t1 = new Thread() {
            @Override
            public void run() {
                while (true) {
                    gareen.hurt();
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        t1.start();
        Thread t2 = new Thread(){
            @Override
            public void run() {
             while (true) {
                 gareen.recover();
                 try {
                     Thread.sleep(100);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
            }
        };
        t2.start();
        String str = "nihoa";
        String str1 = "nihoa";
    }
}
