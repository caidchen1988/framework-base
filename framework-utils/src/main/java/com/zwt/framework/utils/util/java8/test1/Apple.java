package com.zwt.framework.utils.util.java8.test1;

/**
 * @author zwt
 * @detail
 * @date 2018/10/8
 * @since 1.0
 */
public class Apple {
    private String color;
    private int weight;

    public Apple() {
    }

    public Apple(String color, int weight) {
        this.color = color;
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Apple{" +
                "color='" + color + '\'' +
                ", weight=" + weight +
                '}';
    }
}
