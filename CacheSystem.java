import java.util.*;

// Cache interface (Prototype pattern)
interface CacheDesignPatterns extends Cloneable {
    void put(String key, int value);
    int get(String key);
    void remove(String key);
    void clear();
    int size();
    boolean containsKey(String key);
    CacheDesignPatterns clone();  

// Concrete cache implementing FIFO with Prototype and Singleton
class FifoCache implements CacheDesignPatterns {
    private static FifoCache instance;  // Singleton instance

    private final Map<String, Integer> cache;
    private final Queue<String> keys;
    private final int capacity;

    // Private constructor for Singleton
    private FifoCache(int capacity) {
        this.cache = new HashMap<>();
        this.keys = new LinkedList<>();
        this.capacity = capacity;
    }

    // Singleton getInstance method
    public static FifoCache getInstance(int capacity) {
        if (instance == null) {
            instance = new FifoCache(capacity);
        }
        return instance;
    }

    @Override
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

    @Override
    public int get(String key) {
        return cache.getOrDefault(key, -1);
    }

    @Override
    public void remove(String key) {
        if (cache.containsKey(key)) {
            cache.remove(key);
            keys.remove(key);
        }
    }

    @Override
    public void clear() {
        cache.clear();
        keys.clear();
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    @Override
    public CacheDesignPatterns clone() {
        try {
            return (CacheDesignPatterns) super.clone();  // Prototype pattern to clone the cache
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

// Cache Builder (Builder Pattern)
class CacheBuilder {
    private int capacity;
    private String evictionPolicy;

    public CacheBuilder setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public CacheBuilder setEvictionPolicy(String evictionPolicy) {
        this.evictionPolicy = evictionPolicy;
        return this;
    }

    // Build method to create cache based on eviction policy
    public FifoCache build() {
        if (evictionPolicy.equalsIgnoreCase("FIFO")) {
            return FifoCache.getInstance(capacity);
        }
        // Add other eviction policies like LRU, LFU here
        throw new IllegalArgumentException("Unsupported eviction policy");
    }
}

// Abstract Factory for creating caches
abstract class CacheFactory {
    public abstract CacheDesignPatterns createCache();
}

// Concrete Factory for creating FIFO cache
class FifoCacheFactory extends CacheFactory {
    private final int capacity;

    public FifoCacheFactory(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public FifoCache createCache() {
        return FifoCache.getInstance(capacity);
    }
}

// Example usage
public class CacheSystem {
    public static void main(String[] args) {
        // Use builder pattern to configure cache
        CacheDesignPatterns cache = new CacheBuilder()
                .setCapacity(3)
                .setEvictionPolicy("FIFO")
                .build();

        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);

        System.out.println(cache.get("a"));  // Output: 1

        cache.put("d", 4);  // Evicts "a"

        System.out.println(cache.get("a"));  // Output: -1
        System.out.println(cache.get("b"));  // Output: 2

        // Use Prototype pattern to clone the cache
        CacheDesignPatterns clonedCache = cache.clone();
        clonedCache.put("e", 5);
        System.out.println(clonedCache.get("b"));  // Output: 2
        System.out.println(clonedCache.get("e"));  // Output: 5

        // Use Factory pattern to create cache
        CacheFactory factory = new FifoCacheFactory(3);
        CacheDesignPatterns factoryCache = factory.createCache();
        factoryCache.put("x", 10);
        System.out.println(factoryCache.get("x"));  // Output: 10
    }
}
}


