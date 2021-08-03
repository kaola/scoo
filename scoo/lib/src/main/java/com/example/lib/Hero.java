package com.example.lib;

import java.io.Serializable;

public class Hero implements Serializable {

    private static final long serialVersionUID = 1L;
    public String name;
    protected float hp;

    public Hero(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Hero{" +
                "name='" + name + '\'' +
                ", hp=" + hp +
                '}';
    }
}
