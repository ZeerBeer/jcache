import core.Cache;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CacheTest {
    public static void main(String[] args) {
        core.def.Cache<Integer, Integer> cache = new Cache<>(10000);
        AtomicInteger atomicInteger = new AtomicInteger(0);

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10,
                20000, 1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(100000),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i < 10000; i++) {
            poolExecutor.execute(() -> cache.put(atomicInteger.incrementAndGet(), atomicInteger.incrementAndGet()));
        }
    }
}
