package com.thoughtworks.easymetro.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.thoughtworks.easymetro.cocurrency.ExecutorUtils;
import com.thoughtworks.easymetro.Factory;
import com.thoughtworks.easymetro.R;
import com.thoughtworks.easymetro.constants.Constants;
import com.thoughtworks.easymetro.utils.Utility;

public class MainActivity extends Activity
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mOutputTv;
    private Button mOutputBtn;
    private Spinner mStartSpinner;
    private Spinner mDestSpinner;
    private List<String> mStationsList = new ArrayList<>(50);
    private HashMap<String, String[]> mHashMap = new HashMap<>(50);
    private HashMap<String, String> mIntersectionMap = new HashMap<>(50);
    private String mDestStn;
    private String mStartingStn;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initViews();

        createMapForLineAndStations();

        getStnListAndSetAdapters();

        createIntersectionMap();

        calculateOutputAndShowOnUi();


    }

    // Uses Guava's Listenable future to perform the time intensive task in a background thread.
    private void calculateOutputAndShowOnUi()
    {
        mOutputBtn.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                final long t1 = System.currentTimeMillis();
                ListenableFuture<String> future = Factory.getMetroService(MainActivity.this).calculateTimeAndFare(MainActivity.this, mStartingStn, mDestStn, mIntersectionMap, mHashMap);
                Futures.addCallback(future, new FutureCallback<String>()
                {
                    @Override
                    public void onSuccess(String result)
                    {
                        mOutputTv.setText(result);
                        long t2 = System.currentTimeMillis();
                        Log.i(TAG, "Calcualation time = " + (t2 - t1)  + " ms");

                    }

                    @Override
                    public void onFailure(Throwable throwable)
                    {
                        Log.i(TAG, throwable.getLocalizedMessage());
                    }
                }, ExecutorUtils.getUIThread());
            }

        });

    }

    private void initViews()
    {
        mOutputTv = (TextView) findViewById(R.id.tv_Output);
        mStartSpinner = (Spinner) findViewById(R.id.startSpn);
        mDestSpinner = (Spinner) findViewById(R.id.destSpn);
        mOutputBtn = (Button) findViewById(R.id.result_btn);
    }

    public void createMapForLineAndStations()
    {
        String[] blueStations = Utility.getBlueStations();

        String[] yellowStations = Utility.getYellowStations();

        String[] redStations = Utility.getRedStations();

        String[] blackStations = Utility.getBlackStations();

        String[] greenStations = Utility.getGreenStations();

        mHashMap.put(Constants.BLUE, blueStations);
        mHashMap.put(Constants.GREEN, greenStations);
        mHashMap.put(Constants.YELLOW, yellowStations);
        mHashMap.put(Constants.RED, redStations);
        mHashMap.put(Constants.BLACK, blackStations);
    }

    public void getStnListAndSetAdapters()
    {
        String[] key = {Constants.RED, Constants.GREEN, Constants.BLUE, Constants.BLACK, Constants.YELLOW};
        if (mStationsList.size() == 0)
        {
            for (int i = 0; i < mHashMap.size(); i++)
            {
                for (int j = 0; j < mHashMap.get(key[i]).length; j++)
                {
                    mStationsList.add(mHashMap.get(key[i])[j]);

                }
            }
        }
        Collections.sort(mStationsList);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, mStationsList);
        mStartSpinner.setAdapter(adapter);
        mDestSpinner.setAdapter(adapter);

        mStartSpinner.setOnItemSelectedListener(new MyItemSelectedListener());
        mDestSpinner.setOnItemSelectedListener(new MyItemSelectedListener());
    }

    public void createIntersectionMap()
    {
        mIntersectionMap.put(Constants.GREEN_BLUE, "City Centre");
        mIntersectionMap.put(Constants.BLUE_GREEN, "City Centre");
        mIntersectionMap.put(Constants.BLACK_BLUE, "East End");
        mIntersectionMap.put(Constants.BLUE_BLACK, "East End");
        mIntersectionMap.put(Constants.GREEN_YELLOW, "Green Cross");
        mIntersectionMap.put(Constants.YELLOW_GREEN, "Green Cross");
        mIntersectionMap.put(Constants.GREEN_BLACK, "South Park");
        mIntersectionMap.put(Constants.BLACK_GREEN, "South Park");
        mIntersectionMap.put(Constants.BLACK_RED, "Neo Lane");
        mIntersectionMap.put(Constants.RED_BLACK, "Neo Lane");
        mIntersectionMap.put(Constants.RED_YELLOW, "Morpheus Lane");
        mIntersectionMap.put(Constants.YELLOW_RED, "Morpheus Lane");
        mIntersectionMap.put(Constants.RED_BLUE, "Boxing Avenue");
        mIntersectionMap.put(Constants.BLUE_RED, "Boxing Avenue");

    }

    public class MyItemSelectedListener implements OnItemSelectedListener
    {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
            mDestStn = (String) mDestSpinner.getSelectedItem();
            mStartingStn = (String) mStartSpinner.getSelectedItem();
            Log.i(TAG, "mStartingStn = " + mStartingStn);
            Log.i(TAG, "mDestStn = " + mDestStn);
        }

        public void onNothingSelected(AdapterView parent)
        {
        }
    }
}
