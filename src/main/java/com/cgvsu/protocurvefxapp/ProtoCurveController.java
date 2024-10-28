package com.cgvsu.protocurvefxapp;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProtoCurveController {

    // Элементы интерфейса, указанные в FXML-файле, которые будут инжектироваться в контроллер
    @FXML
    AnchorPane anchorPane;  // Контейнер для динамического управления размером Canvas
    @FXML
    private Canvas canvas;  // Canvas - холст для рисования

    // Список для хранения точек, на которые пользователь нажимает
    ArrayList<Point2D> points = new ArrayList<>();

    // Метод, который вызывается автоматически после загрузки интерфейса
    @FXML
    private void initialize() {
        // Добавляем слушатель изменений ширины контейнера anchorPane
        // Когда ширина anchorPane изменяется, изменяем ширину canvas соответственно
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));

        // Добавляем слушатель изменений высоты контейнера anchorPane
        // Когда высота anchorPane изменяется, изменяем высоту canvas соответственно
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        // Устанавливаем обработчик для кликов мыши по canvas
        canvas.setOnMouseClicked(event -> {
            // Проверяем, что пользователь нажал левую кнопку мыши
            if (Objects.requireNonNull(event.getButton()) == MouseButton.PRIMARY) {
                handlePrimaryClick(canvas.getGraphicsContext2D(), event);  // Обработка клика левой кнопкой
            }
        });
    }

    // Метод, который вызывается при нажатии левой кнопкой мыши
    private void handlePrimaryClick(GraphicsContext graphicsContext, MouseEvent event) {
        Point2D newpoint = new Point2D(event.getX(), event.getY());
        for (Point2D point : points) {
            if (Math.abs(point.getX() - newpoint.getX()) < 3 && Math.abs(point.getY() - newpoint.getY()) < 3) {
                return;
            } else if (point.getX() == newpoint.getX()) {
                Paintpoint(graphicsContext, newpoint, Color.RED);
                return;
            }
        }
        points.add(newpoint);
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (Point2D point : points) {
            Paintpoint(graphicsContext, point, Color.BLACK);
        }
        if (points.size() > 1) {
            Function function = new Function(points);
            List<Point2D> list = function.getRez();
            for (int i = 1; i < list.size(); i++) {
                graphicsContext.strokeLine(list.get(i - 1).getX(), list.get(i - 1).getY(), list.get(i).getX(), list.get(i).getY());
            }
        }


    }

    private void Paintpoint(GraphicsContext graphicsContext, Point2D point, Color color) {
        graphicsContext.setFill(color);
        final int POINT_RADIUS = 3;
        graphicsContext.fillOval(
                point.getX() - POINT_RADIUS, point.getY() - POINT_RADIUS,  // Центрирование точки
                2 * POINT_RADIUS, 2 * POINT_RADIUS);  // Размер точки
        graphicsContext.setFill(Color.BLACK);
    }
}
