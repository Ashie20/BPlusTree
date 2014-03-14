package binaryReader;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class LocationRow {

	int id;
	String city;
	String state;
	
	String fileName;
	
	public LocationRow(String fileName){
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
				
				byte[] cityArray = new byte[64];
				for(int i = 0; i < 64 ; i++){
					cityArray[i] = buffer[i+4];
				}
				
				byte[] stateArray = new byte[64];
				for(int i = 0; i < 64 ; i++){
					stateArray[i] = buffer[i+68];
				}
				
				ByteBuffer bufArr = ByteBuffer.wrap(idArray);
				this.id = bufArr.getInt();
				
				this.city = new String(cityArray,Charset.forName("UTF-8"));
				for (int x = 0; x < cityArray.length; x++) {
					int ascii = cityArray[x];
					if ((ascii > 0 && ascii < 32) || ascii <0) break;
					//System.out.println(x + ": '" + cityArray[x] + "' is '" + (char)cityArray[x] + "'");
				}
				
				this.state = new String(stateArray,Charset.forName("UTF-8"));
				for (int x = 0; x < stateArray.length; x++) {
					int ascii = stateArray[x];
					if ((ascii > 0 && ascii < 32) || ascii <0) break;
					//System.out.println(x + ": '" + stateArray[x] + "' is '" + (char)stateArray[x] + "'");
				}
				
				System.out.println(fileName + "\t ID: " + this.id + "\t City: "+ this.city + "\t State: "+ this.state);

				
				
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

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getFileName() {
		return fileName;
	}
	
	
}


