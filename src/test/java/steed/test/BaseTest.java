package steed.test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import steed.ext.domain.user.User;
import steed.util.base.BaseUtil;
import steed.util.base.test.TestUtil;
import steed.util.dao.DaoUtil;
import steed.util.system.TaskUtil;
import steed.util.wechat.AccessTokenUtil;

public class BaseTest {
	@Test
	public void test2(){
		DaoUtil.listAllObj(User.class, Arrays.asList("nickName","name"), Arrays.asList("sex","phoneNumber"));
	}
	
	@Test
	public void test32(){
		new Thread(new Runnable() {
			public void run() {
				AccessTokenUtil.getAccessToken();
			}
		}).start();
		AccessTokenUtil.getAccessToken();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testddd(){
		BaseUtil.out(TestUtil.getTestText().substring(141));
	}
	
	 /**
     * Java代码实现MySQL数据库导出
     * 
     * @author GaoHuanjie
     * @param hostIP MySQL数据库所在服务器地址IP
     * @param userName 进入数据库所需要的用户名
     * @param password 进入数据库所需要的密码
     * @param savePath 数据库导出文件保存路径
     * @param fileName 数据库导出文件文件名
     * @param databaseName 要导出的数据库名
     * @return 返回true表示导出成功，否则返回false。
     */
    public static boolean exportDatabaseTool(String dumpexePath,String hostIP, String userName, String password, String savePath, String databaseName) {
        File saveFile = new File(savePath);
        saveFile.mkdirs();// 创建文件夹
        saveFile.delete();
        
        String command = String.format("%s --opt -h%s --port=3308 --user=%s --password=%s --lock-all-tables=true --result-file=%s --default-character-set=utf8 %s",
        		dumpexePath,hostIP,userName,password,savePath,databaseName);
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("D:\\JspStudy\\MySQL\\bin\\mysqldump.exe").append(" --opt").append(" -h").append(hostIP);
//        stringBuilder.append(" --user=").append(userName) .append(" --password=").append(password).append(" --lock-all-tables=true");
//        stringBuilder.append(" --result-file=").append(savePath + fileName).append(" --default-character-set=utf8 ").append(databaseName);
        try {
        	BaseUtil.out(command);
            Process process = Runtime.getRuntime().exec(command);
            if (process.waitFor() == 0) {// 0 表示线程正常终止。
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
 
    public static void main(String[] args) throws InterruptedException {
    	new TaskUtil().init();
    	Thread.sleep(1000*60*50);
     /*   if (exportDatabaseTool("D:\\JspStudy\\MySQL\\bin\\mysqldump.exe","localhost", "root", "root", "D:\\backupDatabase\\2014-10-134.sql", "test")) {
            System.out.println("数据库备份成功！！！");
        } else {
            System.out.println("数据库备份失败！！！");
        }*/
    }
    
    @Test
    public void testTask(){
    }
	
	@Test
	public void testmysqlBackup(){
		try {
			String dumpPath = "D:\\JspStudy\\MySQL\\bin\\mysqldump.exe";
			String userName = "root";
			String password = "root";
			String hostIP = "localhost";
			String fileName = "D:\\backup.sql";
			StringBuilder stringBuilder = new StringBuilder();
	        stringBuilder.append(dumpPath).append(" --opt").append(" -h ").append(hostIP);
	        stringBuilder.append(" --user=").append(userName) .append(" --password=").append(password).append(" --lock-all-tables=true");
	        stringBuilder.append(" --result-file="+fileName);
	        BaseUtil.out(stringBuilder);
//	        Runtime.getRuntime().exec(stringBuilder.toString());
			Runtime.getRuntime().exec("D:\\JspStudy\\MySQL\\bin\\mysqldump.exe --user=root --password=root --databases test > D:\\backup.sql");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
