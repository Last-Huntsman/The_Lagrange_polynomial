package Task;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WuLineDrawing {
    private Canvas canvas;

    public WuLineDrawing(Canvas canvas) {
        this.canvas = canvas;
    }

    // Рисует пиксель с координатами (x, y) и яркостью brightness (где 0 ≤ brightness ≤ 1).
    public void plot(int x, int y, double brightness) {
        // Здесь должна быть реализация рисования пикселя на экране с заданной яркостью.
        // Преобразуем яркость в значение для альфа-канала (0 - полностью прозрачный, 1 - полностью непрозрачный)
        Color colorWithBrightness = new Color(0, 0, 0, brightness); // черный цвет с заданной яркостью

        // Получаем текущий графический контекст и рисуем точку с указанной яркостью
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(colorWithBrightness);
        gc.fillRect(x, y, 1, 1); // рисуем пиксель 1x1 }
    }
    // Функция возвращает целую часть числа x (аналог floor).
    public  int ipart(double x) {
        return (int) Math.floor(x);
    }

    // Функция округляет число x до ближайшего целого.
    public  int round(double x) {
        return ipart(x + 0.5); // Смещаем x на 0.5, чтобы корректно округлить до ближайшего целого.
    }

    // Функция возвращает дробную часть числа x.
    public  double fpart(double x) {
        return x - Math.floor(x);
    }

    // Функция возвращает 1 - дробная часть числа x (т.е. обратную дробную часть).
    public  double rfpart(double x) {
        return 1 - fpart(x);
    }

    // Основная функция для рисования линии от точки (x0, y0) до точки (x1, y1) с антиалиасингом.
    public  void drawLine(double x0, double y0, double x1, double y1) {
        // Проверка на крутизну линии: если линия крутая (наклон больше 45 градусов),
        // мы меняем x и y местами, чтобы работать с более простым случаем.
        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);

        // Если линия крутая, меняем x и y местами для обеих точек.
        if (steep) {
            double temp = x0; x0 = y0; y0 = temp;
            temp = x1; x1 = y1; y1 = temp;
        }

        // Убедимся, что линия идет слева направо: если x0 > x1, меняем точки местами.
        if (x0 > x1) {
            double temp = x0; x0 = x1; x1 = temp;
            temp = y0; y0 = y1; y1 = temp;
        }

        // Вычисляем приращения по x и y (длину проекций на оси)
        double dx = x1 - x0;
        double dy = y1 - y0;

        // Определяем наклон линии (градиент)
        double gradient = (dx == 0) ? 1 : dy / dx;

        // === Обрабатываем первый конец линии ===
        int xend = round(x0); // Округляем x0 до ближайшего целого значения
        double yend = y0 + gradient * (xend - x0); // Находим y-координату конца
        double xgap = rfpart(x0 + 0.5); // Вычисляем прозрачность для границы пикселя
        int xpxl1 = xend; // Координата x для первого пикселя
        int ypxl1 = ipart(yend); // Целая часть координаты y для первого пикселя

        // Рисуем первый пиксель с использованием яркости, зависящей от позиции линии внутри пикселя
        if (steep) {
            // Если линия крутая, координаты поменяны местами
            plot(ypxl1, xpxl1, rfpart(yend) * xgap);
            plot(ypxl1 + 1, xpxl1, fpart(yend) * xgap);
        } else {
            // Если линия не крутая, используем обычные координаты
            plot(xpxl1, ypxl1, rfpart(yend) * xgap);
            plot(xpxl1, ypxl1 + 1, fpart(yend) * xgap);
        }

        // Вычисляем первую y-пересечение для основного цикла
        double intery = yend + gradient;

        // === Обрабатываем второй конец линии ===
        xend = round(x1); // Округляем x1 до ближайшего целого значения
        yend = y1 + gradient * (xend - x1); // Находим y-координату конца
        xgap = fpart(x1 + 0.5); // Вычисляем прозрачность для границы пикселя
        int xpxl2 = xend; // Координата x для последнего пикселя
        int ypxl2 = ipart(yend); // Целая часть координаты y для последнего пикселя

        // Рисуем последний пиксель с использованием яркости, зависящей от позиции линии внутри пикселя
        if (steep) {
            // Если линия крутая, координаты поменяны местами
            plot(ypxl2, xpxl2, rfpart(yend) * xgap);
            plot(ypxl2 + 1, xpxl2, fpart(yend) * xgap);
        } else {
            // Если линия не крутая, используем обычные координаты
            plot(xpxl2, ypxl2, rfpart(yend) * xgap);
            plot(xpxl2, ypxl2 + 1, fpart(yend) * xgap);
        }

        // === Основной цикл для отрисовки центральной части линии ===
        if (steep) {
            // Если линия крутая, перемещаемся вдоль x и рисуем пиксели вдоль y
            for (int x = xpxl1 + 1; x < xpxl2; x++) {
                plot(ipart(intery), x, rfpart(intery)); // Рисуем пиксель с яркостью rfpart
                plot(ipart(intery) + 1, x, fpart(intery)); // Рисуем следующий пиксель с яркостью fpart
                intery += gradient; // Переход к следующему y-значению
            }
        } else {
            // Если линия не крутая, перемещаемся вдоль x и рисуем пиксели вдоль y
            for (int x = xpxl1 + 1; x < xpxl2; x++) {
                plot(x, ipart(intery), rfpart(intery)); // Рисуем пиксель с яркостью rfpart
                plot(x, ipart(intery) + 1, fpart(intery)); // Рисуем следующий пиксель с яркостью fpart
                intery += gradient; // Переход к следующему y-значению
            }
        }
    }

}
