package com.cgvsu.protocurvefxapp;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Function {

    List<Point2D> list;
    List<Point2D> rez = new ArrayList<>();

    public Function(List<Point2D> list) {
        Comparator<Point2D> comparator = new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
                if (o1.getX() == o2.getX()) return 0;
                return o1.getX() > o2.getX() ? 1 : -1;
            }
        };
        list.sort(comparator);
        this.list = list;
        function();
        rez.add(list.getFirst());
    }

    private List<Point2D> function() {
        int a = (int) list.get(0).getX() + 1;
        int b = (int) list.getLast().getX();
        for (; a <= b; a++) {
            rez.add()
        }
    }

    private static Point2D Polinom(int)
}
