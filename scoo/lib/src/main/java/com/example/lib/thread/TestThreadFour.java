package com.example.lib.thread;

import com.example.lib.HeroLol;

public class TestThreadFour {

    public static void main(String[] args) {
      final Object someObject = new Object();
      final HeroLol gareen = new HeroLol();
      gareen.name = "guanlun";
      gareen.hp = 10000;

      int n = 10000;

      Thread[] addThreads = new Thread[n];
      Thread[] reduceThread = new Thread[n];

      for (int i = 0; i < n; i++) {
          Thread t = new Thread(){
              @Override
              public void run() {
                  //任何线程要修改hp的值，必须先占用someObject
                  synchronized (someObject) {
                      gareen.recover();
                  }

                  try {
                      Thread.sleep(100);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          };
          t.start();
          addThreads[i] = t;
      }

      for (int i=0; i<n; i++) {
          Thread t = new Thread() {
              @Override
              public void run() {
                  //任何线程要修改hp的值，必须先占用someObject
                  synchronized (someObject) {
                      gareen.hurt();
                  }

                  try {
                      Thread.sleep(100);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          };
          t.start();;
          reduceThread[i] = t;
      }

      for (Thread t : addThreads) {
          try {
              t.join();
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      }

      for (Thread t : reduceThread) {
          try {
              t.join();
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      }
      System.out.printf("%d,GeAddThreadAnd%dGeReduceThreadEnd%nGaiLunis%.0f%n",n,n,gareen.hp);
    }
}
