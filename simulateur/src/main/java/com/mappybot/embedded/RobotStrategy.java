package com.mappybot.embedded;

import static com.mappybot.embedded.Strategy.Action.*;

public class RobotStrategy implements Strategy {
    private static final int LIMIT = 100;
    private Action turn = RIGHT;
    private double distance;
    private double previousDistance;

    @Override
    public void refreshDistance(double distance) {
        previousDistance = this.distance;
        this.distance = distance;
        System.out.println("previous : " + previousDistance + " ; distance : " + distance);
        if (distance < LIMIT && previousDistance > distance) {
            System.out.println("changing ; previous : " + previousDistance + " ; distance : " + distance);
            turn = turn == RIGHT ? LEFT : RIGHT;
        }
    }

    @Override
    public Action getAction() {
        if (distance < LIMIT) {
            return RIGHT;
        }
        previousDistance = 0;
        distance = 0;
        return FORWARD;
    }
}