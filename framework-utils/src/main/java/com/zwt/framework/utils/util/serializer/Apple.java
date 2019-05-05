package com.zwt.framework.utils.util.serializer;

import java.io.Serializable;

/**
 * @author zwt
 * @detail
 * @date 2019/4/24
 * @since 1.0
 */
public class Apple implements Serializable{
    private String color;
    private int weight;
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
    public Apple(String color, int weight) {
        this.color = color;
        this.weight = weight;
    }
    @Override
    public String toString() {
        return "Apple{" +
                "color='" + color + '\'' +
                ", weight=" + weight +
                '}';
    }

    public Apple() {
    }
}
