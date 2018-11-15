package lists;

public interface MyLinkedList<T> {
    void addLast(T t);
    void addFirst(T t);
    void removeFirst();
    void removeLast();
    MyLinkedListImpl.Node<T> get(int index);
    T remove(MyLinkedListImpl.Node<T> x);

}
