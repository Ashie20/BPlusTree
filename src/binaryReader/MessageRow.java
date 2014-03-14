package binaryReader;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class MessageRow {

	private int id;
	private String text;
	private int userID;
	private int dateID;
	private int timeID;
	private String fileName;
	
	public MessageRow(int id, String text, int userID, int dateID, int timeID){
		this.id = id;
		this.text = text;
		this.userID = userID;
		this.dateID = dateID;
		this.timeID = timeID;
	}
	
	public MessageRow(String fileName){
		this.fileName = fileName;
		parseParameters(fileName);
	}
	
	public void cleanUpString(){
		this.text = text.replaceAll("[^a-zA-Z0-9]", "");
		System.out.println(this.text);
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
				byte[] id = new byte[4];
				for(int i = 3; i >= 0 ; i--){
					id[i] = buffer[3-i];
				}
				
				byte[] text = new byte[1024];
				for(int i = 0; i < 1024 ; i++){
					text[i] = buffer[i+4];
				}
				
				byte[] userID = new byte[4];
				for(int i = 3; i >= 0 ; i--){
					userID[i] = buffer[1031-i];
				}
				
				byte[] dateID = new byte[4];
				for(int i = 3; i >= 0 ; i--){
					dateID[i] = buffer[1035-i];
				}
				
				byte[] timeID = new byte[4];
				for(int i = 3; i >= 0 ; i--){
					timeID[i] = buffer[1039-i];
				}
				
				ByteBuffer bufArr = ByteBuffer.wrap(id);
				this.id = bufArr.getInt();
				
				this.text = new String(text,Charset.forName("UTF-8"));
				for (int x = 0; x < text.length; x++) {
					int ascii = text[x];
					if ((ascii > 0 && ascii < 32) || ascii <0) break;
					//System.out.println(x + ": '" + text[x] + "' is '" + (char)text[x] + "'");
				}
				
				bufArr = ByteBuffer.wrap(userID);
				this.userID = bufArr.getInt();
				
				bufArr = ByteBuffer.wrap(dateID);
				this.dateID = bufArr.getInt();
				
				bufArr = ByteBuffer.wrap(timeID);
				this.timeID = bufArr.getInt();
				
				System.out.println(fileName + "\t ID: "+ this.id + "\t userID:"+this.userID +"\t dateID:"+this.dateID + "\t timeID:"+this.timeID);
				System.out.println("text: " + this.text);
				
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

	public String getText() {
		return text;
	}

	public int getUserID() {
		return userID;
	}

	public int getDateID() {
		return dateID;
	}

	public int getTimeID() {
		return timeID;
	}

	public String getFileName() {
		return fileName;
	}
	
	
}
