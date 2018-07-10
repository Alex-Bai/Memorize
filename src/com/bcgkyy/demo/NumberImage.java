package com.bcgkyy.demo;

import java.util.Arrays;
import java.util.List;

public class NumberImage {

	private String number;
	private List<String> urls;
	
	public NumberImage(String number, List<String> urls) {
		this.number = number;
		this.urls = urls;
	}
	
	public NumberImage(String number, String urls) {
		this.number = number;
		this.urls = Arrays.asList(urls.split(","));		
	}	
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(number).append(": [ ");
		boolean isFirstUrl = true;
		for(String url : urls) {
			if(!isFirstUrl) {
				sb.append(", ");
			}
			sb.append(url);
			isFirstUrl = false;
		}
		sb.append(" ]");
		return sb.toString();		
	}
}
