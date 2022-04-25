#include <iostream>

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
