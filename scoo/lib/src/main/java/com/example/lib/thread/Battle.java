package com.example.lib.thread;

import com.example.lib.HeroLol;

public class Battle implements Runnable {

    private HeroLol h1;
    private HeroLol h2;

    public Battle(HeroLol h1,HeroLol h2){
        this.h1 = h1;
        this.h2 = h2;
    }

    @Override
    public void run() {
      while (!h2.isDead()) {
          h1.attackHero(h2);
      }
    }
}
