package com.mappybot.mappybot;

import java.io.IOException;

public class Simulator {

    public static void main(String[] args) throws IOException, InterruptedException {
        Graphics graphics = new Graphics();
        Robot robot = new Robot(20, 200, Math.PI / 4, new RobotStrategy());
        Robot robot2 = new Robot(600, 200, Math.PI, new RobotStrategy());

        Board board = new Board(robot, robot2);
        for (int i = 1; i < 1000; i++) {
            board.refreshSensors();
            board.enforcePhysics();
            graphics.redraw(board);
            Thread.sleep(20);
        }
    }
}
