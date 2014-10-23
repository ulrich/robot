package com.mappybot.mappybot;

public interface Strategy {
    void refreshDistance(double distance);

    Action getAction();
}

enum Action {
    FORWARD, BACKWARD, RIGHT, LEFT;
}
