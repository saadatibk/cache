import java.util.*;

public class Cache {
    private final Map<String, Integer> cache;
    private final Queue<String> keys;
    private final int capacity; 

    public Cache(int capacity) {
        this.cache = new HashMap<>();
        this.keys = new LinkedList<>();
        this.capacity = capacity;
    }

    public void put(String key, int value) {
        if (cache.containsKey(key)) {
            cache.put(key, value); 
        } else {
            if (cache.size() >= capacity) {
                String oldestKey = keys.poll();
                cache.remove(oldestKey);
            }
            cache.put(key, value);
            keys.add(key);
        }
    }

    public int get(String key) {
        return cache.getOrDefault(key, -1); // Returns -1 if the key is not found
    }

    public void remove(String key) {
        if (cache.containsKey(key)) {
            cache.remove(key);
            keys.remove(key);
        }
    }

    public void clear() {
        cache.clear();
        keys.clear();
    }

    public int size() {
        return cache.size();
    }

    public static void main(String[] args) {
        Cache cache = new Cache(3);

        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);

        System.out.println(cache.get("a")); 

        cache.put("d", 4);

        System.out.println(cache.get("a")); 
        System.out.println(cache.get("b")); 

        cache.remove("b");
        System.out.println(cache.get("b"));
        
        cache.put("e", 5);
        System.out.println(cache.size()); 
    }
    /
}
