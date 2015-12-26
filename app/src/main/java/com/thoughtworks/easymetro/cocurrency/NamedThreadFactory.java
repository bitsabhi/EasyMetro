package com.thoughtworks.easymetro.cocurrency;

import android.support.annotation.NonNull;
import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory
{
    private String mName;

    public NamedThreadFactory(@NonNull String name)
    {
        mName = name;
    }

    @Override
    public Thread newThread(@NonNull Runnable r)
    {
        Thread t =  new Thread(r, mName);
        t.setDaemon(true);
        return t;
    }
}