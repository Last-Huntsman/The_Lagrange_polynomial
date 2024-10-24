package com.cgvsu.protocurvefxapp;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Function {
    public static List<Point2D> function(List<Point2D> list) {
        Comparator<Point2D> comparator = new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
                if (o1.getX() == o2.getX()) return 0;
                return o1.getX() > o2.getX() ? 1 : -1;
            }
        };
        Collections.sort(list, comparator);
        List<Point2D> rez = new ArrayList<>();
        int a = (int) list.get(0).getX() + 1;
        int b = (int) list.getLast().getX();
        for (; a <= b; a++) {
            rez.add(new Point2D(a))
        }
    }

    private static int
}
