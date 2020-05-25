package com.bgg.main;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Util {

    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order) // ASCENDING = TRUE, DESCENDING ORDER = FALSE
    {

        List<java.util.Map.Entry<String, Integer>> list = new LinkedList<java.util.Map.Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values

        list.sort(new Comparator<java.util.Map.Entry<String, Integer>>()
        {
            public int compare(Entry<String, Integer> o1,
                               Entry<String, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}
