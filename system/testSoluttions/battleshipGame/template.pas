program pas;
var
    firstAction: string;
    row: integer;
    column: integer;
    answer: integer;
    response: string;
begin
    //Запишите сюда первый ход в формате "строка-столбец"
    firstAction := '';
    writeln(firstAction);

    while (true) do
    begin
        //Ответ на выстрел
        readln(answer);
        //Заполните переменные row и column

        response := row + '-' + column;
        writeln(response);
    end;
end.