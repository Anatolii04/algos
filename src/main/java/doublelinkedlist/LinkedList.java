package doublelinkedlist;

public interface LinkedList<T> {
    void addLast(T t);
    void addFirst(T t);
    void removeFirst();
    void removeLast();
    LinkedListImpl.Node<T> get(int index);
    T remove(LinkedListImpl.Node<T> x);

}
