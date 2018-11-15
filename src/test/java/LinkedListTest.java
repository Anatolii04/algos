import lists.MyLinkedListImpl;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class LinkedListTest {

    @Test
    public void testAddFirst(){
        MyLinkedListImpl<String> list = new MyLinkedListImpl<String>();
        list.addFirst("1");
        list.addFirst("2");
        list.addFirst("3");
        assertThat(list.getSize()).isEqualTo(3);
    }
    @Test
    public void testAddLast(){
        MyLinkedListImpl<String> list = new MyLinkedListImpl<String>();
        list.addLast("1");
        list.addLast("2");
        assertThat(list.getSize()).isEqualTo(2);
    }
    @Test
    public void testAddLasteAndRemoveFirst(){
        MyLinkedListImpl<String> list = new MyLinkedListImpl<>();
        list.addLast("1");
        assertThat(list.getFirst()).isNotNull();
        list.removeFirst();
        assertThat(list.getLast()).isNull();
        assertThat(list.getSize()).isEqualTo(0);
    }
    @Test
    public void testAddFirstAndRemoveLast(){
        MyLinkedListImpl<String> list = new MyLinkedListImpl<>();
        list.addFirst("1");
        assertThat(list.getLast()).isNotNull();
        list.removeLast();
        assertThat(list.getFirst()).isNull();
        assertThat(list.getSize()).isEqualTo(0);
    }

    @Test
    public void testRemove(){
        MyLinkedListImpl<String> list = new MyLinkedListImpl<>();
        list.addFirst("1");
        list.rm(1);
        assertThat(list.getSize()).isEqualTo(0);
    }

    @Test
    public void testGet(){
        MyLinkedListImpl<String> list = new MyLinkedListImpl<>();
        list.addFirst("1");
        list.addFirst("2");
        list.addFirst("3");
        String x = list.getElement(0);
        assertThat("3").isEqualTo(x);
    }
}
