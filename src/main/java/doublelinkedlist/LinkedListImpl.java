package doublelinkedlist;

public class LinkedListImpl<T> implements LinkedList<T>{
    int size;
    Node<T> first;
    Node<T> last;

    public void addLast(T t) {
        Node<T> e = this.last;
        Node<T> newNode = new Node<T>(e, t,null);
        this.last = newNode;
        if(e == null)
            first = newNode;
        else
            e.nextNode = newNode;
        size++;
    }

    public void addFirst(T t) {
        Node<T> e = this.first;
        Node<T> newNode = new Node<T>(null, t, e);
        this.first = newNode;
        if(e == null)
            last = newNode;
        else
            e.prevNode = newNode;
        size++;
    }

    public void removeFirst() {
        T n = this.first.element;
        Node<T> next = this.first.nextNode;
        this.first.nextNode = null;
        this.first = null;
        this.first = next;
        if(next == null)
            this.last = null;
        else
            this.first.prevNode = null;
    }

    public void removeLast() {
        T n = this.last.element;
        Node<T> prev = this.last.prevNode;
        this.last.prevNode = null;
        this.last = null;
        this.last = prev;
        if(prev == null)
            this.first = null;
        else
            prev.nextNode = null;
    }


    public static class Node<T> {
        T element;
        Node<T> nextNode;
        Node<T> prevNode;

        public Node(Node<T> prevNode, T element, Node<T> nextNode) {
            this.element = element;
            this.nextNode = nextNode;
            this.prevNode = prevNode;
        }
    }
}