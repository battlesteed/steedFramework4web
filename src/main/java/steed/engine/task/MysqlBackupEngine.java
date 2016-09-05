package steed.engine.task;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import steed.util.base.BaseUtil;
import steed.util.base.DateUtil;
import steed.util.base.PathUtil;
import steed.util.system.SimpleTaskEngine;

public class MysqlBackupEngine extends SimpleTaskEngine{
	private String dumpexePath;
	private String hostIP;
	private String userName;
	private String password;
	private String databaseName;
	private String relativeSavePath;
	private int port = 3306;
	/**
	 * 备份数据文件有效时间(过期的备份数据将被删除),单位秒,负数表示永不过期
	 */
	private long validTime = -1;
	
	/**
     * MySQL数据库导出
     * 
     * @param dumpexePath mysqldump.exe的全路径
     * @param hostIP MySQL数据库所在服务器地址IP
     * @param userName 进入数据库所需要的用户名
     * @param password 进入数据库所需要的密码
     * @param savePath 数据库导出文件保存路径
     * @param databaseName 要导出的数据库名
     * @return 返回true表示导出成功，否则返回false。
     */
    public boolean exportDatabaseTool(String dumpexePath,String hostIP,int port, String userName, String password, String savePath, String databaseName) {
        File saveFile = new File(savePath);
        saveFile.mkdirs();// 创建文件夹
        saveFile.delete();
        
        String command = String.format("%s --opt -h%s --port=%d --user=%s --password=%s --lock-all-tables=true --result-file=%s --default-character-set=utf8 %s",
        		dumpexePath,hostIP,port,userName,password,savePath,databaseName);
        try {
            Process process = Runtime.getRuntime().exec(command);
            if (process.waitFor() == 0) {// 0 表示线程正常终止。
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BaseUtil.getLogger().warn("数据库备份失败,命令行{}",command);
        return false;
    }


	@Override
	public void doTask() {
		String fileName = DateUtil.getStringFormatDate(new Date(), "yyyy-MM-dd-HH-mm-ss")+".sql";
		String relPath = PathUtil.praseRelativePath(relativeSavePath);
		boolean exportDatabaseTool = exportDatabaseTool(dumpexePath, hostIP,port, userName, password, PathUtil.mergePath(relPath, fileName), databaseName);
		if (exportDatabaseTool) {
			BaseUtil.getLogger().debug("数据库备份成功!");
		}
		if (validTime >= 0) {
			deleteExpireBackup(relPath);
		}
	}
	
	private void deleteExpireBackup(String directory){
		File file = new File(directory);
		File[] listFiles = file.listFiles();
		for(File f:listFiles){
			if (!f.isDirectory()) {
				long lastModified = f.lastModified();
				if (new Date().getTime() > lastModified + validTime*1000) {
					BaseUtil.getLogger().info("删除无用数据库备份--->{}",f.getAbsolutePath());
					f.delete();
				}
			}
		}
	}


}
