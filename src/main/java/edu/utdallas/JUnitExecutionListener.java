package edu.utdallas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import edu.utdallas.util.Helper;

public class JUnitExecutionListener extends RunListener {

	//<class name, String<excuted line number>>
	//  // static HashMap<String, HashSet<String>> node = new HashMap<String, HashSet<String>>();
	//   //collect all the result in one list 
	static ArrayList<HashMap> resultList = new ArrayList<HashMap>();
	public ArrayList<String> modified = new ArrayList<String>();


	@Override
	public void testRunStarted(Description description)
			throws Exception{

		//first, get all the filenames under the main/java folder from the test projects
		List<String> results = new ArrayList<String>();

		//call the method to get the list of source file name
		this.getFileNames(System.getProperty("user.dir")+"/src/main/java", results);


		//check if there is a checksum report file, if there is a report file, compare the current result with the previous one
		//if there is no report created, create one

		File f = new File(System.getProperty("user.dir") + "/Checksum-Report.txt");

		if(f.exists() && !f.isDirectory()) { 
			// do something-->compare 
			System.out.println("compare");
			//first, read from the report and store the information into data structure

			ArrayList<String> rName = new ArrayList<String>();
			ArrayList<Long> rCode = new ArrayList<Long>();
			

			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
				String line;
				while ((line = br.readLine()) != null) {

					String[] temp = line.split("&");

					rName.add(temp[0]);

					long number = new Long(temp[1]).longValue();

					rCode.add(number);

				}
			}

			//get the current file information
			checksum s = new checksum();

			//check all the files under the java directory and pass the file name to the checksum method
			System.out.println("size of the result is: "+ results.size());

			for(String result: results){
				s.doChecksum(result);

			}

			if(rCode.size()==s.checksums.size()){

				for(int i =0; i < rCode.size();i++ ){
					if(rCode.get(i) - s.checksums.get(i) != 0){
						
						modified.add(textEdit(rName.get(i)));
					}
				}
			}


			System.out.println("number of class that have been modified: "+modified.get(0));

		}else

		{
			System.out.println("no check sum report, create new one");

			File report = new File(System.getProperty("user.dir") + "/Checksum-Report.txt");

			PrintWriter writer = null;

			try {
				writer = new PrintWriter(report);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			checksum s = new checksum();
		


			//check all the files under the java directory and pass the file name to the checksum method
			for(String result: results){
				s.doChecksum(result);
			}

			if(checksum.fileNames.size()>0 || checksum.checksums.size()>0){	
				for( int i = 0 ; i < checksum.fileNames.size();i++){
					writer.println(checksum.fileNames.get(i)+"&"+checksum.checksums.get(i));
				}
			}


			writer.close();

		} 




	}

	//a method that can get all t he files from a directory
	public void getFileNames(String directoryName,List<String> fileNames ) {
		File directory = new File(directoryName);
		String path = directoryName;

		// get all the files from a directory
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				fileNames.add(directoryName+"/"+file.getName());
			} else if (file.isDirectory()) {
				path = file.getAbsolutePath();
				getFileNames(file.getAbsolutePath(),fileNames);
			}
		}
	}

	
	///Users/Jasmine/Desktop/UTD 2017 Spring/CS 6367 TESTING/Project II/commons-dbutils-trunk/src/main/java/org/apache/commons/dbutils/AbstractQueryRunner.java
	//before we add the string to the modified list, we want to change the format in order to match with our report
	public static String textEdit(String x){
		String[] temp = x.split("/");
		String result = "";
	
		
		int marker = 0;
		ArrayList<String> r = new ArrayList<String>();
		for(int i = 0; i < temp.length;i++){
			if(temp[i].equals("org") ){
				marker = i;
			}
		}
		
		for(int i = marker; i < temp.length; i++){
			
			r.add(temp[i]);
			
		}
	System.out.println("size is : "+ r.size());

	String last = r.get(r.size()-1);
	String fileName = last.split("\\.")[0];
	r.remove(r.size()-1);
	r.add(fileName);
	
		for(String i:r){
			result+=i+".";
		}
		

		result.substring(0,result.length()-1);

		return result.replaceAll("(\\w)\\.(?!\\S)", "$1");
		
	}
	
	
	
	
	
	@Override
	public void testStarted(Description description) throws Exception {



		Helper.testPath = description.getClassName() + ":" + description.getMethodName();
		Helper.hashmap.put(Helper.testPath, new HashSet<String>());
	}

	@Override
	public void testRunFinished(Result result) throws Exception {
		// TODO Auto-generated method stub

		super.testRunFinished(result);

		File report = new File(System.getProperty("user.dir") + "/stmt-cov.txt");
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(report);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Statement coverage information is written to stmt-cov.txt file in the root directory");
		//writer.println("Each Statement that has been covered by the Test is listed below as well as its path and class name for each Methods: ");
	
		//xxx is the report to store all the affected test cases
		HashSet<String> xxx = new HashSet();
		
		
		
		if(modified.size() != 0){
			for(String t: modified ){
				Iterator iter = Helper.hashmap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String key = (String)entry.getKey();
					HashSet<String> val = (HashSet<String>)entry.getValue();

					for (String info : val) {

						if(info.contains(t)){
							int l = key.indexOf(":");
							String newKey = key.substring(0,l);
							xxx.add(newKey);
						}
					}
				}
				
			}
			
		}

		
		for (String s : xxx) {
			writer.println("[Effective Test Class] "+s);
		}
		writer.close();

		System.out.println("Total Executed Lines: " + Helper.totalLines);

	}

}
