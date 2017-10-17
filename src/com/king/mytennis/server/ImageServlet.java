package com.king.mytennis.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.king.mytennis.bean.ImageItemBean;
import com.king.mytennis.bean.ImageUrlBean;
import com.king.mytennis.conf.Command;
import com.king.mytennis.conf.Configuration;

public class ImageServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {


		String type = req.getParameter("type");
		if (type != null) {
			ImageUrlBean bean = null;
			if (type.endsWith(Command.TYPE_IMG_PLAYER)) {
				bean = getPlayerImages(req.getParameter("key"));
			}
			else if (type.endsWith(Command.TYPE_IMG_MATCH)) {
				bean = getMatchImages(req.getParameter("key"));
			}
			// 返回的需要设置编码，否则中文会显示乱码
			resp.setHeader("Content-type", "text/html;charset=UTF-8");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().print(new Gson().toJson(bean));
		}
		else {
			resp.getWriter().print("parameter is null");
		}
	}

	private ImageUrlBean getPlayerImages(String key) {
		ImageUrlBean bean = new ImageUrlBean();
		// 对于包含的中文，如果server.xml里没有配置<Connector URIEncoding="UTF-8"，这里就要处理解码
		// 为了支持通过http://xxx/xxx.jpg直接访问图片，还是选择配置了URIEncoding="UTF-8"
//		try {
//			key = new String(key.getBytes("ISO-8859-1"),"UTF-8");
//		} catch (UnsupportedEncodingException e1) {
//			e1.printStackTrace();
//		}
		bean.setKey(key);
		bean.setItemList(new ArrayList<>());
		bean.getItemList().addAll(getPlayerNormalImages(key));
		bean.getItemList().addAll(getPlayerHeadImages(key));
		return bean;
	}

	private List<ImageItemBean> getPlayerNormalImages(String key) {
		return getImages(Command.TYPE_IMG_PLAYER, key, Configuration.getPlayerImagePath(getServletContext()));
	}

	private List<ImageItemBean> getPlayerHeadImages(String key) {
		return getImages(Command.TYPE_IMG_PLAYER_HEAD, key, Configuration.getPlayerHeadImagePath(getServletContext()));
	}

	private ImageUrlBean getMatchImages(String key) {
		ImageUrlBean bean = new ImageUrlBean();
		bean.setKey(key);
		bean.setItemList(new ArrayList<>());
		bean.getItemList().addAll(getImages(Command.TYPE_IMG_MATCH, key, Configuration.getMatchImagePath(getServletContext())));
		return bean;
	}

	private List<ImageItemBean> getImages(String cmdType, String key, String basePath) {
		List<ImageItemBean> itemList = new ArrayList<>();
		File file = new File(Configuration.getBasePath(getServletContext())
				+ basePath);
		File[] files = file.listFiles();
		if (files != null) {
			for (File f:files) {
				// has folder
				if (f.getName().equals(key) && f.isDirectory()) {
					files = f.listFiles();
					for (File img:files) {
						String url = basePath
								+ "/" + f.getName() + "/" + img.getName();
						System.out.println("imgs: " + url);

						ImageItemBean itemBean = new ImageItemBean();
						itemBean.setKey(cmdType);
						itemBean.setUrl(url);
						itemBean.setSize(img.length());
						itemList.add(itemBean);
					}
					break;
				}
				// single image
				else if (f.getName().contains(".")) {
					String name = f.getName().substring(0, f.getName().lastIndexOf("."));

					if (name.equals(key)) {
						String url = basePath
								+ "/" + f.getName();
						System.out.println("single: " + url);

						ImageItemBean itemBean = new ImageItemBean();
						itemBean.setKey(cmdType);
						itemBean.setUrl(url);
						itemBean.setSize(f.length());
						itemList.add(itemBean);
						break;
					}
				}
			}
		}
		return itemList;
	}

}
