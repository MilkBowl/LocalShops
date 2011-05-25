package com.milkbukkit.localshops.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenericFunctions {
    /**
     * Calculates distance between two cartesian points
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     * @return
     */
    public static double calculateDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double distance = Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2) + Math.pow((z1 - z2), 2));
        return distance;
    }
    
    /**
     * Finds and reduces outliers to the maximum of 2 standard deviations from the population
     * @param list
     * @return
     */
    public static List<Integer> limitOutliers(List<Integer> list) {
        double mean = getMean(list);
        double stdDev = getStandardDeviation(list, mean);
        
        int min = (int) Math.round(mean - (stdDev * 2));
        int max = (int) Math.round(mean + (stdDev * 2));
        
        List<Integer> newList = new ArrayList<Integer>();
        for(int i : list) {
            if(i > max) {
                i = max;
            } else if(i < min) {
                i = min;
            }
            
            newList.add(i);
        }
        
        return newList;
    }
    
    /**
     * Finds and removes outliers
     * @param list
     * @return
     */
    public static List<Integer> removeOutliers(List<Integer> list) {
        double mean = getMean(list);
        double stdDev = getStandardDeviation(list, mean);
        
        int min = (int) Math.round(mean - (stdDev * 2));
        int max = (int) Math.round(mean + (stdDev * 2));
        
        List<Integer> newList = new ArrayList<Integer>();
        for(int i : list) {
            if(i > max || i < min) {
                continue;
            }
            
            newList.add(i);
        }
        
        return newList;
    }
    
    /**
     * Gets the median (middle) of a list of integers
     * @param list
     * @return
     */
    public static double getMedian(List<Integer> list) {
        Collections.sort(list);
        
        if(list.size() % 2 == 1) {
            return list.get((list.size()+1)/2-1);
        } else {
            double lower = list.get(list.size()/2-1);
            double upper = list.get(list.size()/2);
            
            return (lower + upper) / 2.0;
        }
    }
    
    /**
     * Gets the mean (average) of a list of integers
     * @param list
     * @return
     */
    public static double getMean(List<Integer> list) {
        double sum = 0;
        for(Integer i : list) {
            sum += i;
        }
        
        return sum / list.size();
    }
    
    /**
     * Gets the standard deviation of a population (given then provided mean)
     * @param list
     * @param mean
     * @return
     */
    public static double getStandardDeviation(List<Integer> list, double mean) {
        double sum = 0;
        
        for(Integer i : list) {
            sum += Math.pow((i - mean), 2);
        }
        
        return Math.pow(sum/(list.size()-1), .5);
    }
    
    /**
     * Calculate sum of a list
     * @param list
     * @return
     */
    public static int getSum(List<Integer> list) {
        int sum = 0;
        
        for(int i : list) {
            sum += i;
        }
        
        return sum;
    }
    
    /**
     * Calculate adjustment based on volatility
     * @param int deltaStock
     * @param int volatility
     * @return % adjustment as double
     */
    public double getAdjustment(int deltaStock, int volatility) {
        return (Math.pow((1 + volatility/10000), -deltaStock));
    }
}
