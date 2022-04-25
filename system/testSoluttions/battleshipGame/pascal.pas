program pas;
var
    firstAction: string;
    row: integer;
    column: integer;
    answer: integer;
    response: string;
begin
    firstAction := '1-1';
    writeln(firstAction);
    row := 1;
    column := 1;
    column := column + 1;
    while (true) do
    begin
        readln(answer);
        if column > 10 then begin
            row := row + 1;
            column := 1;
        end;
        response := row + '-' + column;
        writeln(response);
        column := column + 1;
    end;
end.