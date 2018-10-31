import doublelinkedlist.LinkedListImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class LinkedListTest {

    @Test
    public void testAddFirst(){
        LinkedListImpl<String> list = new LinkedListImpl<String>();
        list.addFirst("1");
        list.addFirst("2");
        list.addFirst("3");
        assertThat(list.getSize()).isEqualTo(3);
    }
    @Test
    public void testAddLast(){
        LinkedListImpl<String> list = new LinkedListImpl<String>();
        list.addLast("1");
        list.addLast("2");
        assertThat(list.getSize()).isEqualTo(2);
    }
    @Test
    public void testRemoveFirst(){
    }
}
