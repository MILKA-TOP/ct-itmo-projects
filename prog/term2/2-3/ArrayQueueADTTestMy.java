package queue;

public class ArrayQueueADTTestMy {

    public static void fill(ArrayQueueADT queue) {
        for (int i = 0; i < 10000; i++) {
            queue.enqueue(queue, i);
        }
    }

    public static void dump(ArrayQueueADT queue) {
        int originalSize = queue.size(queue);
        while (!queue.isEmpty(queue)) {
            assert originalSize == queue.size(queue);
            assert queue.element(queue).equals(queue.dequeue(queue));
            originalSize--;
        }
    }


    public static void main(String[] args) {
        System.out.println("---- TESTING ArrayQueueADT -----");
        ArrayQueueADT queue = new ArrayQueueADT();

        System.out.println("'ENQUEUE - 1' testing");
        fill(queue);
        assert queue.size(queue) != 0;

        System.out.println("'GET' testing");
        getting(queue);

        System.out.println("'ISEMPTY - 1' testing");
        isItEmpty(queue);

        System.out.println("'ELEMENT'+'DEQUEUE' testing");
        dump(queue);

        System.out.println("'ISEMPTY - 2' testing");
        isItEmpty(queue);

        System.out.println("'ENQUEUE - 2' testing");
        fill(queue);
        assert queue.size(queue) != 0;

        System.out.println("'SET' testing");
        setting(queue);

        System.out.println("'CLEAN' testing");
        cleanning(queue);
        System.out.println("---- COMPLETED TESTING ArrayQueueADT ----");
    }

    private static void cleanning(ArrayQueueADT queue) {
        queue.clear(queue);
        assert queue.size(queue) == 0;
    }

    private static void setting(ArrayQueueADT queue) {
        int elementSet = 1;
        for (int i = 0; i < 10000; i++) {
            queue.set(queue, i, elementSet);
        }
        for (int i = 0; i < queue.size(queue); i++) {
            assert queue.get(queue,i).equals(elementSet);
        }
    }

    private static void isItEmpty(ArrayQueueADT queue) {
        assert (queue.isEmpty(queue) && queue.size(queue) == 0) || (!queue.isEmpty(queue) && queue.size(queue) != 0);
    }

    public static void getting(ArrayQueueADT queue) {
        for (int i = 0; i < queue.size(queue); i++) {
            assert queue.get(queue, i).equals(i);
        }
    }
}
