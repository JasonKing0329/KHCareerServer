package com.king.mytennis.server;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.king.mytennis.bean.AppCheckBean;
import com.king.mytennis.conf.Command;
import com.king.mytennis.conf.Configuration;

public class CheckServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String type = req.getParameter("type");
		if (type != null) {
			Object bean = null;
			if (type.equals(Command.TYPE_APP)) {
				bean = checkAppUpdate(req.getParameter("version"));
			}
			resp.getWriter().print(new Gson().toJson(bean));
		}
		else {
			resp.getWriter().print("parameter is null");
		}
	}

	private Object checkAppUpdate(String version) {
		AppCheckBean bean = new AppCheckBean();
		String localVersion = Configuration.getAppVersion(getServletContext());
		bean.setAppVersion(localVersion);
		String path = Configuration.getBasePath(getServletContext()) + Configuration.getAppPath(getServletContext());
		try {
			File file = new File(path).listFiles()[0];
			bean.setAppName(file.getName());
			bean.setAppSize(file.length());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("update app not exist");
		}
		
		if (localVersion.compareTo(version) > 0) {
			bean.setAppUpdate(true);
		}
		return bean;
	}
}
