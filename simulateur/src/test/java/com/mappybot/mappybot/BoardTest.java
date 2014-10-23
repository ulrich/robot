package com.mappybot.mappybot;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;

import org.junit.Test;

import static com.mappybot.mappybot.Board.*;
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
    public void should_check_if_robot_is_inside_board() throws Exception {
        Shape p2 = new Polygon(new int[] { 0, 8, 8, 0 }, new int[] { 0, 0, 8, 8 }, 4);
        Area board = new Area(new Rectangle(0, 0, WIDTH, HEIGHT));
        Area a2 = new Area(p2);
        board.intersect(a2);

        assertThat(board).isEqualTo(a2);
    }

    @Test
    public void should_get_robot_Area() throws Exception {
        Polygon robotArea = getRobotShape(new Robot(0, 0, 0, null));
        assertThat(robotArea.xpoints).isEqualTo(new int[] { 0, 71, 71, 0 });
        assertThat(robotArea.ypoints).isEqualTo(new int[] { 0, 0, 58, 58 });

        Polygon robotArea2 = getRobotShape(new Robot(100, 10, PI / 2, null));
        assertThat(robotArea2.xpoints).isEqualTo(new int[] { 100, 100, 158, 158 });
        assertThat(robotArea2.ypoints).isEqualTo(new int[] { 10, 81, 81, 10 });
    }
}
