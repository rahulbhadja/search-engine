package SearchEngine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.io.FileReader;
import java.io.IOException;

public class Hash {
    // function to save web pages     
	public HashSet<String> savepages (String FileName) {
		HashSet<String> hashset = new HashSet<String>();
	    	 String s = null;
	    	 try
	    	 {
	    		 BufferedReader brd = new BufferedReader(new FileReader(FileName));
	    		 while((s = brd.readLine()) != null)
	    		 {
	    			 hashset.add(s);
	    		 }
	    		 brd.close();
	    	 }
	    	 catch (FileNotFoundException e)
	    	 {
	    		 System.out.println("The file does not exist");
	    	 }
	    	 catch (IOException e)
	    	 {
	    		 System.out.println("Error");
	    	 }
	    	 return hashset;
	     }
}
