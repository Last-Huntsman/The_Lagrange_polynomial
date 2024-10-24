package com.cgvsu.protocurvefxapp;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Function {

    List<Point2D> list;
    List<Point2D> rez = new ArrayList<>();
    int n ;

    public List<Point2D> getRez() {
        return rez;
    }

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
        this.n= list.size();
        function();
//        if (list.getFirst().getX() != (int) list.getFirst().getX()) rez.add(list.getFirst());
    }

    private void function() {
        int a = (int) list.get(0).getX() + 1;
        int b = (int) list.get(n-1).getX();
        for (; a <= b; a++) {
            rez.add(Polinom(a));
        }
        if (list.get(n-1).getX()!=(int)list.get(n-1).getX()) rez.add(list.get(n-1));
    }

    private  Point2D Polinom(int x) {
        double y=0;
        for (int i = 0; i <n; i++) {
            double l =list.get(i).getY();
            for (int j = 0; j < n; j++) {
                if(i!=j){
                    l=l*(x-list.get(j).getX())/(list.get(i).getX()-list.get(j).getX());
                }
            }
        }
        return new Point2D(x,y);
    }
}
