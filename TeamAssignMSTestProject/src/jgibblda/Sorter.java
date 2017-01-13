package jgibblda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Sorter {
	
	public HashMap<String, Double> mapSorter(HashMap<String, Double> unsortMap, String order)
	{
		List<Entry<String, Double>> list = new LinkedList<Entry<String, Double>>(unsortMap.entrySet());
		// Sorting the list Dobule on values
		Collections.sort(list, new Comparator<Entry<String, Double>>()
				{
			public int compare(Entry<String, Double> o1,
					Entry<String, Double> o2)
			{
				if (!order.equals("DESC"))
				{
					return o1.getValue().compareTo(o2.getValue());
				}
				else
				{
					return o2.getValue().compareTo(o1.getValue());

				}
			}
				});
		HashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Entry<String, Double> entry : list)
		{
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	public ArrayList<Double> listSorter(ArrayList<Double> unsortedList)
	{
		Collections.sort(unsortedList, new Comparator<Double>() {
		    @Override
		    public int compare(Double c1, Double c2) {
		        return c2.compareTo(c1);
		    }
		});
		return unsortedList;
	}
}
