package demo.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	
	/**
	 * @param args
	 */
	public static final String separateLine = "\n";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String pathFile = "text.txt";
		File file = new File(pathFile);	
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			StringBuilder dataset = new StringBuilder();
			String line = null;
			
			while((line = bufferedReader.readLine()) != null){
					dataset.append(line);
			}
			String[] header = dataset.toString().split("<NEW_HEADER>");
			System.out.println(header.length);
			System.out.println(header[0]);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("File not found");
		}
	}
}
