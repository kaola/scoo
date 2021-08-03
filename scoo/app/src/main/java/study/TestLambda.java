package study;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class TestLambda {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) {
        Random r = new Random();
        List<Hero> heros = new ArrayList<>();
        for (int i = 0; i<10; i++) {
            heros.add(new Hero("hero " + i,r.nextInt(1000), r.nextInt(100)));
        }
        System.out.println("初始化后的集合：");
        System.out.println(heros);
        System.out.println("筛选出 hp>100 && damange<50的英雄");
        filter(heros);
        HeroChecker checker = new HeroChecker() {
            @Override
            public boolean test(Hero h) {
                return (h.hp > 100 && h.damage < 50);
            }
        };
        filterOne(heros,checker);
        filterOne(heros, h -> h.hp > 100 && h.damage < 50);
//        filterOne(heros, h -> TestLambda.testHero(h));
//        filterOne(heros, TestLambda::testHero);
        TestLambda test = new TestLambda();
        filterOne(heros, test::testHero);
        filterOne(heros, h -> h.hp > 100 && h.damage < 50);
        filterOne(heros,h -> h.matched());
        filterOne(heros, Hero::matched);

        Supplier<List> s = new Supplier<List>() {
            @Override
            public List get() {
                return new ArrayList();
            }
        };
    List list1 = getList(s);

    List list2 = getList(()-> new ArrayList());

    List list3 = getList(ArrayList::new);

    for (Hero h : heros) {
        if (h.hp > 100 && h.damage <50) {
            System.out.println(h.name);
        }
    }
        //通过聚合操作方式找出满足条件的数据
        heros.stream().filter(h -> h.hp >100 && h.damage < 50).forEach(h ->System.out.println(h.name));
    }


    private static void filter(List<Hero> heros) {
        for (Hero hero : heros) {
            if (hero.hp > 100 && hero.damage <50) {
                System.out.print(hero);
            }
        }
    }

    private static void filterOne(List<Hero> heros, HeroChecker checker) {
        for (Hero hero : heros) {
            if (checker.test(hero)) {
                System.out.print(hero);
            }
        }
    }

    public  boolean testHero(Hero hero) {
        return hero.hp > 100 && hero.damage < 50;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List getList(Supplier<List> s){
        return s.get();
    }
}
