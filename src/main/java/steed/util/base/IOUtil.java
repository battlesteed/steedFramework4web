package steed.util.base;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * io工具类
 * @author 战马
 *
 */
public class IOUtil implements Closeable{
	
	private List<Closeable> waitToClose = new ArrayList<>();
	
	public BufferedReader getBufferedReader(File file,String charsetName) throws FileNotFoundException{
		try {
			FileInputStream in = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(in,charsetName);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			waitToClose.add(bufferedReader);
			return bufferedReader;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public BufferedReader getBufferedReader(String filePath,String charsetName) throws FileNotFoundException{
		return getBufferedReader(new File(filePath), charsetName);
	}
	
/*	public BufferedWriter getBufferedWriter(String path) throws IOException{
		return getBufferedWriter(getFileAndMakeDirs(path));
	}
	
	public BufferedWriter getBufferedWriter(File file) throws IOException{
		FileWriter fileWriter = new FileWriter(file);
		BufferedWriter writer = new BufferedWriter(fileWriter);
		waitToClose.add(writer);
		return writer;
	}*/
	public BufferedWriter getBufferedWriter(File file,String charsetName) throws IOException{
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), charsetName);
		BufferedWriter writer = new BufferedWriter(out);
		waitToClose.add(writer);
		return writer;
	}
	public BufferedWriter getBufferedWriter(String path,String charsetName) throws IOException{
		return getBufferedWriter(getFileAndMakeDirs(path), charsetName);
	}
	
	public BufferedOutputStream getBufferedOutputStream(String path) throws FileNotFoundException {
		return getBufferedOutputStream(getFileAndMakeDirs(path));
	}
	public BufferedOutputStream getBufferedOutputStream(File file) throws FileNotFoundException {
		FileOutputStream outputStream = new FileOutputStream(file);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
		waitToClose.add(bufferedOutputStream);
		return bufferedOutputStream;
	}
	
	public BufferedInputStream getBufferedInputStream(String path) throws FileNotFoundException {
		return getBufferedInputStream(new File(path));
	}
	/**
	 * 获取BufferedInputStream，读取完毕后一定要调用closeInputStream
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public BufferedInputStream getBufferedInputStream(File file) throws FileNotFoundException {
		FileInputStream inputStream = new FileInputStream(file);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		waitToClose.add(bufferedInputStream);
		return bufferedInputStream;
	}
	
	public static void copyFile(String copyedPath,String copyToPath) throws IOException{
		copyFile(new File(copyedPath), getFileAndMakeDirs(copyToPath));
	}
	public static void copyFile(File copyed,String copyToPath) throws IOException{
		copyFile(copyed, getFileAndMakeDirs(copyToPath));
	}
	/**
	 * 复制文件
	 * @param copyed 被复制的文件
	 * @param copyTo 复制到哪里
	 * @throws IOException
	 */
	public static void copyFile(File copyed,File copyTo) throws IOException{
		IOUtil io = new IOUtil();
		try {
			BufferedInputStream in = io.getBufferedInputStream(copyed);
			BufferedOutputStream out = io.getBufferedOutputStream(copyTo);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally {
			io.close();
		}
		
	}
	/**
	 * 把文件中的字符读到string中
	 * @param path 文本路径
	 * @return
	 */
	public static StringBuffer file2StringBuffer(String path,String charsetName){
		return file2StringBuffer(new File(path), charsetName);
	}
	
	/**
	 * saveString
	 * @return
	 */
	public static void saveString(String path,String charsetName,String content){
		saveString(getFileAndMakeDirs(path), charsetName, content);
	}
	/**
	 * @return
	 */
	public static void saveString(File file,String charsetName,String content){
		IOUtil io = new IOUtil();
		try {
			BufferedWriter bufferedWriter = io.getBufferedWriter(file,charsetName);
			bufferedWriter.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			io.close();
		}
		
	}
	
	/**
	 * 把文件中的字符读到string中
	 * @param path 文本路径
	 * @return
	 */
	public static StringBuffer file2StringBuffer(File file,String charsetName){
		StringBuffer sb = new StringBuffer();
		IOUtil io = new IOUtil();
		try {
			BufferedReader bufferedReader = io.getBufferedReader(file,charsetName);
			String temp;
			while ((temp = bufferedReader.readLine()) != null) {
				sb.append(temp);
				sb.append("\n");
			}
			return sb;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			io.close();
		}
	}
	
	/**
	 * 获取file并创建其所有上级目录
	 * @param path
	 * @return
	 */
	public static File getFileAndMakeDirs(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
			file.delete();
		}
		return file;
	}
	@Override
	public void close(){
		Iterator<Closeable> iterator = waitToClose.iterator();
		while (iterator.hasNext()) {
			try {
				Closeable next = iterator.next();
				if (next instanceof Flushable) {
					((Flushable)next).flush();
				}
				next.close();
			} catch (IOException e) {
//				e.printStackTrace();
			}
			iterator.remove();
		}
	}
}
