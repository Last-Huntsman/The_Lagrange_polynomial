package Task;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProtoCurveController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    ArrayList<Point2D> points = new ArrayList<>();

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseClicked(event -> {
//            loadPointsFromFile();
            if (Objects.requireNonNull(event.getButton()) == MouseButton.PRIMARY) {
                handlePrimaryClick(canvas.getGraphicsContext2D(), event);
            }
        });
    }

    private void handlePrimaryClick(GraphicsContext graphicsContext, MouseEvent event) {
        Point2D newPoint = new Point2D(event.getX(), event.getY());
        for (Point2D point : points) {
            if (Math.abs(point.getX() - newPoint.getX()) < 3 && Math.abs(point.getY() - newPoint.getY()) < 3) {
                return;
            } else if (point.getX() == newPoint.getX()) {
                paintPoint(graphicsContext, newPoint, Color.RED);
                return;
            }
        }

        points.add(newPoint);
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Point2D point : points) {
            paintPoint(graphicsContext, point, Color.BLACK);
        }

        if (points.size() > 1) {
            Function function = new Function(points);
            List<Point2D> list = function.getRez();
            for (int i = 1; i < list.size(); i++) {
                int k = 1000;
                double y1 = list.get(i - 1).getY();
                double y2 = list.get(i).getY();
                if (y1 > canvas.getHeight() + k) y1 = k;
                else if (y1 < -k) y1 = -k;
                if (y2 > canvas.getHeight() + k) y2 = k;
                else if (y2 < -k) y2 = -k;

                graphicsContext.strokeLine(list.get(i - 1).getX(), y1, list.get(i).getX(), y2);
            }
            savePointsToFile();
        }
    }

    private void paintPoint(GraphicsContext graphicsContext, Point2D point, Color color) {
        graphicsContext.setFill(color);
        final int POINT_RADIUS = 3;
        graphicsContext.fillOval(
                point.getX() - POINT_RADIUS, point.getY() - POINT_RADIUS,
                2 * POINT_RADIUS, 2 * POINT_RADIUS);
        graphicsContext.setFill(Color.BLACK);

    }

    // Сохранение точек в файл "test.txt" в текущей директории
    public void savePointsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("test.txt"))) {
            for (Point2D point : points) {
                writer.write(point.getX() + "," + point.getY());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving points: " + e.getMessage());
        }
    }

    // Загрузка точек из файла "test.txt" и построение линий
    public void loadPointsFromFile() {
        points.clear(); // Очищаем текущий список точек
        try (BufferedReader reader = new BufferedReader(new FileReader("test.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] coordinates = line.split(",");
                double x = Double.parseDouble(coordinates[0]);
                double y = Double.parseDouble(coordinates[1]);
                points.add(new Point2D(x, y));
            }
            redrawCanvas();
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading points: " + e.getMessage());
        }
    }

    // Перерисовка Canvas с точками и линиями
    private void redrawCanvas() {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Отрисовка всех точек
        for (Point2D point : points) {
            paintPoint(graphicsContext, point, Color.BLACK);
        }

        // Отрисовка линий, если есть более одной точки
        if (points.size() > 1) {
            Function function = new Function(points);
            List<Point2D> list = function.getRez();
            for (int i = 1; i < list.size(); i++) {
                int k = 1000;
                double y1 = list.get(i - 1).getY();
                double y2 = list.get(i).getY();
                if (y1 > canvas.getHeight() + k) y1 = k;
                else if (y1 < -k) y1 = -k;
                if (y2 > canvas.getHeight() + k) y2 = k;
                else if (y2 < -k) y2 = -k;
                graphicsContext.strokeLine(list.get(i - 1).getX(), y1, list.get(i).getX(), y2);
            }
        }
    }
}
