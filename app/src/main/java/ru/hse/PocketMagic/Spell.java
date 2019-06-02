package ru.hse.PocketMagic;

public class Spell {
    private String name;
    private int cost;
    private int damage;
    private int cast;
    private int duration;
    private String type;
    private int healing;
    private String description;

    public Spell(String name, int cost, int damage, int cast, int duration, String type, int healing, String description) {
        this.name = name;
        this.cost = cost;
        this.damage = damage;
        this.cast = cast;
        this.duration = duration;
        this.type = type;
        this.healing = healing;
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
