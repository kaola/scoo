package com.example.lib.thread;

import com.example.lib.HeroLol;

public class TestThreadSix {
    public static void main(String[] args) {
        final HeroLol ahri = new HeroLol();
        ahri.name = "ahri";
        final HeroLol annie = new HeroLol();
        annie.name = "annie";

        Thread t1 = new Thread() {
            @Override
            public void run() {
                //占有九尾妖狐
                synchronized (ahri) {
                    System.out.println("t1 have ahri");
                    try {
                        //停顿1000毫秒，另一个线程有足够的时间占有安妮
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("t1 try hava annie");
                    System.out.println("t1 waiiting");
                    synchronized (annie) {
                        System.out.println("do something");
                    }
                }
            }
        };

        t1.start();
        Thread t2 = new Thread(){
            @Override
            public void run() {
                //占有安妮
                synchronized (annie) {
                    System.out.println("t2 already annie");
                    try {
                        //停顿1000毫秒,另一个线程有足够的时间占有暂用九尾妖狐
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("t2 try hava ahri");
                    System.out.println("t2 waiting");
                    synchronized (ahri) {
                        System.out.println("do something");
                    }
                }
            }
        };
        t2.start();
    }
}
