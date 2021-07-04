package search;

public class BinarySearchMin {

    public static void main(String[] args) {
//        int num = Integer.parseInt(args[0]);
        int[] arr = stringToIntArr(args);
//        System.out.println(minSearchingIt(arr));
        System.out.println(minSearchingRe(0, arr.length - 1, arr));
    }

    //Pred: arr[] - cyclic shift of a sorted (strictly) ascending array or this is sorted array
    // && after the maximum value is the minimum value in this arr || or this is sorted array
    // && arr - array of integers;
    //Post: R = min(arr)  // minimum of this array;
    public static int minSearchingIt(int[] arr) {
        //Pred: arr[] - cyclic shift of a sorted (strictly) ascending array or this is sorted array
        // && after the maximum value is the minimum value in this arr || or this is sorted array
        // && arr - array of integers;
        int leftId = 0, rightId = arr.length - 1;
        //Post: leftId == 0 && rightId = arr.length - 1 && arr[] - cyclic shift of a sorted (strictly) ascending array
        // && after the maximum value is the minimum value in this arr
        // or this is sorted array && arr - array of integers && leftId < rightId;

        //Pred: leftId == 0 && rightId = arr.length - 1
        // && arr[] - cyclic shift of a sorted (strictly) ascending array or this is sorted array
        // && after the maximum value is the minimum value in this arr
        // && arr - array of integers && leftId < rightId ;
        if (arr.length == 1 || arr[leftId] < arr[rightId]) {
            //Pred: (cyclic shift = 0 && arr - sorted && ar[0] == min(arr)) || (arr.length == 1);
            return arr[leftId];
            //Post: arr[leftId] == min(arr);
        }
        //Post:arr[leftId] >= arr[rightId] && leftId < rightId
        //Inv: arr[] - cyclic shift of a sorted (strictly) ascending array
        // && after the maximum value is the minimum value in this arr && arr[leftId] > arr[rightId] && leftId < rightId
        while (rightId != 1 + leftId) {
            //Pred: arr[] - cyclic shift of a sorted (strictly) ascending array
            int midId = (leftId + rightId) / 2;
            //Post: midId == (leftId + rightId) / 2 && arr[] - cyclic shift of a sorted (strictly) ascending array

            //Pred: midId == (leftId + rightId) / 2 && arr[] - cyclic shift of a sorted (strictly) ascending array
            if (arr[leftId] > arr[midId]) {
                rightId = midId;
            } else {
                leftId = midId;
            }
            //Post: (arr[leftId] > arr[midId] && rightId == midId && arr[leftId] > arr[rightId])
            // || (arr[leftId] <= arr[midId] && arr[leftId] == arr[midId] && leftId == midId)
        }
        //Post: rightId == leftId + 1 && arr[leftId] > arr[leftId + 1] && arr[] - cyclic shift of a sorted (strictly) ascending array
        // && after the maximum value is the minimum value in this arr
        // && arr[leftId] > arr[rightId] && arr[rightId] = min(arr);

        return arr[rightId];
    }


    //Pred: leftId >= 0 && rightId < arr.length
    // && arr[] - cyclic shift of a sorted (strictly) ascending array && after the maximum value is the minimum value in this arr
    // or this is sorted array
    //&& arr - array of integers && leftId < rightId;

    //Post: R = min(arr)  // minimum of this array;
    public static int minSearchingRe(int leftId, int rightId, int[] arr) {
        //Pred: leftId >= 0 && rightId < arr.length
        // && arr[] - cyclic shift of a sorted (strictly) ascending array && after the maximum value is the minimum value in this arr
        // or this is sorted array
        // && arr - array of integers && leftId < rightId;
        if (arr[leftId] < arr[rightId] || arr.length == 1) {
            //Pred: (cyclic shift = 0 && arr - sorted && ar[0] == min(arr)) || (arr.length == 1);
            return arr[leftId];
            //Post: arr[leftId] == min(arr);
        }
        //Post:arr[leftId] >= arr[rightId] && leftId < rightId ;

        //Pred: arr[leftId] > arr[rightId] &&
        // && arr[] - cyclic shift of a sorted (strictly) ascending array && after the maximum value is the minimum value in this arr
        // && arr - array of integers && leftId < rightId;
        if (rightId - leftId != 1) {


            //Pred: arr[leftId] > arr[rightId]
            // && arr[] - cyclic shift of a sorted (strictly) ascending array && after the maximum value is the minimum value in this arr
            // && arr - array of integers;
            int midId = (leftId + rightId) / 2;
            //Post: midId == (leftId + rightId) / 2 && arr[leftId] > arr[rightId]
            // && arr[] - cyclic shift of a sorted (strictly) ascending array && after the maximum value is the minimum value in this arr
            // && arr - array of integers;

            //Pred: midId == (leftId + rightId) / 2 && arr[] - cyclic shift of a sorted (strictly) ascending array;
            if (arr[leftId] > arr[midId]) {
                //arr[leftId] > arr[midId] && rightId == midId && arr[leftId] > arr[rightId] && leftId < rightId;
                return minSearchingRe(leftId, midId, arr);
            } else {
                //arr[leftId] <= arr[midId] && arr[leftId] == arr[midId] && leftId == midId && leftId < rightId;
                return minSearchingRe(midId, rightId, arr);
            }
        }
        //Post: rightId == left + 1 && arr[leftId] > arr[rightId] && arr[leftId] > arr[leftId + 1]
        // && arr[] - cyclic shift of a sorted (strictly) ascending array
        // && after the maximum value is the minimum value in this arr && arr[rightId] = min(arr);
        return arr[rightId];
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

    //Pred: The args array consists of elements of numbers in string
    //Post: outArr - array of integers
    public static int[] stringToIntArr(String[] args) {
        int[] outArr = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            outArr[i] = Integer.parseInt(args[i]);
        }
        return outArr;
    }
}
