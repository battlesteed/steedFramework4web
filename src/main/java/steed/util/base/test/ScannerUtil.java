package steed.util.base.test;

import java.util.Scanner;

public class ScannerUtil {
	private boolean loop = true;
	private Scanner scanner;
	public ScannerUtil() {
		scanner = new Scanner(System.in);
	}

	public boolean isLoop() {
		return loop;
	}
	public String scan(){
		String next = scanner.next();
		if ("exit".equals(next)) {
			loop = false;
			scanner.close();
		}
		return next;
	}
	
}
