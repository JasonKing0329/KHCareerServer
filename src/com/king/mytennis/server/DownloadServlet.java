package com.king.mytennis.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.king.mytennis.conf.Command;
import com.king.mytennis.conf.Configuration;

public class DownloadServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String type = req.getParameter("type");
		if (type != null) {
			File file = getFilePath(type, req);
			if (file != null && file.exists()) {
				resp.setContentType(this.getServletContext().getMimeType(file.getName()));
				resp.addHeader("Content-Disposition", "attachment;filename=" + file.getName());
				resp.setContentLength((int)file.length());

				try {
					FileInputStream fis = new FileInputStream(file);
					OutputStream os = resp.getOutputStream();
					int len = 0;
					byte[] buff = new byte[1024];
					while ((len = fis.read(buff)) > 0) {
						os.write(buff, 0, len);
					}
					os.flush();
					os.close();
					fis.close();

					// 下载完成后
					downloadFinish(type, file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				resp.getWriter().append("Error: file doesn't exist");
			}
		}
	}

	private void downloadFinish(String type, File file) {

	}

	private File getFilePath(String type, HttpServletRequest req) throws UnsupportedEncodingException {
		File file = null;
		if (type.equals(Command.TYPE_APP)) {
			String path = Configuration.getBasePath(getServletContext()) + Configuration.getAppPath(getServletContext());
			path = new File(path).listFiles()[0].getPath();
			file = new File(path);
		}
		else {
			String filePath = req.getParameter("name");
			// 对于包含的中文，如果server.xml里没有配置<Connector URIEncoding="UTF-8"，这里就要处理解码
//    		filePath = new String(filePath.getBytes("ISO-8859-1"), "UTF-8");
			String path = Configuration.getBasePath(getServletContext()) + filePath;
			file = new File(path);
		}
		return file;
	}

}
