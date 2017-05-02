package edu.utdallas.util;

import java.util.HashMap;
import java.util.HashSet;

public class Helper {
	
	public static HashMap<String, HashSet<String>> hashmap = new HashMap<String, HashSet<String>>();
	
	public static String testPath;
	
	public static int totalLines = 0;
	
	public static void addExecutedLine(String className) {
		if (hashmap.containsKey(Helper.testPath)) {
			hashmap.get(Helper.testPath).add(className);
		} 
		/*else {
			hashmap.put(Helper.testPath, new HashSet<String>());
			hashmap.get(Helper.testPath).add(className);
		}*/
		
	}
	
	public static void totalLinesPlus() {
		Helper.totalLines++;
	}
}
