package com.mappybot.simulator;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;

import org.junit.Test;

import static com.mappybot.simulator.Board.*;
import static com.mappybot.simulator.Geometry.*;
import static java.lang.Math.*;
import static org.assertj.core.api.Assertions.*;

public class GeometryTest {

    @Test
    public void should_compute_polygon_intersection() {
        Shape p1 = new Polygon(new int[] { 4, 4, 7 }, new int[] { 3, 2, 4 }, 3);
        Shape p2 = new Polygon(new int[] { 6, 8, 8, 6 }, new int[] { 1, 1, 2, 2 }, 4);
        Area a1 = new Area(p1);
        a1.intersect(new Area(p2));

        assertThat(a1.getBounds()).isEqualTo(new Rectangle(0, 0, 0, 0));
    }

    @Test
    public void should_detect_robot_collision() throws Exception {
        assertThat(intersect(getRobotShape(new Robot(0, 0, 0, null)), getRobotShape(new Robot(Robot.WIDTH, 10, 0, null)))).isFalse();
        assertThat(intersect(getRobotShape(new Robot(0, 0, 0, null)), getRobotShape(new Robot(Robot.WIDTH - 1, 10, 0, null)))).isTrue();
        assertThat(intersect(getRobotShape(new Robot(0, 0, 0, null)), getRobotShape(new Robot(Robot.WIDTH * 2 - 1, 10, PI, null)))).isTrue();
        assertThat(intersect(getRobotShape(new Robot(0, 0, 0, null)), getRobotShape(new Robot(Robot.WIDTH * 2, 10, PI, null)))).isFalse();
    }

    @Test
    public void should_detect_inside_board() throws Exception {
        assertThat(included(BOARD_SHAPE, getRobotShape(new Robot(0, 0, 0.0, null)))).isTrue();
        assertThat(included(BOARD_SHAPE, getRobotShape(new Robot(0, Board.HEIGHT - Robot.HEIGHT, 0.0, null)))).isTrue();
        assertThat(included(BOARD_SHAPE, getRobotShape(new Robot(0, Board.HEIGHT - Robot.HEIGHT + 1, 0.0, null)))).isFalse();
        assertThat(included(BOARD_SHAPE, getRobotShape(new Robot(Board.WIDTH - Robot.WIDTH, 0, 0.0, null)))).isTrue();
        assertThat(included(BOARD_SHAPE, getRobotShape(new Robot(Board.WIDTH - Robot.WIDTH + 1, 0, 0.0, null)))).isFalse();
        assertThat(included(BOARD_SHAPE, getRobotShape(new Robot(Board.WIDTH - Robot.WIDTH, Board.HEIGHT - Robot.HEIGHT, PI / 4, null)))).isFalse();
    }

}
