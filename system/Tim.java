import java.util.Scanner;

public class solution1{
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        //Запишите сюда первый ход
        String firstAction = "50";
        System.out.println(firstAction);
        System.out.flush();
        int left = 0;
        int right = 100;
        int myShot = (right + left) / 2;
        while (true) {
            int answer = Integer.parseInt(scanner.nextLine());
            String response = "";
            //Write code

            //Загаданное число меньше выстрела
            if (answer == -1) {
                left = myShot;
                myShot = (right+left)/2;
                response = String.valueOf(myShot);
            } else {
                right = myShot;
                myShot = (right+left)/2;
                response = String.valueOf(myShot);
            }
            System.out.println(response);
            System.out.flush();
        }
    }
}