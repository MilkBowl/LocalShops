/**
 * 
 * Copyright 2011 MilkBowl (https://github.com/MilkBowl)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */

package net.milkbowl.localshops.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class GenericFunctions {
	// Color Map
	private static final Map<String, ChatColor> COLOR_MAP = new HashMap<String, ChatColor>();
	static {
		COLOR_MAP.put("%AQUA%", ChatColor.AQUA);
		COLOR_MAP.put("%BLACK%", ChatColor.BLACK);
		COLOR_MAP.put("%BLUE%", ChatColor.BLUE);
		COLOR_MAP.put("%DARK_AQUA%", ChatColor.DARK_AQUA);
		COLOR_MAP.put("%DARK_BLUE%", ChatColor.DARK_BLUE);
		COLOR_MAP.put("%DARK_GRAY%", ChatColor.DARK_GRAY);
		COLOR_MAP.put("%DARK_GREEN%", ChatColor.DARK_GREEN);
		COLOR_MAP.put("%DARK_PURPLE%", ChatColor.DARK_PURPLE);
		COLOR_MAP.put("%DARK_RED%", ChatColor.DARK_RED);
		COLOR_MAP.put("%GOLD%", ChatColor.GOLD);
		COLOR_MAP.put("%GRAY%", ChatColor.GRAY);
		COLOR_MAP.put("%GREEN%", ChatColor.GREEN);
		COLOR_MAP.put("%LIGHT_PURPLE%", ChatColor.LIGHT_PURPLE);
		COLOR_MAP.put("%RED%", ChatColor.RED);
		COLOR_MAP.put("%WHITE%", ChatColor.WHITE);
		COLOR_MAP.put("%YELLOW%", ChatColor.YELLOW);
	}
	
	/**
	 * Parses the Color data to the proper chat-color string.
	 * 
	 * @param s
	 * @return
	 */
	public static String parseColors(String s) {
		for(String key : COLOR_MAP.keySet()) {
			s = s.replaceAll(key, COLOR_MAP.get(key).toString());
		}

		return s;
	}
	
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
    
    public static double calculateDistance(Location loc1, Location loc2) {
        return calculateDistance(loc1.getX(), loc1.getY(), loc1.getZ(), loc2.getX(), loc2.getY(), loc2.getZ());
    }

    /**
     * Finds and reduces outliers to the maximum of 2 standard deviations from the population
     * @param list
     * @return
     */
    public static List<Integer> limitOutliers(List<Integer> list) {
        //If we don't have at least 2 - ignore. (was causing all stocks with 1 to get 0d)
        if (list.size() < 2)
            return list;
        
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

        if(list.size() % 2 != 0) {
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
     * @param int volatility
     * @param int deltaStock
     * @return % adjustment as double
     */
    public static double getAdjustment(double volatility, int deltaStock) {
        return (Math.pow((1.0 + (volatility/100000)), -deltaStock));
    }

    /**
     * Calculates size of a cuboid, returns null if larger than provided max width and height,
     * otherwise returns a readable string of the region size.
     * @param xyzA Coordinates (3 elements)
     * @param xyzB Coordinates (3 elements)
     * @param maxWidth Maximum overall width
     * @param maxHeight Maximum overall height
     * @return
     */
    public static String calculateCuboidSize(int[] xyzA, int[] xyzB, int maxWidth, int maxHeight) {
        if (xyzA == null || xyzB == null) {
            return null;
        }

        double width1 = Math.abs(xyzA[0] - xyzB[0]) + 1;
        double height = Math.abs(xyzA[1] - xyzB[1]) + 1;
        double width2 = Math.abs(xyzA[2] - xyzB[2]) + 1;

        String size = "" + width1 + "x" + height + "x" + width2;

        if (width1 > maxWidth || width2 > maxWidth || height > maxHeight || height < 2) {
            return null;
        } else {
            return size;
        }
    }
    
    //Takes 2 Locations constructs xyz array and passed it back to original checker function for testing.
    public static String calculateCuboidSize(Location loc1, Location loc2, int maxWidth, int maxHeight) {
        int[] xyzA = {loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ()};
        int[] xyzB = {loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ()};
        return calculateCuboidSize(xyzA, xyzB, maxWidth, maxHeight);
    }
    
    /**
     * Joins elements of a String array with the glue between them into a String.
     * @param array
     * @param glue
     * @return
     */
    public static String join(String[] array, String glue) {
        String joined = null;
        for (String element : array) {
            if (joined == null) {
                joined = element;
            } else {
                joined += glue + element;
            }
        }
        
        if(joined == null) {
            return "";
        } else {
            return joined;
        }
    }    

    /**
     * Joins elements of a String array with the glue between them into a String.
     * @param list
     * @param glue
     * @return
     */
    public static String join(List<String> list, String glue) {
        String joined = null;
        for (String element : list) {
            if (joined == null) {
                joined = element;
            } else {
                joined += glue + element;
            }
        }
        
        if(joined == null) {
            return "";
        } else {
            return joined;
        }
    }
    
    /**
     * Joins elements of a String array with the glue between them into a String.
     * @param list
     * @param glue
     * @return
     */
    public static String join(Set<String> list, String glue) {
        String joined = null;
        for (String element : list) {
            if (joined == null) {
                joined = element;
            } else {
                joined += glue + element;
            }
        }
        
        if(joined == null) {
            return "";
        } else {
            return joined;
        }
    }
    
    public static String stripVowels(String s) {
    	s = s.replace("a", "");
    	s = s.replace("e", "");
    	s = s.replace("i", "");
    	s = s.replace("o", "");
    	s = s.replace("u", "");
    	return s;
    }
}
