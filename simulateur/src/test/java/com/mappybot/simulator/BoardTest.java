package com.mappybot.simulator;

import com.mappybot.embedded.Strategy;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;

import org.junit.Test;

import static com.mappybot.simulator.Board.*;
import static java.lang.Math.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BoardTest {
    private final Strategy strategy = mock(Strategy.class);
    private final Robot robot = new Robot(0, 0, 0, strategy);
    private final Board board = new Board(robot);

    @Test
    public void should_compute_distance_to_horizontal_board_limit() {
        robot.setX(100);
        board.refreshSensors();

        verify(strategy).refreshDistance(Board.WIDTH - 100);
    }

    @Test
    public void should_compute_distance_to_horizontal_board_limit1() {
        robot.setX(100);
        robot.setDirection(PI);
        board.refreshSensors();

        verify(strategy).refreshDistance(100);
    }

    @Test
    public void should_compute_distance_to_vertical_board_limit1() {
        robot.setY(40);
        robot.setDirection(-PI / 2);
        board.refreshSensors();

        verify(strategy).refreshDistance(40);
    }

    @Test
    public void should_compute_distance_to_vertical_board_limit() {
        robot.setY(50);
        robot.setDirection(Math.PI / 2);

        board.refreshSensors();

        verify(strategy).refreshDistance(Board.HEIGHT - 50);
    }

    @Test
    public void should_compute_distance_to_diagonal_board_limit() {
        robot.setX(0);
        robot.setY(0);
        robot.setDirection(Math.PI / 4);

        board.refreshSensors();

        verify(strategy).refreshDistance(Board.HEIGHT * Math.sqrt(2));
    }

    @Test
    public void should_compute_distance_to_diagonal2_board_limit() {
        robot.setDirection(-Math.PI / 4);
        robot.setY(3);
        robot.setY(100);

        board.refreshSensors();

        verify(strategy).refreshDistance(100 * Math.sqrt(2));
    }

    @Test
    public void should_compute_polygon_intersection() {
        Shape p1 = new Polygon(new int[] { 4, 4, 7 }, new int[] { 3, 2, 4 }, 3);
        Shape p2 = new Polygon(new int[] { 6, 8, 8, 6 }, new int[] { 1, 1, 2, 2 }, 4);
        Area a1 = new Area(p1);
        a1.intersect(new Area(p2));

        assertThat(a1.getBounds()).isEqualTo(new Rectangle(0, 0, 0, 0));
    }

    @Test
    public void should_get_robot_Shape() throws Exception {
        Polygon robotArea = getRobotShape(new Robot(0, 0, 0, null));
        assertThat(robotArea.xpoints).isEqualTo(new int[] { 0, 71, 71, 0 });
        assertThat(robotArea.ypoints).isEqualTo(new int[] { 0, 0, 58, 58 });

        Polygon robotArea2 = getRobotShape(new Robot(100, 10, PI / 2, null));
        assertThat(robotArea2.xpoints).isEqualTo(new int[] { 100, 100, 42, 42 });
        assertThat(robotArea2.ypoints).isEqualTo(new int[] { 10, 81, 81, 10 });

        Polygon robotArea3 = getRobotShape(new Robot(100, 10, PI / 4, null));
        assertThat(robotArea3.xpoints).isEqualTo(new int[] { 100, 150, 109, 59 });
        assertThat(robotArea3.ypoints).isEqualTo(new int[] { 10, 60, 101, 51 });
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
        assertThat(Board.insideBoard(new Robot(0, 0, 0.0, null))).isTrue();
        assertThat(Board.insideBoard(new Robot(0, Board.HEIGHT - Robot.HEIGHT, 0.0, null))).isTrue();
        assertThat(Board.insideBoard(new Robot(0, Board.HEIGHT - Robot.HEIGHT + 1, 0.0, null))).isFalse();
        assertThat(Board.insideBoard(new Robot(Board.WIDTH - Robot.WIDTH, 0, 0.0, null))).isTrue();
        assertThat(Board.insideBoard(new Robot(Board.WIDTH - Robot.WIDTH + 1, 0, 0.0, null))).isFalse();
        assertThat(Board.insideBoard(new Robot(Board.WIDTH - Robot.WIDTH, Board.HEIGHT - Robot.HEIGHT, PI / 4, null))).isFalse();
    }
}
