package map;


public class MyHashMap<K,Z> {
    static final Node<?,?>[] EMPTY_TABLE = {};
    MyHashMap.Node<K,Z>[] table = (MyHashMap.Node<K,Z>[]) EMPTY_TABLE;
    int threshold;
    int size;
    float loadFactor;
    int hashSeed = 0;
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final int ALTERNATIVE_HASHING_THRESHOLD_DEFAULT = Integer.MAX_VALUE;

    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);

        this.loadFactor = loadFactor;
        threshold = initialCapacity;
    }

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * holds values which can't be initialized until after VM is booted.
     */
    private static class Holder {

        /**
         * Table capacity above which to switch to use alternative hashing.
         */
        static final int ALTERNATIVE_HASHING_THRESHOLD;

        static {
            String altThreshold = java.security.AccessController.doPrivileged(
                    new sun.security.action.GetPropertyAction(
                            "jdk.map.althashing.threshold"));

            int threshold;
            try {
                threshold = (null != altThreshold)
                        ? Integer.parseInt(altThreshold)
                        : ALTERNATIVE_HASHING_THRESHOLD_DEFAULT;

                // disable alternative hashing if -1
                if (threshold == -1) {
                    threshold = Integer.MAX_VALUE;
                }

                if (threshold < 0) {
                    throw new IllegalArgumentException("value must be positive integer.");
                }
            } catch(IllegalArgumentException failed) {
                throw new Error("Illegal value for 'jdk.map.althashing.threshold'", failed);
            }

            ALTERNATIVE_HASHING_THRESHOLD = threshold;
        }
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    static int indexFor(int h, int length) {
        return h & (length-1);
    }

    static class Node<K,Z> {
        int hash;
        final K key;
        Z val;
        Node<K,Z> next;

        public Node(int hash, K key, Z val, Node<K, Z> next) {
            this.hash = hash;
            this.key = key;
            this.val = val;
            this.next = next;
        }
        void recordAccess(MyHashMap<K,Z> m) {
        }
        void recordRemoval(MyHashMap<K,Z> m) {
        }

        public final K getKey() {
            return key;
        }

        public final Z getValue() {
            return val;
        }
    }

    void transfer(Node[] newTable, boolean rehash) {
        int newCapacity = newTable.length;
        for (Node<K,Z> e : table) {
            while(null != e) {
                Node<K,Z> next = e.next;
                if (rehash) {
                    e.hash = null == e.key ? 0 : hash(e.key);
                }
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            }
        }
    }

    final boolean initHashSeedAsNeeded(int capacity) {
        boolean currentAltHashing = hashSeed != 0;
        boolean useAltHashing = sun.misc.VM.isBooted() &&
                (capacity >= MyHashMap.Holder.ALTERNATIVE_HASHING_THRESHOLD);
        boolean switching = currentAltHashing ^ useAltHashing;
        if (switching) {
            hashSeed = useAltHashing
                    ? sun.misc.Hashing.randomHashSeed(this)
                    : 0;
        }
        return switching;
    }

    void resize(int newCapacity) {
        MyHashMap.Node[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        MyHashMap.Node[] newTable = new MyHashMap.Node[newCapacity];
        transfer(newTable, initHashSeedAsNeeded(newCapacity));
        table = newTable;
        threshold = (int)Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
    }


    void addNode(int hash, K key, Z value, int bucketIndex) {
        if ((size >= threshold) && (null != table[bucketIndex])) {
            resize(2 * table.length);
            hash = (null != key) ? hash(key) : 0;
            bucketIndex = indexFor(hash, table.length);
        }

        createNode(hash, key, value, bucketIndex);
    }

    void createNode(int hash, K key, Z value, int bucketIndex) {
        MyHashMap.Node<K,Z> e = table[bucketIndex];
        table[bucketIndex] = new MyHashMap.Node<>(hash, key, value, e);
        size++;
    }

    private static int roundUpToPowerOf2(int number) {
        // assert number >= 0 : "number must be non-negative";
        return number >= MAXIMUM_CAPACITY
                ? MAXIMUM_CAPACITY
                : (number > 1) ? Integer.highestOneBit((number - 1) << 1) : 1;
    }

    /**
     * Inflates the table.
     */
    private void inflateTable(int toSize) {
        // Find a power of 2 >= toSize
        int capacity = roundUpToPowerOf2(toSize);

        threshold = (int) Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);
        table = new Node[capacity];
        initHashSeedAsNeeded(capacity);
    }

    /**
     * Offloaded version of put for null keys
     */
    private Z putForNullKey(Z value) {
        for (Node<K,Z> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                Z oldValue = e.val;
                e.val = value;
                e.recordAccess(this);
                return oldValue;
            }
        }
        addNode(0, null, value, 0);
        return null;
    }



    public Z put(K key, Z value) {
        if (table == EMPTY_TABLE) {
            inflateTable(threshold);
        }
        if (key == null)
            return putForNullKey(value);
        int hash = hash(key);
        int i = indexFor(hash, table.length);
        for (Node<K,Z> e = table[i]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                Z oldValue = e.val;
                e.val = value;
                e.recordAccess(this);
                return oldValue;
            }
        }

        addNode(hash, key, value, i);
        return null;
    }

    public int size() {
        return size;
    }

    private Z getForNullKey() {
        if (size == 0) {
            return null;
        }
        for (Node<K,Z> e = table[0]; e != null; e = e.next) {
            if (e.key == null)
                return e.val;
        }
        return null;
    }

    final Node<K,Z> getEntry(Object key) {
        if (size == 0) {
            return null;
        }

        int hash = (key == null) ? 0 : hash(key);
        for (Node<K,Z> e = table[indexFor(hash, table.length)];
             e != null;
             e = e.next) {
            Object k;
            if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                return e;
        }
        return null;
    }


    public Z get(Object key) {
        if (key == null)
            return getForNullKey();
        Node<K,Z> entry = getEntry(key);

        return null == entry ? null : entry.getValue();
    }

    public Z remove(Object key) {
        MyHashMap.Node<K,Z> e = removeEntryForKey(key);
        return (e == null ? null : e.val);
    }

    final Node<K,Z> removeEntryForKey(Object key) {
        if (size == 0) {
            return null;
        }
        int hash = (key == null) ? 0 : hash(key);
        int i = indexFor(hash, table.length);
        Node<K,Z> prev = table[i];
        Node<K,Z> e = prev;


        while (e != null) {
            Node<K,Z> next = e.next;
            Object k;
            if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k)))) {
                size--;
                if (prev == e)
                    table[i] = next;
                else
                    prev.next = next;
                e.recordRemoval(this);
                return e;
            }
            prev = e;
            e = next;
        }

        return e;
    }
}
