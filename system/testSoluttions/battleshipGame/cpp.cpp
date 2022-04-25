#include <iostream>

int main() {
    int answer;
    int row;
    int column;
    std::string firstAction;
    std::string response;

    firstAction = "1-1";
    std::cout << firstAction << std::endl;
    row = 1;
    column = 1;
    column = column + 1;
    while (true) {
        std::cin >> answer;
        if (column > 10) {
            row = row + 1;
            column = 1;
        }
        response = std::to_string(row) + "-" + std::to_string(column);
        std::cout << response << std::endl;
        column = column + 1;
    }
    return 0;
}
