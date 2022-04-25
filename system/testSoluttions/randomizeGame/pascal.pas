program pas;
var
    step: string;
    left: integer;
    right: integer;
    shot: integer;
    answer: integer;
    response: integer;
begin
    step := '50';
    writeln(step);
    left := 0;
    right := 100;
    shot := (right + left) div 2;
    while (true) do
    begin
        readln(answer);
        response := 0;
        if answer = -1 then
            left := shot
        else
            right := shot;
        shot := (right + left) div 2;
        response := shot;
        writeln(response);
    end;
end.