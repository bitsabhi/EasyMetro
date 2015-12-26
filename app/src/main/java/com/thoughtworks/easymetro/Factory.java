package com.thoughtworks.easymetro;

import android.content.Context;

import com.thoughtworks.easymetro.services.MetroService;


public class Factory
{
    private static final Object LOCK = new Object();

    private static MetroService sMetroService;

    public static MetroService getMetroService(Context context)
    {
        synchronized (LOCK)
        {
            if (sMetroService == null)
            {
                sMetroService = new MetroService(context);
            }
        }
        return sMetroService;
    }

}
