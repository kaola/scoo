package com.example.lib.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestThreadThree {
    public static String now() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public static void main(String[] args) {
        final Object someObject = new Object();

        Thread t1 = new Thread(){
            @Override
            public void run() {
                System.out.println( now()+" t1 RUN");
                System.out.println( now()+this.getName()+ " have：someObject");
              synchronized (someObject) {
                  System.out.println( now()+this.getName()+ " have object：someObject");
                  try {
                      Thread.sleep(5000);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  System.out.println( now()+this.getName()+ " free object：someObject");
              }
            }
        };
        t1.setName("t1");
        t1.start();
        Thread t2 = new Thread(){
            @Override
            public void run() {
                System.out.println( now()+" t2 run");
                System.out.println( now()+this.getName()+ " have：someObject");
                synchronized (someObject){
                    System.out.println( now()+this.getName()+ " have object 2：someObject");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println( now()+this.getName()+ " free object2：someObject");
                }
            }
        };
        t2.setName("t2");
        t2.start();
    }
}
