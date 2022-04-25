firstStep = "1-1"
print(firstStep)
row = 1
column = 1
column += 1
while True:
    answer = int(input())
    if column > 10:
        row += 1
        column = 1

    response = str(row) + "-" + str(column)
    column += 1
    print(response)
