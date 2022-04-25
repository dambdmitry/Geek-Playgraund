#include <stdio.h>

int main() {
    int answer;
    int row;
    int column;

    char firstAction[] = "1-1\n";
    fprintf(stdout, firstAction);
    fflush(stdout);
    row = 1;
    column = 1;
    column = column + 1;
    while (1) {
        scanf("%d", &answer);

        if (column > 10) {
            row = row + 1;
            column = 1;
        }
        fprintf(stdout, "%d-%d\n", row, column);
        fflush(stdout);
        column = column + 1;
    }
    return 0;
}
