package com.mappybot.embedded;

public interface Strategy {
    void refreshDistance(double distance);

    Action getAction();

    enum Action {
        FORWARD, BACKWARD, RIGHT, LEFT;
    }
}
