import map.MyHashMap;
import org.junit.Test;



import static org.assertj.core.api.Assertions.assertThat;

public class MyHashMapTest {


    @Test
    public void testPut(){
        MyHashMap<String, String> map = new MyHashMap<>();
        map.put("1", "2");
        assertThat(map.size()).isEqualTo(1);
    }

    @Test
    public void testGet(){
        MyHashMap<String, String> map = new MyHashMap<>();
        map.put("1", "2");
        assertThat(map.get("1")).isEqualTo("2");
    }

    @Test
    public void testRemove(){
        MyHashMap<String, String> map = new MyHashMap<>();
        map.put("1", "2");
        assertThat(map.size()).isEqualTo(1);
        map.remove("1");
        assertThat(map.size()).isEqualTo(0);

    }}
