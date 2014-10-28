package com.mappybot.simulator;

import java.awt.Polygon;
import java.util.Arrays;
import java.util.List;

import lombok.Value;

import static com.mappybot.simulator.Geometry.*;
import static java.lang.Math.*;

@Value
public class Board {
    public static final int WIDTH = 685;
    public static final int HEIGHT = 480;
    public static final Polygon BOARD_SHAPE = new Polygon(new int[] { 0, 0, WIDTH, WIDTH }, new int[] { 0, HEIGHT, HEIGHT, 0 }, 4);
    private final List<Robot> robots;

    public Board(Robot... robots) {
        this.robots = Arrays.asList(robots);
    }

    public void refreshSensors() {
        for (Robot robot : robots) {
            double dir = robot.getDirection();
            double dx = 0;
            double dy = 0;
            if (dir >= 0 && dir <= PI / 2) {
                dx = (WIDTH - robot.getX()) / cos(dir);
                dy = (HEIGHT - robot.getY()) / sin(dir);
            }
            else if (dir >= -PI / 2 && dir <= 0) {
                dx = (WIDTH - robot.getX()) / cos(dir);
                dy = (robot.getY()) / sin(dir);
            }
            else if (dir >= -PI && dir <= 0 - PI / 2) {
                dx = (robot.getX()) / cos(dir);
                dy = (robot.getY()) / sin(dir);
            }
            else if (dir >= PI / 2 && dir <= PI) {
                dx = (robot.getX()) / cos(dir);
                dy = (HEIGHT - robot.getY()) / sin(dir);
            }
            double distance = min(abs(dx), abs(dy));
            robot.getStrategy().refreshDistance(distance);
        }
    }

    public void enforcePhysics() {
        for (Robot robot : robots) {
            int previousX = robot.getX();
            int previousY = robot.getY();
            switch (robot.getStrategy().getAction()) {
                case FORWARD:
                    robot.forward();
                    break;
                case BACKWARD:
                    robot.backward();
                    break;
                case RIGHT:
                    robot.right();
                    break;
                case LEFT:
                    robot.left();
                    break;
            }

            if (!included(BOARD_SHAPE, getRobotShape(robot)) || collisionWithOtherRobot(robot)) {
                System.out.println("COLLISION !" + robot);
                robot.setX(previousX);
                robot.setY(previousY);
            }
            if (robot.getDirection() > PI) robot.setDirection(robot.getDirection() - 2 * PI);
            if (robot.getDirection() < -PI) robot.setDirection(robot.getDirection() + 2 * PI);
        }
    }

    private boolean collisionWithOtherRobot(Robot robot) {
        return robots.stream().filter(r -> !robot.equals(r)).anyMatch(r -> intersect(getRobotShape(r), getRobotShape(robot)));
    }

    static Polygon getRobotShape(Robot robot) {
        int x2 = (int) Math.round(robot.getX() + Robot.WIDTH * cos(robot.getDirection()));
        int y2 = (int) Math.round(robot.getY() + Robot.WIDTH * sin(robot.getDirection()));

        int x3 = (int) Math.round(x2 - Robot.HEIGHT * sin(robot.getDirection()));
        int y3 = (int) Math.round(y2 + Robot.HEIGHT * cos(robot.getDirection()));

        int x4 = (int) Math.round(robot.getX() - Robot.HEIGHT * sin(robot.getDirection()));
        int y4 = (int) Math.round(robot.getY() + Robot.HEIGHT * cos(robot.getDirection()));
        return new Polygon(new int[] { robot.getX(), x2, x3, x4 }, new int[] { robot.getY(), y2, y3, y4 }, 4);
    }
}
