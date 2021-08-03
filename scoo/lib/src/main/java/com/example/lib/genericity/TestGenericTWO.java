package com.example.lib.genericity;

import com.example.lib.APHero;
import com.example.lib.Hero;

import java.util.ArrayList;

public class TestGenericTWO {
    public static void main(String[] args) {
        ArrayList<? super Hero> heroList = new ArrayList<>();
        heroList.add(new Hero("1"));
        heroList.add(new Hero("2"));
        heroList.add(new APHero("1"));
//        Hero h =  heroList.get(0);
    }
}
