package com.app.budometer.helper;


import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;


public class ThreadPool {
    private int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private int CORE_POOL_SIZE = CPU_COUNT;
    private int MAX_POOL_SIZE = 2 * CPU_COUNT + 1;

    private ThreadPoolExecutor THREAD_POOL_EXECUTOR;

    private ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger();

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, "download_task#" + mCount.getAndIncrement());
        }
    };

    private ThreadPool() {
    }

    public static ThreadPool getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final ThreadPool instance = new ThreadPool();
    }

    private ThreadPoolExecutor getThreadPoolExecutor() {
        if (THREAD_POOL_EXECUTOR == null) {
            long KEEP_ALIVE = 10L;
            THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
                    CORE_POOL_SIZE,
                    MAX_POOL_SIZE,
                    KEEP_ALIVE,
                    TimeUnit.SECONDS,
                    new LinkedBlockingDeque<Runnable>(),
                    sThreadFactory);
        }
        return THREAD_POOL_EXECUTOR;
    }

    public void execute(Runnable command) {
        getThreadPoolExecutor().execute(command);
    }
}
