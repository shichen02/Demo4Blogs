package threadpooltest;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ThreadPoolManager {

    private static final ThreadPoolManager instance = new ThreadPoolManager();

    public static ThreadPoolManager getInstance() {
        return instance;
    }

    private static final long TIMOUT = 2000L;

    private static final int SCHEDULE_CORE_POOL_SIZE = 10;


    private static final int CORE_POOL_SIZE = 10;

    private static final int MAX_POOL_SIZE = 10;

    private static final int KEEP_ALIVE_TIME = 10;

    private ThreadPoolManager() {
    }

    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(), task -> {
        Thread thread = new Thread(task);
        thread.setName("normal-thread-" + thread.getId());
        return thread;
    });

    public static final ScheduledThreadPoolExecutor SCHEDULED_EXECUTOR_POOL
            = new ScheduledThreadPoolExecutor(SCHEDULE_CORE_POOL_SIZE, task -> {
        Thread thread = new Thread(task);
        thread.setName("schedule-thread-" + thread.getId());
        return thread;
    }) {
        /**
         * 重写afterExecute方法
         *
         * @param r the runnable that has completed
         * @param t the exception that caused termination, or null if
         * execution completed normally
         */
        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            //这个是 Execute 提交的时候
            if (t != null) {
                log.error("afterExecute里面获取到异常信息" + t.getMessage());
            }

            //如果r的实际类型是FutureTask 那么是submit提交的，所以可以在里面get到异常
            if (r instanceof FutureTask) {
                try {
                    Future<?> future = (Future<?>) r;
                    future.get();
                } catch (Exception e) {
                    log.error("future里面取执行异常", e);
                }
            }
        }
    };

    static {
        SCHEDULED_EXECUTOR_POOL.setRemoveOnCancelPolicy(true);
    }

    public ScheduledFuture<?> scheduleAtFix(Runnable task, String taskInfo, long initialDelay, long period, TimeUnit unit) {
        RunnableWrapper taskWrapper = new RunnableWrapper(task, taskInfo);
        taskWrapper.setShowWaitLog(true);
        return SCHEDULED_EXECUTOR_POOL.scheduleAtFixedRate(taskWrapper, initialDelay, period, unit);
    }

    public void execute(Runnable task) {
        THREAD_POOL.execute(task);
    }

}