#include <iostream>

int main() {
    int step;
    int answer;
    int response;

    step = 1;
    std::cout << step << std::endl;
    step = step + 1;
    while (true) {
        std::cin >> answer;
        response = step;
        std::cout << response << std::endl;
        step = step + 1;
    }
    return 0;
}
