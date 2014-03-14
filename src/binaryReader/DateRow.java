package binaryReader;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DateRow {

	int id;
	int year;
	int month;
	int day;
	
	String fileName;
	
	public DateRow(String fileName){
		this.fileName = fileName;
		parseParameters(fileName);
	}
	
	private void parseParameters(String fileName){
		try {
			File inFile = new File(fileName);
			
			FileInputStream fis = new FileInputStream(inFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			DataInputStream dataIn = new DataInputStream(bis);
			try {
				int len = dataIn.available();
				byte[] buffer = new byte[len];
				
				dataIn.read(buffer);
				
				//copy int portions and reverse them
				byte[] idArray = new byte[4];
				for(int i = 3; i >= 0 ; i--){
					idArray[i] = buffer[3-i];
				}
				
				byte[] yearArray = new byte[4];
				for(int i = 3; i >= 0 ; i--){
					yearArray[i] = buffer[7-i];
				}
				
				byte[] monthArray = new byte[4];
				for(int i = 3; i >= 0 ; i--){
					monthArray[i] = buffer[11-i];
				}
				
				byte[] dayArray = new byte[4];
				for(int i = 3; i >= 0 ; i--){
					dayArray[i] = buffer[15-i];
				}

				
				ByteBuffer bufArr = ByteBuffer.wrap(idArray);
				this.id = bufArr.getInt();
				
				bufArr = ByteBuffer.wrap(yearArray);
				this.year = bufArr.getInt();
				
				bufArr = ByteBuffer.wrap(monthArray);
				this.month = bufArr.getInt();
				
				bufArr = ByteBuffer.wrap(dayArray);
				this.day = bufArr.getInt();

				System.out.println(fileName +"\t ID: " + this.id + "\t Year: " + this.year + "\t  Month: " + this.month + "\t Day: " + this.day);
				
				fis.close();
				bis.close();
				dataIn.close();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("File not found");
			System.exit(1);
		}
		return;
	}

	public int getId() {
		return id;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public String getFileName() {
		return fileName;
	}
	
	
}
