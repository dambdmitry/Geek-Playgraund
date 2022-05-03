INSERT INTO game_interface VALUES (0, 'RANDOMIZER', 'RANDOMIZE - суть игры заключается в том, что вам загадывают число от 1 до 100 и вам предстоит его угадать за наименьшее количество шагов, чем ваш соперник.
В ответ на вашу попытку отгадать число вы будете получать подсказки о его местоположении - меньше или больше оно, отправленного вами числа.', 'Необходимо изначально отправить ваш первый ход и затем на каждую отправку числа вам будут приходить ответы:
-1 - отправленное число меньше загаданного
1 - отправленное число больше загаданного
0 - отправленное число равно загаданному
Разрешено отправлять только числа от 1 до 100, иначе программа не пройдет проверку.
Придерживайтесь шаблона.');

INSERT INTO game_interface VALUES (1, 'BATTLESHIP', 'BATTLESHIP - традиционный морской бой. Единственное отличие в том, что вам не нужно задумываться о местоположении ваших кораблей. Вам нужно лишь атаковать флот соперника. Кто быстрее уничтожит все корабли соперника, тот и победил.',
                                   'Поле с кораблями расположено на матрице 10 на 10.
Координаты атаки передаются x-y, где x - номер строки, y - номер столбца. Координаты принимают значения от 1 до 10 включительно. Например валидные значения 1-1, 3-4, 10-10.
В ответ же на вашу атаку придет ответ:
0 - мимо
1 - ранил
2 - убил
Придерживайтесь шаблона.');

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

INSERT INTO program_template VALUES (0, 'PYTHON',
                                     'firstStep = ""
print(firstStep)#Первый ход

#Цикл игры
while True:
    answer = int(input()) #Получение ответа от системы

    #Логика игры:

    #Ваш ход
    response = ""
    print(response) #Отправка хода',
                                     0);

INSERT INTO program_template VALUES (1, 'JAVA',
                                     'import java.util.Scanner;

public class Main{
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        String firstAction = "";
        System.out.println(firstAction); //Первый ход
        System.out.flush();
        //Цикл игры
        while (true) {
            int answer = Integer.parseInt(scanner.nextLine()); //Получение ответа от системы

            //Логика игры:

            //Ваш ход
            String response = "";
            System.out.println(response); //Отправка хода
            System.out.flush();
        }
    }
}',
                                     0);

INSERT INTO program_template VALUES (2, 'PASCAL',
                                     'program pas;
var
    firstStep: integer;
    answer: integer;
    response: integer;
begin
    firstStep:= 50;
    writeln(firstStep);
    while (true) do
    begin
        readln(answer);
	//Заполните переменную response
        response := 0;

        writeln(response);
    end;
end.',
                                     0);

INSERT INTO program_template VALUES (3, 'C',
                                     '#include <stdio.h>

int main() {
    int step;
    int answer;
    int response;

    //Первый шаг
    step = 1;
    fprintf(stdout, "%d\n", step);
    fflush(stdout);
    while (1) {
        scanf("%d", &answer);
        //Заполните переменную response
        response = 0;
        fprintf(stdout, "%d\n", response);
        fflush(stdout);
    }
    return 0;
}
',
                                     0);

INSERT INTO program_template VALUES (4, 'CPP',
                                     '#include <iostream>

int main() {
    int step;
    int answer;
    int response;

    //Первый шаг
    step = 0;
    std::cout << step << std::endl;
    while (true) {
        std::cin >> answer;
        // Заполните переменную response
        response = 0;
        std::cout << response << std::endl;
    }
    return 0;
}
',
                                     0);

INSERT INTO program_template VALUES (5, 'PHP',
                                     '<?php
$step = 0;
$answer = 0;
$response = 0;

#Первый шаг
$step = 0;
echo("{$step}\r\n");

while (TRUE) {
    $answer = (integer) readline();
    #Заполните переменную response
    $response = 0;
    echo("{$response}\r\n");
}
?>',
                                     0);

INSERT INTO program_template VALUES (6, 'JAVA',
                                     'import java.util.Scanner;

public class solution2{
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        //Запишите сюда первый ход в формате "строка-столбец"
        String firstAction = "";
        System.out.println(firstAction);
        System.out.flush();
        int row;
        int column;
        while (true) {
            //Ответ на выстрел
            int answer = Integer.parseInt(scanner.nextLine());
            //Заполните переменные row и column

            String response = row + "-" + column;
            System.out.println(response);
            System.out.flush();
        }
    }
}',
                                     1);

INSERT INTO program_template VALUES (7, 'CPP',
                                     '#include <iostream>

int main() {
    int answer;
    int row;
    int column;
    std::string firstAction;
    std::string response;

    //Запишите сюда первый ход в формате "строка-столбец"
    firstAction = "";
    std::cout << firstAction << std::endl;

    while (true) {
        //Ответ на выстрел
        std::cin >> answer;
        //Заполните переменные row и column

        response = std::to_string(row) + "-" + std::to_string(column);
        std::cout << response << std::endl;
    }
    return 0;
}
',
                                     1);

INSERT INTO program_template VALUES (8, 'PYTHON',
                                     '#Запишите сюда первый ход в формате "строка-столбец"
firstStep = ""
print(firstStep)
row = -1
column = -1
while True:
    #Ответ на выстрел
    answer = int(input())
    #Заполните переменные row и column

    response = str(row) + "-" + str(column)
    print(response)',
                                     1);

INSERT INTO program_template VALUES (9, 'PASCAL',
                                     'program pas;
var
    firstAction: string;
    row: integer;
    column: integer;
    answer: integer;
    response: string;
begin
    //Запишите сюда первый ход в формате "строка-столбец"
    firstAction := '''';
    writeln(firstAction);

    while (true) do
    begin
        //Ответ на выстрел
        readln(answer);
        //Заполните переменные row и column

        response := row + ''-'' + column;
        writeln(response);
    end;
end.',
                                     1);

INSERT INTO program_template VALUES (10, 'PHP',
                                     '<?php
$answer = 0;
$response = "";
#Запишите сюда первый ход в формате "строка-столбец"
$firstAction = '''';
echo("{$firstAction}\r\n");
$row = -1;
$column = -1;

while (TRUE) {
    #Ответ на выстрел
    $answer = (integer)readline();
    #Заполните переменные row и column

    $response = "{$row}-{$column}";
    echo("{$response}\r\n");
}
?>',
                                     1);

INSERT INTO program_template VALUES (11, 'C',
                                     '#include <stdio.h>

int main() {
    int answer;
    int row;
    int column;

    //Запишите сюда первый ход в формате "строка-столбец"
    char firstAction[] = "\n";
    fprintf(stdout, firstAction);
    fflush(stdout);

    while (1) {
        //Ответ на выстрел
        scanf("%d", &answer);
        //Заполните переменные row и column


        fprintf(stdout, "%d-%d\n", row, column);
        fflush(stdout);
    }
    return 0;
}
',
                                     1);

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


