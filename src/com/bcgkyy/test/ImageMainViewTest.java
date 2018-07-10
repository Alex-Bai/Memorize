package com.bcgkyy.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

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
		String imageFolderPath = "C:/Users/E068043/git/Memorize/img";
		String outputFolderPath = "img/";				
		File imageFolder = new File(imageFolderPath);
		Map<String, List<String>> imageMaps = new HashMap<String, List<String>>();
		for(File file : imageFolder.listFiles()) {
			String fileName = file.getName().split(".")[0];
			String imageUrl = outputFolderPath+"fileName.png";
			List<String> urls = imageMaps.getOrDefault(fileName, new ArrayList<String>());
			urls.add(imageUrl);
		}
	}
	
}
