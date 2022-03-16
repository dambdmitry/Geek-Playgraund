import java.util.Scanner;

public class solution2{
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        //Запишите сюда первый ход
        String firstAction = "1";
        System.out.println(firstAction);
        System.out.flush();

        int myShot = 1;
        while (true) {
            //Ответ на выстрел
            int answer = Integer.parseInt(scanner.nextLine());
            String response = "";
            //Write code
            response = String.valueOf(myShot);
            myShot++;
            System.out.println(response);
            System.out.flush();
        }
    }
}