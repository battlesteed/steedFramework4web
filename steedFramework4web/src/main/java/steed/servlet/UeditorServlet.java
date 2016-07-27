package steed.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import steed.domain.BaseDomain;
import steed.domain.BaseUser;
import steed.domain.GlobalParam;
import steed.util.base.DomainUtil;
import steed.util.base.IOUtil;
import steed.util.base.PathUtil;
import steed.util.base.PropertyUtil;
import steed.util.digest.DigestUtil;
import steed.util.digest.Md5Util;
import steed.util.digest.Sha256Util;

import com.baidu.ueditor.ActionEnter;


public class UeditorServlet extends BaseServlet {
	private static final long serialVersionUID = -4349638234580052136L;
	private static Logger logger = LoggerFactory.getLogger(UeditorServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Content-Type" , "text/html");
		String rootPath = GlobalParam.FOLDER.rootPath;
//		BaseUtil.out(rootPath);
		logger.debug("rootPath----->"+rootPath);
		PrintWriter out = resp.getWriter();
		
		String action = req.getParameter("action");
		JSONObject config;
		StringBuffer sb = IOUtil.file2StringBuffer(PathUtil.getClassesPath()+"config.json");
		/*String path = PathUtil.getClassesPath()+"config.json";
//		logger.debug("path--->"+path);
		FileReader fileReader = new FileReader(new File(path));
		BufferedReader reader = new BufferedReader(fileReader);
		StringBuffer sb = new StringBuffer();
		String temp;
		while((temp = reader.readLine())!=null){
			sb.append(temp);
		}
		reader.close();
		fileReader.close();*/
		
		config = new JSONObject(sb.toString().replaceAll("/\\*[\\s\\S]*?\\*/",""));
		BaseUser user = (BaseUser) req.getSession().getAttribute(GlobalParam.attribute.user);
		
		changeConfig(config, user);
		
		logger.debug(config.toString());
		out.write(new ActionEnter(req, rootPath,config).exec());
		out.flush();
	}

	private void changeConfig(JSONObject config, BaseUser user) {
		String userPath;
		Date now = new Date();
		if (user != null) {
			String userNickName = Md5Util.Md5Digest(DomainUtil.getDomainId((BaseDomain) user)+"") +"/";
			userPath = Sha256Util.sha256Digest(userNickName).substring(30)+"/"+userNickName;
		}else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/");
			userPath = format.format(now);
		}
		String decodeUrl = DigestUtil.AESAndMd532MinEncode(String.format("%d%d", now.getTime(),new Random().nextInt()));
		/**
		 * 保存路径
		 */
		config.put("imagePathFormat", "/upload/"+userPath+"image/{yyyy}{mm}{dd}/{time}"+decodeUrl);
		config.put("scrawlPathFormat", "/upload/"+userPath+"image/{yyyy}{mm}{dd}/{time}"+decodeUrl);
		config.put("snapscreenPathFormat", "/upload/"+userPath+"image/{yyyy}{mm}{dd}/{time}"+decodeUrl);
		config.put("videoPathFormat", "/upload/"+userPath+"video/{yyyy}{mm}{dd}/{time}"+decodeUrl);
		config.put("filePathFormat", "/upload/"+userPath+"file/{yyyy}{mm}{dd}/{time}"+decodeUrl);
		config.put("catcherPathFormat", "/upload/"+userPath+"image/{yyyy}{mm}{dd}/{time}"+decodeUrl);
		
		/**
		 * 文件，图片列表
		 */
		config.put("imageManagerListPath", "/upload/"+userPath+"image/");
		config.put("fileManagerListPath", "/upload/"+userPath+"file/");
		
		/**
		 * 资源访问路径前缀
		 */
		String prefix = PropertyUtil.getConfig("site.rootURL") + GlobalParam.FOLDER.contextPath;
		config.put("imageUrlPrefix",prefix);
		config.put("scrawlUrlPrefix",prefix);
		config.put("snapscreenUrlPrefix",prefix);
		config.put("videoUrlPrefix",prefix);
		config.put("fileUrlPrefix",prefix);
		config.put("catcherUrlPrefix",prefix);
		config.put("imageManagerUrlPrefix",prefix);
		config.put("fileManagerUrlPrefix",prefix);
	}
}
