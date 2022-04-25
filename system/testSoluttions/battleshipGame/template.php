<?php
$answer = 0;
$response = "";
#Запишите сюда первый ход в формате "строка-столбец"
$firstAction = '';
echo("{$firstAction}\r\n");
$row = -1;
$column = -1;

while (TRUE) {
    #Ответ на выстрел
    $answer = (integer)readline();
    #Заполните переменные row и column

    $response = "{$row}-{$column}";
    echo("{$response}\r\n");
}
?>