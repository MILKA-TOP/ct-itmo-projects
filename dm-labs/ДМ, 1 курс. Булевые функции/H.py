n = int(input())
start = "((A0|B0)|(A0|B0))"
if n == 1:
    print(start)
else:
    for i in range(1, n):
        start = "((" + start + "|((A" + str(i) + "|A" + str(i) + ")|(B" + str(i) + "|B"+ str(i) + ")))|(A" + str(i) + "|B" + str(i) + "))"
    print(start)