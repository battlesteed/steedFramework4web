package steed.util.file;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import steed.domain.BaseDomain;
import steed.domain.BaseUser;
import steed.domain.GlobalParam;
import steed.util.base.DomainUtil;
import steed.util.base.StringUtil;
import steed.util.digest.DigestUtil;
import steed.util.digest.Md5Util;
import steed.util.digest.Sha256Util;

public class AttachmentUtil {
	
	/**
	 * 保存附件
	 * @param file
	 * @param fileName 为null时fileName = file.getName();
	 * @param user 为null时保存到公共目录，否则保存到用户目录
	 * @return 相对于项目部署目录的相对路径
	 * @see #delete(String)
	 */
	public static String save(File file,String fileName,BaseUser user){
		if (fileName == null) {
			fileName = file.getName();
		}
		String path = getPath(user,file,fileName);
		FileUtil.copyFile(file, GlobalParam.FOLDER.rootPath+path);
		return path;
	}
	/**
	 * 删除附件
	 * @param path 相对于项目部署目录的相对路径save的返回值
	 * @see #save(File, String, BaseUser)
	 */
	public static void delete(String path){
		new File(GlobalParam.FOLDER.rootPath+path).delete();
	}
	
	
	private static String getPath(BaseUser user,File file,String fileName) {
		String userPath;
		Date now = new Date();
		if (user != null) {
			String userNickName = Md5Util.Md5Digest(DomainUtil.getDomainId((BaseDomain) user)+"")+"/";
			userPath = Sha256Util.sha256Digest(userNickName).substring(30)+"/";
		}else {
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd/");
			userPath = format.format(now);
		}
		String decodeUrl = DigestUtil.AESAndMd532MinEncode(String.format("%d%d", now.getTime(),new Random().nextInt()));
		return "/upload/"+userPath+"file/"+decodeUrl+FileUtil.getFileSuffix(fileName);
	}
	
}
