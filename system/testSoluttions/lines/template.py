def readPoints():
    points = input()
    return points.split(";")

while True:
    #Ответ от соперника, если координаты -1, то он пропустил ход
    coordinates = input().split(" ")
    x1 = int(coordinates[0])
    y1 = int(coordinates[1])
    x2 = int(coordinates[2])
    y2 = int(coordinates[3])
    #Логика игры, запоните переменные координатами точек, из которых хотите соеденить линию
    resX1 = 0
    resY1 = 0
    resX2 = 0
    resY2 = 0


    response = str(resX1) + "-" + str(resY1) + " " + str(resX2) + "-" + str(resY2)
    print(response)

    #0 - записано, 1 - нет
    result = input()
