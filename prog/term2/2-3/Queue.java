package queue;


/*Model:
    [a1, a2, ..., an], n — размер очереди
 */

/*Inv:
    n >= 0;
 */


public interface Queue {

    //Pred: element != null
    //Post: n += 1 && an = element
    void enqueue(Object element);

    //Pred: size() > 0;
    //Post: R = a1;
    Object element();

    //Pred: size > 0;
    //Post: R = a1 && a1' = null && forall i = 1..(n-1) a[i] = a[i+1] && n--;
    Object dequeue();

    //Pred: size() > index
    //Post: R = a[index]
    Object get(int index);

    //Pred: size() > index && element != null;
    //Post: a[index] = element;
    void set(int index, Object element);

    //Pred: true
    //Post: R = n
    int size();

    //Pred: true
    //Post: R = (size() == 0)
    boolean isEmpty();

    //Pred: true
    //Post: forall i = 1...n': a[i] = null && n == 0
    void clear();

    //Pred: true;
    //Post: R = Object[] array; forall i = 1...size: array[i] = get(i);
    Object[] toArray();
}
