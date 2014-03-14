package binaryReader;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class TimeRow {

	int id;
	int hour;
	int minute;
	
	String fileName;
	
	public TimeRow(String fileName){
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
				
				byte[] hourArray = new byte[4];
				for(int i = 3; i >= 0 ; i--){
					hourArray[i] = buffer[7-i];
				}
				
				byte[] minuteArray = new byte[4];
				for(int i = 3; i >= 0 ; i--){
					minuteArray[i] = buffer[11-i];
				}
				
				
				ByteBuffer bufArr = ByteBuffer.wrap(idArray);
				this.id = bufArr.getInt();
				
				bufArr = ByteBuffer.wrap(hourArray);
				this.hour = bufArr.getInt();
				
				bufArr = ByteBuffer.wrap(minuteArray);
				this.minute = bufArr.getInt();
				

				System.out.println(fileName + "\t ID: " + this.id + "\t Hour: " + this.hour + "\t Minute: " + this.minute);
				
				
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

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public String getFileName() {
		return fileName;
	}
	
	
}
