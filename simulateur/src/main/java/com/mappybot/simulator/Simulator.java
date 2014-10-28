package com.mappybot.simulator;

import com.mappybot.embedded.RobotStrategy;

import java.io.IOException;

import static java.lang.Math.*;

public class Simulator {

    public static void main(String[] args) throws IOException, InterruptedException {
        Graphics graphics = new Graphics();
        Robot robot = new Robot(Robot.WIDTH, 200 - Robot.HEIGHT, 0, new RobotStrategy());
        Robot robot2 = new Robot(600, 200, PI, new RobotStrategy());

        Board board = new Board(robot, robot2);
        for (int i = 1; i < 1000; i++) {
            board.refreshSensors();
            board.enforcePhysics();
            graphics.redraw(board);
            Thread.sleep(20);
        }
    }
}
