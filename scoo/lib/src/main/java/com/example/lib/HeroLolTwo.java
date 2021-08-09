package com.example.lib;

public class HeroLolTwo {

    public String name;
    public float hp;

    public int damage;

    public void attackHero(HeroLolTwo hero) {
        hero.hp -= damage;
        System.out.format("%s 正在攻击 %s, %s的血变成了 %.0f%n", name, hero.name, hero.name, hero.hp);

        if (hero.isDead()) {
            System.out.println(hero.name + "die！");
        }
    }
    //回血
    //直接在方法前面加上修饰符synchronized
    //其所对应的同步对象，就是this
    //和hurt方法达到的效果一样
    public synchronized void recover() {
        hp = hp + 1;
        System.out.printf("%s 回血1点,增加血后，%s的血量是%.0f%n", name, name, hp);
        this.notify();
    }
   //掉血
    public void hurt() {
        //使用this作为同步对象
        synchronized (this) {
            if (hp == 1) {
                try {
                    //让占有this的减血线程，暂时释放对this的占用，并等待
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            hp = hp-1;
            System.out.printf("%s 减血1点,减少血后，%s的血量是%.0f%n", name, name, hp);
        }
    }

    public boolean isDead() {
        return 0 >= hp ? true : false;
    }
}
