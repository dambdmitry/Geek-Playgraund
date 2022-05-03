import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class test {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        //Запишите сюда первый ход
        String[] points = readPoints();
        List<Point> nPoints = new LinkedList<>();
        for (String point : points) {
            nPoints.add(convertStringToPoint(point));
        }
        while (true) {
            //Ответ от соперника, если -1, то он пропустил ход
            String[] coordinates = scanner.nextLine().split(" ");
            int x1 = Integer.parseInt(coordinates[0]);
            int y1 = Integer.parseInt(coordinates[1]);
            int x2 = Integer.parseInt(coordinates[2]);
            int y2 = Integer.parseInt(coordinates[3]);
            //Логика игры, запоните переменные координатами точек, из которых хотите соеденить линию
            if (x1 != -1) {
                final Point p1 = new Point(x1, y1);
                final Point p2 = new Point(x2, y2);
                nPoints.remove(p1);
                nPoints.remove(p2);
            }


            Point p1 = nPoints.get(new Random().nextInt(nPoints.size()));
            Point p2 = nPoints.get(new Random().nextInt(nPoints.size()));
            while (p1.equals(p2)) {
                p2 = nPoints.get(new Random().nextInt(nPoints.size()));
            }
            int resX1 = p1.x;
            int resY1 = p1.y;
            int resX2 = p2.x;
            int resY2 = p2.y;
            String response = resX1 + "-" + resY1 + " " + resX2 + "-" + resY2;
            System.out.println(response);
            System.out.flush();

            //0 - записано, 1 - нет
            final String result = scanner.nextLine();
            if (Integer.parseInt(result) == 0) {
                nPoints.remove(p1);
                nPoints.remove(p2);
            }
        }
    }

    //Массив будет состоять из строк вида x-y, x и y это координаты точки
    static String[] readPoints() {
        String points = scanner.nextLine();
        return points.split(";");
    }

    static Point convertStringToPoint(String pointString){
        final String[] split = pointString.split("-");
        return new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    static class Point{
        Integer x;
        Integer y;

        public Point(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != null ? !x.equals(point.x) : point.x != null) return false;
            return y != null ? y.equals(point.y) : point.y == null;
        }

        @Override
        public int hashCode() {
            int result = x != null ? x.hashCode() : 0;
            result = 31 * result + (y != null ? y.hashCode() : 0);
            return result;
        }
    }
}