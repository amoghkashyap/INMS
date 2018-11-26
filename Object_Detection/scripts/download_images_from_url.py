import os

with open("dataset.txt", "r") as ins:
    array = []
    for line in ins:
        array.append(line)

for item in array:
    os.system("wget "+item)
    print(item)


