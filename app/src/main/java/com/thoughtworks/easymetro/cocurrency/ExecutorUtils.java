package com.thoughtworks.easymetro.cocurrency;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ExecutorUtils
{
    private static final int BG_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    private static final String BG_THREAD = "background_thread";

    private static Executor mUIThread = new UiThreadExecutor();
    private static ListeningExecutorService mBackgroundThreadPool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(BG_POOL_SIZE, new NamedThreadFactory(BG_THREAD)));


    public static Executor getUIThread()
    {
        return mUIThread;
    }

    public static ListeningExecutorService getBackgroundPool()
    {
        return mBackgroundThreadPool;
    }

}