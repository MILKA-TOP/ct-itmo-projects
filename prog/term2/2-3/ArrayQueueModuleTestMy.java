package queue;

public class ArrayQueueModuleTestMy {

    public static void fill(ArrayQueueModule queue) {
        for (int i = 0; i < 15; i++) {
            queue.enqueue(i);
        }
    }

    public static void dump(ArrayQueueModule queue) {
        int originalSize = queue.size();
        while (!queue.isEmpty()) {
            assert originalSize == queue.size();
            assert queue.element().equals(queue.dequeue());
            originalSize--;
        }
    }


    public static void main(String[] args) {
        System.out.println("---- TESTING ArrayQueueModule -----");
        ArrayQueueModule queue = new ArrayQueueModule();

        System.out.println("'ENQUEUE - 1' testing");
        fill(queue);
        assert queue.size() != 0;

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
        assert queue.size() != 0;

        System.out.println("'SET' testing");
        setting(queue);

        System.out.println("'CLEAN' testing");
        cleanning(queue);
        System.out.println("---- COMPLETED TESTING ArrayQueueModule ----");
    }

    private static void cleanning(ArrayQueueModule queue) {
        queue.clear();
        assert queue.size() == 0;
    }

    private static void setting(ArrayQueueModule queue) {
        int elementSet = 1;
        for (int i = 0; i < 15; i++) {
            queue.set(i, elementSet);
        }
        for (int i = 0; i < queue.size(); i++) {
            assert queue.get(i).equals(elementSet);
        }
    }

    private static void isItEmpty(ArrayQueueModule queue) {
        assert (queue.isEmpty() && queue.size() == 0) || (!queue.isEmpty() && queue.size() != 0);
    }

    public static void getting(ArrayQueueModule queue) {
        for (int i = 0; i < queue.size(); i++) {
            assert queue.get(i).equals(i);
        }
    }

}
