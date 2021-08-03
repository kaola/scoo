package com.example.lib.genericity;

import com.example.lib.APHero;
import com.example.lib.Hero;

import java.util.ArrayList;

public class TestGeneric {
    public static void main(String[] args) {
        ArrayList<APHero> apHeroList = new ArrayList<>();
        apHeroList.add(new APHero("12"));

        ArrayList<? extends Hero> heroList = apHeroList;
        Hero h = heroList.get(0);

//        heroList.add(new APHero("12"));
    }
}
