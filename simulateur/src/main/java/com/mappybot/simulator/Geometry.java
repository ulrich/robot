package com.mappybot.simulator;

import java.awt.Polygon;
import java.awt.geom.Area;

public class Geometry {
    static boolean included(Polygon p1, Polygon p2) {
        Area s2 = new Area(p2);
        Area intersection = new Area(p1);
        intersection.intersect(s2);
        return intersection.equals(s2);
    }

    static boolean intersect(Polygon p1, Polygon p2) {
        Area s2 = new Area(p2);
        Area intersection = new Area(p1);
        intersection.intersect(s2);
        return !intersection.isEmpty();
    }
}
