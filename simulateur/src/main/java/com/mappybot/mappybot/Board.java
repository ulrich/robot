package com.mappybot.mappybot;

import java.awt.Polygon;
import java.awt.geom.Area;
import java.util.Arrays;
import java.util.List;

import lombok.Value;

import org.assertj.core.util.VisibleForTesting;

import static java.lang.Math.*;

@Value
public class Board {
    public static final int WIDTH = 620;
    public static final int HEIGHT = 410;
    private static final Polygon BOARD_SHAPE = new Polygon(new int[] { 0, 0, WIDTH, WIDTH }, new int[] { 0, HEIGHT, HEIGHT, 0 }, 4);
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

            Area boardArea = new Area(BOARD_SHAPE);
            boardArea.intersect(new Area(getRobotShape(robot)));

            if (robot.getX() < 0 || robot.getY() < 0 || robot.getY() > WIDTH || robot.getY() > HEIGHT) {
                System.out.println("COLLISION !" + robot);
                robot.setX(previousX);
                robot.setY(previousY);
            }

            if (robot.getDirection() > PI) robot.setDirection(robot.getDirection() - 2 * PI);
            if (robot.getDirection() < -PI) robot.setDirection(robot.getDirection() + 2 * PI);
        }
    }

    @VisibleForTesting
    static Polygon getRobotShape(Robot robot) {
        int x2 = (int) Math.round(robot.getX() + robot.getWidth() * cos(robot.getDirection()));
        int y2 = (int) Math.round(robot.getY() + robot.getWidth() * sin(robot.getDirection()));

        int x3 = (int) Math.round(x2 + robot.getHeight() * sin(robot.getDirection()));
        int y3 = (int) Math.round(y2 + robot.getHeight() * cos(robot.getDirection()));

        int x4 = (int) Math.round(robot.getX() + robot.getHeight() * sin(robot.getDirection()));
        int y4 = (int) Math.round(robot.getY() + robot.getHeight() * cos(robot.getDirection()));
        return new Polygon(new int[] { robot.getX(), x2, x3, x4 }, new int[] { robot.getY(), y2, y3, y4 }, 4);
    }
}
