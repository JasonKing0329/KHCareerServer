package com.king.mytennis.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.king.mytennis.bean.GdbRespBean;

public class ConnectionServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		GdbRespBean bean = new GdbRespBean();
		bean.setOnline(true);
		resp.getWriter().print(new Gson().toJson(bean));
	}
}
