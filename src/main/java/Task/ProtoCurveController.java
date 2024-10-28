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
            loadPointsFromFile();
//            if (Objects.requireNonNull(event.getButton()) == MouseButton.PRIMARY) {
//                handlePrimaryClick(canvas.getGraphicsContext2D(), event);
//            }
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
                if (list.get(i - 1).getY() > canvas.getHeight() + 10 || list.get(i).getY() > canvas.getHeight() + 10 ||
                        list.get(i - 1).getY() < -10 || list.get(i).getY() < -10) continue;
                graphicsContext.strokeLine(list.get(i - 1).getX(), list.get(i - 1).getY(), list.get(i).getX(), list.get(i).getY());
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
                if (list.get(i - 1).getY() > canvas.getHeight() + 10 || list.get(i).getY() > canvas.getHeight() + 10 ||
                        list.get(i - 1).getY() < -10 || list.get(i).getY() < -10) continue;
                graphicsContext.strokeLine(list.get(i - 1).getX(), list.get(i - 1).getY(), list.get(i).getX(), list.get(i).getY());
            }
        }
    }
}
