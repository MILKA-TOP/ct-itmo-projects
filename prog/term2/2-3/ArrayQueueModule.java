package queue;


import java.util.Arrays;

public class ArrayQueueModule {

/*Model:

    maxSize = 100000
    i, j ∈ [-1; maxSize)
    i < j: [ai,...aj]
    i > j: [a0,...aj] U [ai,...a(maxSize)]
    i == j == -1: [] - is empty
 */

/*Inv:
    maxSize = 100000
    i, j ∈ [-1; maxSize)
    i < j: forall k = i..(j - 1): a[k] != null
    i > j: forall k = (0..j) U (i..maxSize): a[k] != null
    i == j == -1: forall 0..maxSize: a[k] == null

 */

    private static int front = 0, size = 0;
    private static Object[] elements = new Object[5];


    //Pred: true
    //Post: size
    public static int size() {
        return size;
    }

    //Pred: element != null
    //Post:  (size = size' + 1) && (elements[(front + size) % elements.length] = element)
    //  || ((elements.length)' == (size())' && elements.length = (elements.length)' * 2);
    public static void enqueue(Object element) {
        assert element != null;

        elements[(front + size) % elements.length] = element;


        size++;
        if (size() == elements.length) {
            ensureCapacity(elements.length);
        }
    }

    //Pred: capacity == ℤ
    //Post: elements.length = (elements.length)' * 2 || (front' >= (front' + size') % capacity && front == 0)
    public static void ensureCapacity(int capacity) {
        if (front < (front + size) % capacity) {
            elements = Arrays.copyOf(elements, 2 * capacity);
        } else {
            Object[] frontArray = Arrays.copyOfRange(elements, front, capacity);
            Object[] rearArray = Arrays.copyOfRange(elements, 0, front);
            elements = Arrays.copyOf(frontArray, 2 * capacity);
            System.arraycopy(rearArray, 0, elements, frontArray.length, rearArray.length);
        }


        front = 0;
    }

    //Pred: size > 0;
    //Post: R = (elements[front'])' && elements[front'] = null && (((size())' == 1 && front == 0)
    // || (size()' > 1 && front = (front' + 1) % elements.length));
    public static Object dequeue() {
        assert size() > 0;


        Object outObject = elements[front];
        elements[front] = null;


        size--;
        front = (front + 1) % elements.length;
        if (size == 0) {
            front = 0;
        }
        return outObject;
    }


    //Pred: size() > 0;
    //Post: elements[front]
    public static Object element() {
        assert size() > 0;

        return elements[front];
    }

    //Pred: true
    //Post: R == (size() == 0)
    public static boolean isEmpty() {
        return size() == 0;
    }


    //Pred: index == ℤ && size() > index
    //Post: R = elements[(front + index) % elements.length]
    public static Object get(int index) {
        assert size() > index;

        return elements[(front + index) % elements.length];
    }


    //Pred: index == ℤ && size() > index && element != null
    //Post: elements[(front + index) % elements.length] == element;
    public static void set(int index, Object element) {
        assert element != null;
        assert size() > index;


        elements[(front + index) % elements.length] = element;
    }


    //Pred: true
    //Post: forall i = 1...n: a[i] = null && size == 0 && front == 0
    public static void clear() {

        Arrays.fill(elements, null);
        front = 0;
        size = 0;
    }
}

