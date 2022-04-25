import java.util.Scanner;

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
}