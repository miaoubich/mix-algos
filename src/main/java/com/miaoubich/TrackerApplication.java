package com.miaoubich;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/*
 * Every time somebody comes to the website, we write a record to a log file consisting 
 * of Timestamp, PageId, CustomerId. At the end of the day we have a big log file with 
 * many entries in that format. And for every day we have a new file.
   Now, given two log files (log file from day 1 and log file from day 2,
    we want to generate a list of ‘loyal customers’ that meet the criteria of: 
    (a) they came on both days, and 
    (b) they visited at least two unique pages.
 * */
public class TrackerApplication {

	public static void main(String[] args) {
		// Timestamp, PageId, CustomerId
		String[] s1 = { "", "", "" };
		List<String[]> file1 = List.of(
				new String[] { "t1", "p1", "1" }, 
				new String[] { "t2", "p3", "2" },
				new String[] { "t3", "p2", "1" }, 
				new String[] { "t4", "p3", "1" }, 
				new String[] { "t5", "p1", "3" },
				new String[] { "t6", "p4", "1" },
				new String[] { "t7", "p4", "3" },
				new String[] { "t8", "p4", "4" },
				new String[] { "t9", "p1", "1" });
		//1: [p1, p2, p3, p4]
		List<String[]> file2 = List.of(
				new String[] { "t1", "p1", "1" }, 
				new String[] { "t2", "p3", "2" },
				new String[] { "t2", "p2", "2" }, 
				new String[] { "t2", "p3", "1" }, 
				new String[] { "t2", "p5", "3" });

//		tracker1(file1, file2);
		
		System.out.println("Loyal Customers: " + tracker(file1, file2));
	}

	public static Set<Integer> tracker(List<String[]> file1, List<String[]> file2) {
		Set<Integer> loyalCustomers = new HashSet<>();
		
		Map<Integer, Set<String>> map = file1.stream()							 
				.collect(Collectors.groupingBy(arr->Integer
								.valueOf(arr[2]), Collectors.mapping(arr->arr[1], Collectors.toSet())));
		for(String[] arr: file2) {
			Integer customerId = Integer.valueOf(arr[2]);
			if(map.containsKey(customerId)) {
				Set<String> pages = map.get(customerId);
				pages.add(arr[1]);
				map.put(customerId, pages);
				if(pages.size() >=2)
					loyalCustomers.add(customerId);
			}
			
		}
		return loyalCustomers;
	}
	
	public static void tracker1(List<String[]> file1, List<String[]> file2) {
	    Map<Integer, Set<String>> map = new HashMap<>();
	    Set<Integer> loyalCustomers = new HashSet<>();

	    for (String[] arr : file1) {
	        Integer customerId = Integer.valueOf(arr[2]);
	        
	        // Get the existing set of pages for this customer, or create a new set if it doesn't exist
	        Set<String> pages = map.getOrDefault(customerId, new HashSet<>()); //map(1, Set.of())
	        
	        // Add the current page to the set
	        pages.add(arr[1]);
	        
	        // Put the updated set of pages back into the map
	        map.put(customerId, pages);//map(1, "p1")
	    }
	    
	    map.forEach((k,v)->System.out.println(k+": " + v));
	    
	    for(String[] arr : file2) {
	    	Integer customerId = Integer.valueOf(arr[2]);//2
	    	if(map.containsKey(customerId)) {
	    		Set<String> pages = map.get(customerId);//map(2, {p3})
	    		pages.add(arr[1]);//{p3, p2}
	    		if(pages.size()>=2)
	    			loyalCustomers.add(customerId);
	    	}
	    }
	 // Print the loyal customers
	    System.out.print("Loyal Customers by customerId: ");
	    loyalCustomers.forEach(c->System.out.print(c + ", "));
	}
	
	public static void tracker2(List<String[]> file1, List<String[]> file2) {
	    // Process the log file from the first day
	    Map<Integer, Set<String>> mapDay1 = file1.stream()
	        .collect(Collectors.groupingBy(arr -> Integer.valueOf(arr[2]), 
	               Collectors.mapping(arr -> arr[1], Collectors.toSet())));

	    // Process the log file from the second day
	    Map<Integer, Set<String>> mapDay2 = file2.stream()
	        .collect(Collectors.groupingBy(arr -> Integer.valueOf(arr[2]), 
	               Collectors.mapping(arr -> arr[1], Collectors.toSet())));

	    // Identify the loyal customers
	    Set<Integer> loyalCustomers = mapDay1.entrySet().stream()
	        .filter(entry -> mapDay2.containsKey(entry.getKey()))
	        .map(entry -> {
	            Set<String> allPages = new HashSet<>(entry.getValue());
	            allPages.addAll(mapDay2.get(entry.getKey()));
	            return new AbstractMap.SimpleEntry<>(entry.getKey(), allPages);
	        })
	        .filter(entry -> entry.getValue().size() >= 2)
	        .map(Map.Entry::getKey)
	        .collect(Collectors.toSet());

	    // Print the loyal customers
	    System.out.print("Loyal Customers by customerId: ");
	    loyalCustomers.forEach(c -> System.out.print(c + ", "));
	}




}
