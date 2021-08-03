package com.example.lib.genericity;

import com.example.lib.Hero;

import java.util.LinkedList;

public class HeroStack {
    LinkedList<Hero> heros = new LinkedList<>();
    public void push(Hero hero) {
        heros.addLast(hero);
    }

    public Hero pull() {
        return heros.removeLast();
    }

    public Hero peek() {
        return heros.getLast();
    }

    public static void main(String[] args) {
        HeroStack heroStack = new HeroStack();
        for (int i =0; i < 5; i++) {
            Hero h = new Hero("hero name " +i);
            System.out.println("压入 hero:" + h);
            heroStack.push(h);
        }

        for (int i = 0; i < 5; i++) {
            Hero h = heroStack.pull();
            System.out.println("弹出 hero" + h);
        }
    }
}
