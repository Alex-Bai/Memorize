package com.bcgkyy.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.bcgkyy.db.DBManagement;
import com.bcgkyy.demo.NumberImage;
import com.bcgkyy.images.ImageMainView;


public class ImageMainViewTest {
	
	private ImageMainView imageMainView = new ImageMainView("imageNumber.xlsx", "imageMapping");	
	
	@Test
	public void readImagesFromDBTest() {
		List<NumberImage> numberImages = imageMainView.readImagesFromDB();
		for(NumberImage numberImage : numberImages) {			
			System.out.println("imageUrls: " + numberImage.toString());
		}
	}
	
	
	@Test
	public void readImageInfor() {
		String imageFolderPath = "/Users/Alex/Downloads/images/";
		DBManagement dbManagement = new DBManagement("imageNumber.xlsx", "imageMapping");
		String outputFolderPath = "img/";				
		File imageFolder = new File(imageFolderPath);
		Map<String, List<String>> imageMaps = new HashMap<String, List<String>>();
		for(File file : imageFolder.listFiles()) {
			String fileName = file.getName();			
			if(fileName.contains("png")) {
				String number = file.getName().split("\\.")[0];				
				if(number.contains("-")) {
					number = number.split("-", 2)[0];
				}
				String imageUrl = outputFolderPath+fileName;
				List<String> urls = imageMaps.getOrDefault(number, new ArrayList<String>());
				urls.add(imageUrl);	
				imageMaps.put(number, urls);
			}			
		}
		for(String number : imageMaps.keySet()) {
			String[] record = new String[] {number,String.join(",", imageMaps.get(number))};
			try {
				dbManagement.insertRecord(record);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
