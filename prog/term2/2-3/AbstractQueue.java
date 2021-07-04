package queue;

/**/
/*Model:
    [a1, a2, ..., an], n — размер очереди
 */

/*Inv:
    n >= 0;
 */


public abstract class AbstractQueue implements Queue {


    protected int size = 0;


    //Pred: true
    //Post: R = n
    public int size() {
        return size;
    }


    //Pred: true
    //Post: R = (size() == 0)
    public boolean isEmpty() {
        return size() == 0;
    }


    //Pred: element != null
    //Post: n += 1 && an = element
    public void enqueue(Object element) {
        assert element != null;

        enqueueImpl(element);
        size++;

    }

    //Pred: size > 0;
    //Post: R = a1 && a1' = null && forall i = 1..(n-1) a[i] = a[i+1] && n--;
    public Object dequeue() {
        assert size() > 0;
        Object temp = dequeueImpl();
        size--;
        return temp;
    }


    //Pred: size() > 0;
    //Post: R = a1;
    public Object element() {
        assert size() > 0;

        return elementImpl();
    }

    //Pred: true
    //Post: forall i = 1...n': a[i] = null && n == 0
    public void clear() {
        clearImpl();
        size = 0;
    }

    //Pred: size() > index && index >= 0
    //Post: R = a[index]
    public Object get(int index) {
        assert size() > index;
        assert index >= 0;

        return getImpl(index);

    }

    //Pred: size() > index && element != null && index >= 0;
    //Post: a[index] = element;
    public void set(int index, Object element) {
        assert element != null;
        assert size() > index;
        assert index >= 0;

        setImpl(index, element);
    }


    //Pred: true;
    //Post: R = Object[] array; forall i = 1...size: array[i] = get(i);
    public Object[] toArray() {
        Object[] outArray = new Object[size];
        for (int i = 0; i < size; i++) {
            outArray[i] = dequeue();
            enqueue(outArray[i]);
        }
        return outArray;
    }


    protected abstract void enqueueImpl(Object element);

    protected abstract Object iterImpl(int index);

    protected abstract Object getImpl(int index);

    protected abstract void setImpl(int index, Object element);

    protected abstract Object dequeueImpl();

    protected abstract void clearImpl();

    protected abstract Object elementImpl();

}
