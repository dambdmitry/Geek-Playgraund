firstStep = "50"
print(firstStep)
left = 0
right = 100
myShot = int((right + left) / 2)
while True:
    answer = int(input())
    response = ""
    if answer == -1:
        left = myShot
    else:
        right = myShot
    myShot = int((right + left) / 2)
    response = str(myShot)
    print(myShot)