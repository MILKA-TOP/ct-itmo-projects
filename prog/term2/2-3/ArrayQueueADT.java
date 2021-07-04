package queue;

import java.util.Arrays;

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

public class ArrayQueueADT {

    private int front = 0, size = 0;
    private Object[] elements = new Object[5];


    //Pred: size() > 0 && queue != null
    //Post: elements[front]
    public static Object element(ArrayQueueADT queue) {
        assert queue != null;
        assert size(queue) > 0;

        return queue.elements[queue.front];
    }


    //Pred: queue != null
    //Post: size
    public static int size(ArrayQueueADT queue) {
        assert queue != null;
        return queue.size;
    }

    //Pred: queue != null
    //Post: R == (size() == 0)
    public static boolean isEmpty(ArrayQueueADT queue) {
        assert queue != null;
        return size(queue)  == 0;
    }

    //Pred: queue != null
    //Post: forall i = 1...n: a[i] = null && size == 0 && front == 0
    public static void clear(ArrayQueueADT queue) {
        assert queue != null;

        Arrays.fill(queue.elements, null);
        queue.front = 0;
        queue.size = 0;
    }

    //Pred: element != null && size() < elements.length && queue != null
    //Post: (size = size' + 1) &&  (elements[(front + size) % elements.length] = element;);
    public static void enqueue(ArrayQueueADT queue, Object element) {
        assert queue != null;
        assert element != null;

        queue.elements[(queue.front + queue.size) % queue.elements.length] = element;
        queue.size++;
        if (size(queue) == queue.elements.length) {
            ensureCapacity(queue, queue.elements.length);
        }
    }

    //Pred: capacity == ℤ && queue != null
    //Post: elements.length = (elements.length)' * 2 || (front' >= (front' + size') % capacity && front == 0)
    public static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        assert queue != null;

        if (queue.front < (queue.front + queue.size) % queue.elements.length) {
            queue.elements = Arrays.copyOf(queue.elements, 2 * capacity);
        } else {
            Object[] frontArray = Arrays.copyOfRange(queue.elements, queue.front, queue.elements.length);
            Object[] rearArray = Arrays.copyOfRange(queue.elements, 0, queue.front);
            queue.elements = Arrays.copyOf(frontArray, 2 * capacity);
            System.arraycopy(rearArray, 0, queue.elements, frontArray.length, rearArray.length);
            Arrays.fill(frontArray, null);
            Arrays.fill(rearArray, null);
        }
        queue.front = 0;
    }

    //Pred: size > 0 && queue != null
    //Post: R = (elements[front'])' && elements[front'] = null && (((size())' == 1 && front == 0)
    // || (size()' > 1 && front = (front' + 1) % elements.length));
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue != null;
        assert size(queue) > 0;
        Object outObject = queue.elements[queue.front];
        queue.elements[queue.front] = null;
        queue.size--;
        queue.front = (queue.front + 1) % queue.elements.length;
        if (queue.size == 0) {
            queue.front = 0;
        }
        return outObject;
    }

    //Pred: index == Z && size() > index && queue != null
    //Post: R = elements[(front + index) % elements.length]
    public static Object get(ArrayQueueADT queue, int index) {
        assert queue != null;
        assert size(queue) > index;

        return queue.elements[(queue.front + index) % queue.elements.length];
    }

    //Pred: index == ℤ && size() > index && element != null && queue != null
    //Post: elements[(front + index) % elements.length] == element;
    public static void set(ArrayQueueADT queue, int index, Object element) {
        assert queue != null;
        assert element != null;
        assert size(queue) > index;

        queue.elements[(queue.front + index) % queue.elements.length] = element;
    }
}
