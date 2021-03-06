package lists;

public class MyLinkedListImpl<T> implements MyLinkedList<T> {
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

    public int getSize() {
        return size;
    }

    public void addFirst(T t) {
        Node<T> e = this.first;
        Node<T> newNode = new Node<>(null, t, e);
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
        this.first.element = null;
        this.first = next;
        if(next == null)
            this.last = null;
        else
            this.first.prevNode = null;
        size--;
    }

    public void removeLast() {
        T n = this.last.element;
        Node<T> prev = this.last.prevNode;
        this.last.element = null;
        this.last.prevNode = null;
        this.last = prev;
        if(prev == null)
            this.first = null;
        else
            prev.nextNode = null;
        size--;
    }

    public T getElement(int index){
        return get(index).element;
    }

    public Node<T> get(int index) {
        Node x;
        int i;
        if (index < this.size/2) {
            x = this.first;

            for(i = 0; i < index; ++i) {
                x = x.nextNode;
            }

            return x;
        } else {
            x = this.last;

            for(i = this.size - 1; i > index; --i) {
                x = x.prevNode;
            }

            return x;
        }
    }

    public T rm(int index){
        return remove(get(index));
    }

    public T remove(Node<T> x) {
        final T element = x.element;
        final Node<T> next = x.nextNode;
        final Node<T> prev = x.prevNode;


        if (prev == null) {
            this.first = next;
        } else {
            prev.nextNode = next;
            x.prevNode = null;
        }

        if (next == null) {
            this.last = prev;
        } else {
            next.prevNode = prev;
            x.nextNode = null;
        }

        x.element = null;
        size--;
        return element;
    }

    public Node<T> getFirst() {
        return first;
    }

    public Node<T> getLast() {
        return last;
    }

    static class Node<T> {
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
