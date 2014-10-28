package com.mappybot.simulator;

import com.mappybot.embedded.Strategy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Robot {
    public static final int WIDTH = 71;
    public static final int HEIGHT = 58;
    private static final int STEP = 5;
    private int x;
    private int y;
    private double direction;
    private Strategy strategy;

    public void forward() {
        x += STEP * Math.cos(direction);
        y += STEP * Math.sin(direction);
        System.out.println("forward : " + this);
    }

    public void backward() {
        x -= STEP * Math.cos(direction);
        y -= STEP * Math.sin(direction);
    }

    public void right() {
        direction += 0.05;
        System.out.println("right : " + this);
    }

    public void left() {
        direction -= 0.05;
        System.out.println("left : " + this);
    }
}
