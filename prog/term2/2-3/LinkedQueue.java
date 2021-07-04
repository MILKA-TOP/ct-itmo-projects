package queue;

public class LinkedQueue extends AbstractQueue {

    private Node frontNode;
    private Node rearNode;

    @Override
    protected void enqueueImpl(Object element) {
        Node temp = new Node(element, null);
        if (size() == 0) {
            rearNode = temp;
            frontNode = rearNode;
            return;
        }
        rearNode.next = temp;
        rearNode = rearNode.next;
    }

    @Override
    protected Object getImpl(int index) {
        return iterImpl(index).value;
    }

    @Override
    protected void setImpl(int index, Object element) {
        Node temp = iterImpl(index);
        temp.value = element;
    }


    @Override
    protected Object dequeueImpl() {
        Object outObject = frontNode.value;
        frontNode = frontNode.next;
        return outObject;
    }

    @Override
    protected void clearImpl() {
        frontNode.next = null;
        frontNode.value = null;
        rearNode.next = null;
        rearNode.value = null;
    }

    @Override
    protected Object elementImpl() {
        return frontNode.value;
    }

    @Override
    protected Node iterImpl(int index) {
        Node temp = frontNode;
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        return temp;
    }

    private static class Node {
        private Object value;
        private Node next;

        public Node(Object value, Node next) {
            assert value != null;

            this.value = value;
            this.next = next;
        }
    }
}
