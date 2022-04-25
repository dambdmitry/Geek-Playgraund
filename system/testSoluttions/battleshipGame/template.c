#include <stdio.h>

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
