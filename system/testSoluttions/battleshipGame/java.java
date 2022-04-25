import java.util.Scanner;

public class solution2{
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        //Запишите сюда первый ход
        String firstAction = "1-1";
        System.out.println(firstAction);
        System.out.flush();
        int row = 1;
        int column = 1;
        column++;
        while (true) {
            //Ответ на выстрел
            int answer = Integer.parseInt(scanner.nextLine());
            if(column > 10) {
                row++;
                column = 1;
            }
            String response = row + "-" + column;
            //Write code
            System.out.println(response);
            System.out.flush();
            column++;
        }
    }
}