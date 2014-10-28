package com.mappybot.simulator;

import com.mappybot.embedded.Strategy;

import java.awt.Polygon;

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
}
