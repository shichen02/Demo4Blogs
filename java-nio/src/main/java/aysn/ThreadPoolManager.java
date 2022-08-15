package aysn;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * 线程池管理器
 *
 * @author tangsc
 * @date 2022/08/14
 */
@Slf4j
public class ThreadPoolManager {

    private static final ThreadPoolManager instance = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return instance;
    }
    
    private static final int SCHEDULE_CORE_POOL_SIZE
            = 5;

    private static final int CORE_POOL_SIZE
            = 80;

    private static final int MAX_POOL_SIZE
            = 100;

    private static final int KEEP_ALIVE_TIME
            = 10;

    private ThreadPoolManager(){}

    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), task -> {
        Thread thread = new Thread(task);
        thread.setName("module-thread-" + thread.getId());
        return thread;
    });

    private static final ScheduledExecutorService SCHEDULED_EXECUTOR_POOL
            = new ScheduledThreadPoolExecutor(SCHEDULE_CORE_POOL_SIZE, task -> {
        Thread thread = new Thread(task);
        thread.setName("monitor-schedule-check-thread-" + thread.getId());
        return thread;
    });

    public void scheduleAtFix(Runnable task, long initialDelay, long period, TimeUnit unit) {
        SCHEDULED_EXECUTOR_POOL.scheduleAtFixedRate(task, initialDelay, period, unit);
    }

    public void execute(Runnable task) {
        THREAD_POOL.execute(task);
    }

}
