package com.thoughtworks.easymetro.utils;

import com.thoughtworks.easymetro.constants.Constants;
import com.thoughtworks.easymetro.services.MetroService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by abhisheksrivastava on 12/26/15.
 */
public class Utility
{
    public static List<String> getJunctions(HashMap<String, String> mIntersectionMap, String line)
    {

        List<String> junctions = new ArrayList<String>();

        String concStr = line + "Green";
        junctions.add((mIntersectionMap.get(concStr)));
        concStr = line + "Red";
        junctions.add((mIntersectionMap.get(concStr)));
        concStr = line + "Yellow";
        junctions.add((mIntersectionMap.get(concStr)));
        concStr = line + "Black";
        junctions.add((mIntersectionMap.get(concStr)));
        concStr = line + "Blue";
        junctions.add((mIntersectionMap.get(concStr)));


        return junctions;

    }

    // This is the main fucntion which calculates the time and cost
    public static void calculateTimeAndCost(MetroService metroService, String commonLine, String startStn, String destStn)
    {

        int startIndex = 0;
        int destIndex = 0;

        String[] routesArr = metroService.mHashMap.get(commonLine);
        for (int i = 0; i < routesArr.length; i++)
        {
            if (routesArr[i] == startStn)
            {
                startIndex = i;
            }
            if (routesArr[i] == destStn)
            {
                destIndex = i;
            }
        }
        int diff = Math.abs(startIndex - destIndex);
        if (metroService.mPrevLine != commonLine && metroService.mCost != 0)
            metroService.mCost = metroService.mCost + 1;
        metroService.mCost = metroService.mCost + (diff * 1);
        metroService.mTime = metroService.mTime + diff * 5;
        metroService.mPrevLine = commonLine;
    }

    public static List<String> getLine(HashMap<String, String[]> mHashMap, String station)
    {
        List<String> linecolorList = new ArrayList<String>();
        String[] tempArray = {};
        String[] key = {Constants.RED, Constants.GREEN, Constants.BLUE, Constants.BLACK, Constants.YELLOW};

        for (int i = 0; i < mHashMap.size(); i++)
        {
            tempArray = mHashMap.get(key[i]);
            for (int j = 0; j < tempArray.length; j++)
            {
                String temp = tempArray[j];
                if (temp == station)
                {
                    linecolorList.add(key[i]);
                }


            }
        }
        return linecolorList;

    }

    public static String[] getBlueStations()
    {
        return new String[]{"East End",
                "Foot Stand",
                "Football Stadium",
                "City Centre",
                "Peter Park",
                "Maximus",
                "Rocky Street",
                "Boxer Street",
                "Boxing Avenue",
                "West End"};
    }

    public static String[] getYellowStations()
    {
        return new String[]{
                "Green Cross",
                "Orange Street",
                "Silk Board",
                "Snake Park",
                "Morpheus Lane",
                "Little Street",
                "Cricket Grounds"
        };
    }

    public static String[] getRedStations()
    {
        return new String[]{
                "Matrix Stand",
                "Keymakers Lane",
                "Oracle Lane",
                "Boxing Avenue",
                "Cypher Lane",
                "Smith Lane",
                "Morpheus Lane",
                "Trinity Lane",
                "Neo Lane"
        };
    }

    public static String[] getBlackStations()
    {
        return new String[]{
                "East End",
                "Gotham Street",
                "Batman Street",
                "Jokers Street",
                "Hawkins Street",
                "Da Vinci Lane",
                "South Park",
                "Newton Bath Tub",
                "Einstein Lane",
                "Neo Lane"
        };
    }

    public static String[] getGreenStations()
    {
        return new String[]{
                "Sheldon Street",
                "GreenLand",
                "City Centre",
                "Stadium House",
                "Green House",
                "Green Cross",
                "South Pole",
                "South Park"
        };
    }
}
