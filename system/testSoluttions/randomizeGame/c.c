#include <stdio.h>

int main() {
    int step;
    int answer;
    int response;

    step = 1;
    //printf("%d\r\n", step);
    fprintf(stdout, "%d\n", step);
    fflush(stdout);
    step = step + 1;
    while (1) {
        scanf("%d", &answer);
        response = step;
        //printf("%d\r\n", response);
        fprintf(stdout, "%d\n", response);
        fflush(stdout);
        step = step + 1;
    }
    return 0;
}
