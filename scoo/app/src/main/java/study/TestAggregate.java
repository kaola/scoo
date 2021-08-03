package study;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestAggregate {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) {
        Random r = new Random();
        List<Hero> heros = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            heros.add(new Hero("hero " + i, r.nextInt(1000), r.nextInt(100)));
        }


        System.out.println("初始化后的集合：");
        System.out.println(heros);
        System.out.println("查询条件：hp>100 && damage<50");
        System.out.println("通过传统操作方式找出满足条件的数据：");

        for (Hero h : heros) {
            if (h.hp > 100 && h.damage < 50) {
                System.out.println(h.name);
            }
        }
        System.out.println("通过聚合操作方式找出满足条件的数据：");

        heros.stream().filter(hero -> hero.hp > 100 && hero.damage < 50)
                .forEach(hero -> System.out.println(hero.name));

        Hero hs[] = heros.toArray(new Hero[heros.size()]);

        Arrays.stream(hs).forEach(hero -> System.out.println(hero.name));

        heros.stream().distinct().forEach(hero -> System.out.println(hero));

        heros.stream().sorted((h1,h2) -> h1.hp >= h2.hp ? 1 : -1).forEach(hero -> System.out.println(hero));

        heros.stream().limit(3).forEach(hero -> System.out.println(hero));

        heros.stream().skip(3).forEach(hero -> System.out.println(hero));

        heros.stream().mapToDouble(Hero::getHp).forEach(hero -> System.out.println(hero));

       heros.stream().forEach(hero -> System.out.println(hero));

       Object[] hss = heros.stream().toArray();

        Hero minDamageHero = heros.stream().min((h1, h2) -> h1.damage - h2.damage).get();

        Hero mxnDamageHero = heros.stream().max((h1, h2) -> h1.damage - h2.damage).get();

        long count = heros.stream().count();

        Hero firstHero = heros.stream().findFirst().get();
    }
}
