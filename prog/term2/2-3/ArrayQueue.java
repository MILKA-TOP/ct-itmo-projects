package queue;

import java.util.Arrays;

public class ArrayQueue extends AbstractQueue {

    public int front = 0;
    public Object[] elements = new Object[1];

    @Override
    protected void enqueueImpl(Object element) {
        elements[(front + size) % elements.length] = element;

        if (size() + 1 == elements.length) {
            ensureCapacityImpl();
            front = 0;
        }
    }

    @Override
    protected Integer iterImpl(int index) {
        return (front + index) % elements.length;
    }

    @Override
    protected Object getImpl(int index) {
        return elements[iterImpl(index)];
    }

    @Override
    protected void setImpl(int index, Object element) {
        elements[iterImpl(index)] = element;
    }

    private void ensureCapacityImpl() {
        if (front < (front + size) % elements.length) {
            elements = Arrays.copyOf(elements, 2 * elements.length);
        } else {
            Object[] frontArray = Arrays.copyOfRange(elements, front, elements.length);
            Object[] rearArray = Arrays.copyOfRange(elements, 0, front);
            elements = Arrays.copyOf(frontArray, 2 * elements.length);
            System.arraycopy(rearArray, 0, elements, frontArray.length, rearArray.length);
            Arrays.fill(frontArray, null);
            Arrays.fill(rearArray, null);
        }
    }

    @Override
    protected Object dequeueImpl() {
        Object outObject = elements[front];
        elements[front] = null;
        front = (front + 1) % elements.length;
        if (size() == 0) {
            front = 0;
        }
        return outObject;
    }

    @Override
    protected void clearImpl() {
        Arrays.fill(elements, null);
        front = 0;
    }

    @Override
    protected Object elementImpl() {
        return elements[front];
    }

}
