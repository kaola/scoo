package com.example.lib.thread;

import com.example.lib.HeroLol;

public class TestThreadSeven {
    public static void main(String[] args) {
        final HeroLol gareen = new HeroLol();
        gareen.name = "gareen";
        gareen.hp = 616;

        Thread t1 = new Thread() {
            @Override
            public void run() {
                while (true) {
                 //因为减血更快，所以盖伦的血量迟早会到1的
                 //使用while循环判断是否是1，如果是1就不停的循环
                 //直到加血线线程回复了血量
                    while (gareen.hp == 1) {
                        continue;
                    }

                    gareen.hurt();
                    System.out.printf("t1 为%s 减血1点,减少血后，%s的血量是%.0f%n",gareen.name,gareen.name,gareen.hp);

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t1.start();

        Thread t2 = new Thread() {
            @Override
            public void run() {
                while (true) {
                    gareen.recover();
                    System.out.printf("t2 为%s 回血1点,增加血后，%s的血量是%.0f%n", gareen.name, gareen.name, gareen.hp);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t2.start();
    }
}
