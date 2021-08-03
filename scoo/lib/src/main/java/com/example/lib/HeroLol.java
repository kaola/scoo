package com.example.lib;

import java.io.Serializable;

public class HeroLol {

    public String name;
    public float hp;

    public int damage;

    public void attackHero(HeroLol hero) {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        hero.hp -= damage;
        System.out.format("%s 正在攻击 %s, %s的血变成了 %.0f%n", name, hero.name, hero.name, hero.hp);

        if (hero.isDead()) {
            System.out.println(hero.name + "die！");
        }
    }

    public void recover() {
        hp = hp + 1;
    }

    public void hurt() {
        hp = hp-1;
    }

    public boolean isDead() {
        return 0 >= hp ? true : false;
    }
}
