package com.cgvsu.protocurvefxapp;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class ProtoCurveController {

    // Элементы интерфейса, указанные в FXML-файле, которые будут инжектироваться в контроллер
    @FXML
    AnchorPane anchorPane;  // Контейнер для динамического управления размером Canvas
    @FXML
    private Canvas canvas;  // Canvas - холст для рисования

    // Список для хранения точек, на которые пользователь нажимает
    ArrayList<Point2D> points = new ArrayList<Point2D>();

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
            switch (event.getButton()) {
                case PRIMARY -> handlePrimaryClick(canvas.getGraphicsContext2D(), event);  // Обработка клика левой кнопкой
            }
        });
    }

    // Метод, который вызывается при нажатии левой кнопкой мыши
    private void handlePrimaryClick(GraphicsContext graphicsContext, MouseEvent event) {
        // Создаем объект Point2D для хранения координат клика мыши
        final Point2D clickPoint = new Point2D(event.getX(), event.getY());

        // Задаем радиус для точки, которая будет нарисована
        final int POINT_RADIUS = 3;
        // Рисуем круг (точку) в месте клика с указанным радиусом
        graphicsContext.fillOval(
                clickPoint.getX() - POINT_RADIUS, clickPoint.getY() - POINT_RADIUS,  // Центрирование точки
                2 * POINT_RADIUS, 2 * POINT_RADIUS);  // Размер точки

        // Если уже есть хотя бы одна точка, рисуем линию от последней точки к новой
        if (points.size() > 0) {
            final Point2D lastPoint = points.get(points.size() - 1);  // Получаем последнюю точку из списка
            // Рисуем линию от последней точки к новой
            graphicsContext.strokeLine(lastPoint.getX(), lastPoint.getY(), clickPoint.getX(), clickPoint.getY());
        }

        // Добавляем новую точку в список для дальнейшего использования
        points.add(clickPoint);
    }
}
