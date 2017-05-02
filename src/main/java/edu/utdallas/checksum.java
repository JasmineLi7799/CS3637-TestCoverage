package edu.utdallas;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

public class checksum {

	public static ArrayList<String> fileNames;
	public static ArrayList<Long> checksums;


	public checksum() {
		
		this.fileNames=new ArrayList<String>();
		this.checksums=new ArrayList<Long>();
		
	}
	
	public static void doChecksum(String fileName) {
		 
        try {

            CheckedInputStream cis = null;
            //long fileSize = 0;
            
            FileInputStream fis = null;
      	  	CRC32 crc = null;
      	
            try {
            	  fis = new FileInputStream(fileName);  
            	  crc = new CRC32();
            	  cis = new CheckedInputStream(fis, crc);
            	  
                
            } catch (FileNotFoundException e) {
                System.err.println("File not found.");
                System.exit(1);
            }
            
            byte[] buf = new byte[128];
            while(cis.read(buf) >= 0) {
            }
            
            long c = cis.getChecksum().getValue();

            
            checksum.fileNames.add(fileName);
            
           checksum.checksums.add(c);
        
            
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        
      //  System.out.println("====== end of one call of check sum =========");
    }
}
