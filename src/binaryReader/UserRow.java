package binaryReader;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class UserRow {

	int id;
	String name;
	int locationID;
	
	String fileName;
	
	public UserRow(String fileName){
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
				
				byte[] nameArray = new byte[64];
				for(int i = 0; i < 64 ; i++){
					nameArray[i] = buffer[i+4];
				}
				
				byte[] locationIDArray = new byte[4];
				for(int i = 3; i >= 0 ; i--){
					locationIDArray[i] = buffer[67-i];
				}
				
				ByteBuffer bufArr = ByteBuffer.wrap(idArray);
				this.id = bufArr.getInt();
				
				this.name = new String(nameArray,Charset.forName("UTF-8"));
				for (int x = 0; x < nameArray.length; x++) {
					int ascii = nameArray[x];
					if ((ascii > 0 && ascii < 32) || ascii <0) break;
					//System.out.println(x + ": '" + nameArray[x] + "' is '" + (char)nameArray[x] + "'");
				}
				
				
				bufArr = ByteBuffer.wrap(locationIDArray);
				this.locationID = bufArr.getInt();
				
				
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

	public String getName() {
		return name;
	}

	public int getLocationID() {
		return locationID;
	}

	public String getFileName() {
		return fileName;
	}
	
	
	
}
