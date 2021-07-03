step = int(input())
firstCoutArr = []
polinom = []
tempPolinom = []
secondTempPolinom = []
n = 2**step
for i in range(n):
    toArr, tablePartTemp = input().split()
    firstCoutArr.append(toArr)
    tablePart = int(tablePartTemp)
    tempPolinom.append(tablePart)
polinom.append(tempPolinom[0])
for i in range(1, n): 
    for j in range(len(tempPolinom) - 1):
        secondTempPolinom.append(tempPolinom[j]^tempPolinom[j+1])
    tempPolinom = secondTempPolinom
    secondTempPolinom = []
    polinom.append(tempPolinom[0])
for i in range(n):
    print(firstCoutArr[i] + " " + str(polinom[i]))
