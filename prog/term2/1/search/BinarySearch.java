package search;

public class BinarySearch {

    public static void main(String[] args) {
        int num = Integer.parseInt(args[0]);
        int[] arr = stringToIntArr(args);
//        System.out.println(binSearchingIt(num, arr));
        System.out.println(binSearchingRe(num, arr, -1, arr.length));
    }

    //Pred: arr[] - sorted by non-decreasing order && num = ℤ

    //Post: R = index && num >= arr[index] && 0 <= R <= arr.length && R = ℤ
    // && ((R = 0 || num < arr[R - 1])
    public static int binSearchingIt(int num, int[] arr) {
        //Pred: num = ℤ && arr[] - sorted by non-decreasing order
        int leftId = -1, rightId = arr.length;
        //Post: leftId == -1 && rightId ==  arr.length && num = ℤ && arr[] - sorted by non-decreasing order

        //Inv: leftId >= -1 && rightId <= arr.length && num = ℤ && arr[] - sorted by non-decreasing order
        while (leftId - rightId != -1) {
            //Pred: leftId + 1 != rightId
            int midId = (leftId + rightId) / 2;
            //Post: midId = (leftId + rightId) / 2 && leftId + 1 != rightId

            //Pred: -1 < midId < arr.length
            if (arr[midId] > num) {
                leftId = midId;
            } else {
                rightId = midId;
            }
            //Post: (midId == rightId && arr[rightId] <= num) || (midId == leftId && arr[leftId] > num)
        }

        //rightId = ℤ && arr[rightId] <= num && 0 <= rightId <= arr.length
        // leftId >= -1 && arr[rightId] <= num < arr[leftId] && num = ℤ
        // && arr[] - sorted by non-decreasing order && leftId + 1 == rightId
        return rightId;
    }


    //Pred: arr[] - sorted by non-decreasing order && num = ℤ && leftIf >= -1 && rightId <= args.length && leftId < rightId

    //Post: arr[] - sorted by non-decreasing order && num = ℤ
    // && ((midId >= -1 && rightId <= args.length && midId < rightId && arr[midId] > num) ||
    // || (leftId >= -1 && midId <= args.length && leftId < midId && arr[midId] <= num) ||
    // || (leftId - rightId == -1 && R = index && num >= arr[index] && 0 <= R <= arr.length && R = ℤ && (R = 0 || num < arr[R - 1]))
    public static int binSearchingRe(int num, int[] arr, int leftId, int rightId) {
        //Pred: arr[] - sorted by non-decreasing order && num = ℤ && leftIf >= -1 && rightId <= args.length && leftId < rightId
        if (leftId - rightId != -1) {
            //Pred: arr[] - sorted by non-decreasing order && num = ℤ && leftIf >= -1 && rightId <= args.length && leftId < rightId
            int midId = (leftId + rightId) / 2;
            //Post: arr[] - sorted by non-decreasing order && num = ℤ && leftIf >= -1 && rightId <= args.length
            // && leftId < rightId && midId = (leftId + rightId) / 2

            //Pred: -1 < midId < arr.length
            if (arr[midId] > num) {
                //arr[] - sorted by non-decreasing order && num = ℤ && midId >= -1
                // && rightId <= args.length && midId < rightId && arr[midId] > num
                return binSearchingRe(num, arr, midId, rightId);
            } else {
                //arr[] - sorted by non-decreasing order && num = ℤ && leftId >= -1
                // && midId <= args.length && leftId < midId && arr[midId] <= num
                return binSearchingRe(num, arr, leftId, midId);
            }
        }
        return rightId;
    }

    public static int[] stringToIntArr(String[] args) {
        int[] outArr = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            outArr[i - 1] = Integer.parseInt(args[i]);
        }
        return outArr;
    }
}
