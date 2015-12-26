package com.thoughtworks.easymetro.services;

import android.content.Context;
import android.util.Log;

import com.google.common.util.concurrent.ListenableFuture;
import com.thoughtworks.easymetro.cocurrency.ExecutorUtils;
import com.thoughtworks.easymetro.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by abhisheksrivastava on 12/26/15.
 * To perform time intensive task in a background thread
 */
public class MetroService
{
    private static final String TAG = MetroService.class.getSimpleName();
    public String mPrevLine;
    public int mCost;
    public int mTime;
    public String minFinalOutput;
    public int minTime;
    public int minCost;
    public HashMap<String, String[]> mHashMap = new HashMap<>(50);
    public HashMap<String, String> mIntersectionMap = new HashMap<>(50);

    public MetroService(Context context)
    {
    }

    public ListenableFuture<String> calculateTimeAndFare(final Context context, final String start, final String end, HashMap<String, String> intersectionMap, HashMap<String, String[]> map)
    {
        mHashMap = map;
        mIntersectionMap = intersectionMap;

        ListenableFuture<String> future = ExecutorUtils.getBackgroundPool().submit(new Callable<String>()
        {


            @Override
            public String call() throws Exception
            {
                String commonLine = getCommonLine(start, end);

                if (commonLine != null)
                {
                    // Traverse and cal
                    mCost = 0;
                    mTime = 0;
                    Utility.calculateTimeAndCost(MetroService.this, commonLine, start, end);
                    minFinalOutput = "Take " + commonLine + " line from " + start + " to "
                            + end +
                            "\n Total cost = $" + mCost + "\n" +
                            "Total time = " + mTime + "min"
                            + "\n\nEnjoy your ride!"


                    ;

                } else
                {
                    minTime = 1000000;
                    minCost = 0;
                    String finalOutput = "";
                    minFinalOutput = "";

                    List<String> startLine = Utility.getLine(mHashMap, start);

                    for (int i = 0; i < startLine.size(); i++)
                    {
                        List<String> junctions1 = new ArrayList<String>();
                        junctions1 = Utility.getJunctions(mIntersectionMap, startLine.get(i));

                        junctions1.remove(start);
                        for (int j = 0; j < junctions1.size(); j++)
                        {
                            mCost = 0;
                            mTime = 0;
                            finalOutput = "";
                            Utility.calculateTimeAndCost(MetroService.this, startLine.get(i), start, junctions1.get(j));
                            finalOutput = finalOutput + "Go from " + start + " to " + junctions1.get(j) + " by " + startLine.get(i) + " line \n";
                            String destLine1 = getCommonLine(junctions1.get(j), end);
                            if (destLine1 != null)
                            {
                                Utility.calculateTimeAndCost(MetroService.this, destLine1, junctions1.get(j), end);
                                finalOutput = finalOutput + "Go from " + junctions1.get(j) + " to " + end + " by " + destLine1 + " line \n";
                                Log.i(TAG, "finalOutput = " + finalOutput);
                                if (minTime > mTime)
                                {
                                    minTime = mTime;
                                    minCost = mCost;
                                    minFinalOutput = finalOutput;
                                }
                            } else
                            {

                                List<String> allLines = Utility.getLine(mHashMap, junctions1.get(j));
                                String otherLine = null;
                                for (int a = 0; a < allLines.size(); a++)
                                {
                                    if (allLines.get(a) != startLine.get(i))
                                    {
                                        otherLine = allLines.get(a);
                                    }
                                }
                                List<String> junctions2 = new ArrayList<String>();
                                junctions2 = Utility.getJunctions(mIntersectionMap, otherLine);

                                for (int k = 0; k < junctions2.size(); k++)
                                {
                                    String destLine2 = getCommonLine(junctions2.get(k), end);
                                    if (destLine2 != null)
                                    {
                                        Utility.calculateTimeAndCost(MetroService.this, otherLine, junctions1.get(j), junctions2.get(k));
                                        Utility.calculateTimeAndCost(MetroService.this, destLine2, junctions2.get(k), end);
                                        finalOutput = finalOutput + "Go from " + junctions1.get(j) + " to " + junctions2.get(k) + " by " + otherLine + " line\n";
                                        finalOutput = finalOutput + "Go from " + junctions2.get(k) + " to " + end + " by " + destLine2 + " line\n";
                                        Log.i(TAG, "finalOutput = " + finalOutput);
                                        if (minTime > mTime)
                                        {
                                            minTime = mTime;
                                            minCost = mCost;
                                            minFinalOutput = finalOutput;
                                        }
                                    }
                                }
                            }

                        }

                    }
                    Log.i(TAG, "minTime = " + minTime);
                    Log.i(TAG, "minCost = " + minCost);
                    Log.i(TAG, "minfinalOutput = " + minFinalOutput);
                    minFinalOutput = minFinalOutput + "\nTotal cost $" + minCost + "" +
                            "\nTotal time " + minTime + " min" +
                            "\n\n Enjoy the RIDE!";
                }
                return minFinalOutput;
            }

        });
        return future;

    }

    private String getCommonLine(String start, String dest)
    {

        ArrayList<String> startLine = new ArrayList<>(20);
        ArrayList<String> destLine = new ArrayList<>(20);

        String[] key = mHashMap.keySet().toArray(new String[mHashMap.keySet().size()]);

        String[] tempArray = {};
        for (int i = 0; i < mHashMap.size(); i++)
        {
            tempArray = mHashMap.get(key[i]);

            for (int j = 0; j < tempArray.length; j++)
            {
                String temp = tempArray[j];

                if (temp == start)
                {
                    startLine.add(key[i]);


                } else if (temp == dest)
                {
                    destLine.add(key[i]);

                }
            }

        }

        String commonLine = null;
        for (int i = 0; i < startLine.size(); i++)
        {
            for (int j = 0; j < destLine.size(); j++)
            {
                if (startLine.get(i) == destLine.get(j))
                {
                    commonLine = startLine.get(i);

                }

            }
        }

        return commonLine;
    }

}