package com.example.lib.thread;

import com.example.lib.HeroLol;

public class TestThread {
    public static void main(String[] args) {
        final HeroLol gareen = new HeroLol();
        gareen.name = "gailun";
        gareen.hp = 616;
        gareen.damage = 50;

        final HeroLol teemo = new HeroLol();
        teemo.name = "TiMo";
        teemo.hp = 300;
        teemo.damage = 30;

        final HeroLol bh = new HeroLol();
        bh.name = "ShanJinLeiRen";
        bh.hp = 500;
        bh.damage = 65;

        final HeroLol leesin = new HeroLol();
        leesin.name = "MangSeng";
        leesin.hp = 455;
        leesin.damage = 80;
//
//        while (!teemo.isDead()) {
//            gareen.attackHero(teemo);
//        }
//
//        while (!leesin.isDead()) {
//            bh.attackHero(leesin);
//        }
//        KillThread killThread1 = new KillThread(gareen,teemo);
//        killThread1.start();
//        KillThread killThread2 = new KillThread(bh,leesin);
//        killThread2.start();

//        Battle battle1 = new Battle(gareen,teemo);
//
//        new Thread(battle1);
//
//        Battle battle2 = new Battle(bh,leesin);
//        new Thread(battle2).start();

//        Thread t1 = new Thread() {
//            @Override
//            public void run() {
//                super.run();
//               while (!teemo.isDead()) {
//                   gareen.attackHero(teemo);
//               }
//            }
//        };
//        t1.start();
//
//        Thread t2 = new Thread() {
//            @Override
//            public void run() {
//                bh.attackHero(leesin);
//            }
//        };
//        t2.start();

        Thread t1 = new Thread() {
            @Override
            public void run() {
                while (!teemo.isDead()) {
                    gareen.attackHero(teemo);
                }
            }
        };


//        try {
//            t1.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Thread t2 = new Thread() {
            @Override
            public void run() {
                while (!leesin.isDead()) {
                    bh.attackHero(leesin);
                }
            }
        };
        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);
        t1.start();
        t2.start();
    }
}
