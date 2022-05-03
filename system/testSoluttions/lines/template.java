import java.util.Scanner;

public class test {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        String[] points = readPoints();

        while (true) {
            //Ответ от соперника, если координаты -1, то он пропустил ход
            String[] coordinates = scanner.nextLine().split(" ");
            int x1 = Integer.parseInt(coordinates[0]);
            int y1 = Integer.parseInt(coordinates[1]);
            int x2 = Integer.parseInt(coordinates[2]);
            int y2 = Integer.parseInt(coordinates[3]);
            //Логика игры, запоните переменные координатами точек, из которых хотите соеденить линию
            int resX1 = 0;
            int resY1 = 0;
            int resX2 = 0;
            int resY2 = 0;



            String response = resX1 + "-" + resY1 + " " + resX2 + "-" + resY2;
            System.out.println(response);
            System.out.flush();

            //0 - записано, 1 - нет
            String result = scanner.nextLine();
        }
    }
    //Массив будет состоять из строк вида x-y, x и y это координаты точки
    static String[] readPoints() {
        String points = scanner.nextLine();
        return points.split(";");
    }
}