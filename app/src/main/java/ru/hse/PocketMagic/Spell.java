package ru.hse.PocketMagic;

public class Spell {
    private String name;
    private int cost;
    private int damage;
    private String description;

    public Spell(String name, int cost, int damage, String description) {
        this.name = name;
        this.cost = cost;
        this.damage = damage;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getDamage() {
        return damage;
    }

    public String getDescription() {
        return description;
    }
}
