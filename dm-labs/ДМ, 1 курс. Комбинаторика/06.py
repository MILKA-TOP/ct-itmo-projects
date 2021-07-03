my_fileR = open("vectors.in", "r")
wr = open("vectors.out", "w")
n = int(my_fileR.read())
a = []
count = 0
for i in range(2 ** n):
    outBool = True
    num = str(bin(i))[2:]
    j = 0
    nowLen = len(num)
    while nowLen < n:
        num = "0" + num
        nowLen += 1
    while j < len(num) - 1:
        if num[j] == num[j + 1] and num[j] == "1":
            outBool = False
            break
        j += 1
    if outBool:
        count += 1
        a.append(num)
wr.write(str(count) + "\n")
print(count)
for i in range(count):
    print(a[i])
    wr.write(a[i] + "\n")
