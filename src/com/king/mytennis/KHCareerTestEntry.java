package com.king.mytennis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.google.gson.Gson;

public class KHCareerTestEntry {

	public static void main(String[] args) {

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("conf.properties"));
			String path = properties.getProperty("folder_path");
			System.out.println(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
