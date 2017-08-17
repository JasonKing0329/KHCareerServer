package com.king.mytennis.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;

public class Configuration {
	
	private static String basePath;
	private static final String CONF_NAME = "conf.properties";

	public static String getBasePath(ServletContext servletContext) {
		if (basePath == null) {
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
				basePath = properties.getProperty("folder_path");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return basePath;
	}

	public static String getAppVersion(ServletContext servletContext) {
		String appVersion = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
			appVersion = properties.getProperty("app_version");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return appVersion;
	}

	public static String getGdbDatabaseVersion(ServletContext servletContext) {
		String appVersion = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
			appVersion = properties.getProperty("gdb_db_version");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return appVersion;
	}

	public static String getAppPath(ServletContext servletContext) {
		String path = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
			path = properties.getProperty("app_update_path");

			File file = new File(path);
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				path = files[0].getPath();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String getGdbDatabasePath(ServletContext servletContext) {
		String path = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
			path = properties.getProperty("gdb_db_update_path");

			File file = new File(path);
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				path = files[0].getPath();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String getBaseUrl(ServletContext servletContext) {
		String path = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
			path = properties.getProperty("base_url");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String getUploadPath(ServletContext servletContext) {
		String path = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
			path = properties.getProperty("upload_path");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String getPlayerImagePath(ServletContext servletContext) {
		String path = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
			path = properties.getProperty("player_img_path");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String getPlayerHeadImagePath(ServletContext servletContext) {
		String path = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
			path = properties.getProperty("player_img_head_path");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String getMatchImagePath(ServletContext servletContext) {
		String path = null;
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(servletContext.getRealPath("/") + CONF_NAME));
			path = properties.getProperty("player_img_match");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

}
