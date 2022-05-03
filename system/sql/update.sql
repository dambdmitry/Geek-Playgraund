INSERT INTO game_interface VALUES (2, 'LINES', 'LINES. Изначально дано 16 точек на поле 10 на 10. Координаты точек от 0 до 10. Каждый игрок по очереди выбирает две точки и соединяет их отрезком. Отрезок не должен пересекаться с проведенными ранее отрезками. Проигрывает тот, кто не может сделать ход.',
                                   'Сначала вы должны принять все имеющиеся точки. Они передаются как массив строк, где каждая строка имеет шаблон x-y. x и y это координаты точки.
Далее в цикле игры принимаются координаты точек которые задал соперник. Если координаты имеют значения -1, то игрок пропустил ход.
Пропуск хода возможен в двух случаях:
1) Игрок сделал неправильный ход (его отрезок пересекает существующие, либо попытался использовать уже занятые точки)
2) Ваш ход первый
Поэтому лучше проверять, то что вы отправляете.
После отправки ваших точек приходит ответ:
0 - ваш отрезок корректный и он записан
1 - ваш ход пропущен');

INSERT INTO program_template VALUES (12, 'JAVA',
                                     'import java.util.Scanner;

public class test {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        //Запишите сюда первый ход
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
}',
                                     2);

INSERT INTO program_template VALUES (13, 'PYTHON',
                                     'def readPoints():
    points = input()
    return points.split(";")

while True:
    #Ответ от соперника, если координаты -1, то он пропустил ход
    coordinates = input().split(" ")
    x1 = int(coordinates[0])
    y1 = int(coordinates[1])
    x2 = int(coordinates[2])
    y2 = int(coordinates[3])
    #Логика игры, запоните переменные координатами точек, из которых хотите соеденить линию
    resX1 = 0
    resY1 = 0
    resX2 = 0
    resY2 = 0


    response = str(resX1) + "-" + str(resY1) + " " + str(resX2) + "-" + str(resY2)
    print(response)

    #0 - записано, 1 - нет
    result = input()
def readPoints():
    points = input()
    return points.split(";")

while True:
    #Ответ от соперника, если координаты -1, то он пропустил ход
    coordinates = input().split(" ")
    x1 = int(coordinates[0])
    y1 = int(coordinates[1])
    x2 = int(coordinates[2])
    y2 = int(coordinates[3])
    #Логика игры, запоните переменные координатами точек, из которых хотите соеденить линию
    resX1 = 0
    resY1 = 0
    resX2 = 0
    resY2 = 0


    response = str(resX1) + "-" + str(resY1) + " " + str(resX2) + "-" + str(resY2)
    print(response)

    #0 - записано, 1 - нет
    result = input()
',
                                     2);