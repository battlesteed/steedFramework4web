package steed.util.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.util.Hashtable;

/**
 * 查询ip归属地
 * @author 战马
 *
 */
public class QueryIP {

	public IPLocation loc = new IPLocation();

	public RandomAccessFile ipFile = null;

	private static byte REDIRECT_MODE_1 = 0x01;

	private static byte REDIRECT_MODE_2 = 0x02;

	private byte[] buf = new byte[256];

	private byte[] b3 = new byte[3];

	private byte[] b4 = new byte[4];

	public PrintWriter file_out = null;

	/*public static void main(String[] args) throws Exception {
		long begin_time = System.currentTimeMillis();
		QueryIP queryIP = new QueryIP();
		// queryIP.query();
		Hashtable h = queryIP.query2("211.67.81.4");
//		System.out.println("COUNTRY---->"+h.get("COUNTRY"));
//		System.out.println("AREA---->"+h.get("AREA"));
		Set set = h.entrySet();
		Iterator<Entry> i = set.iterator();
		while(i.hasNext()){
			Entry e = i.next();
			System.out.println(e.getKey()+"----->"+e.getValue());
		}
		long end_time = System.currentTimeMillis();
		System.out.println("Total time is " + (end_time - begin_time) + "ms");
	}*/

	public void query() throws Exception {
		ipFile = new RandomAccessFile("QQWry.Dat", "r");
		file_out = new PrintWriter("out.csv");
		long index_begin_off = readLong4(0);
		long index_end_off = readLong4(4);

		System.out.println("index_begin_off =" + index_begin_off);
		System.out.println("index_end_off =" + index_end_off);

		for (long i = index_begin_off; i < index_end_off; i += 7) {
			byte[] ip_start = new byte[4];
			byte[] ip_end = new byte[4];

			ipFile.seek(i);
			ipFile.readFully(ip_start);
			long ip_record_off = readLong3();

			ipFile.seek(ip_record_off);
			ipFile.readFully(ip_end);

			ip_start = toNetOrder(ip_start);
			ip_end = toNetOrder(ip_end);

			IPLocation loc_get = getIPLocation(ip_record_off);
			file_out.println(getDotedIP(ip_start) + "," + getDotedIP(ip_end)
					+ "," + loc_get.country + "," + loc_get.area);
		}

		// long begin_off = readLong3(index_end_off+4);

		// IPLocation test_loc = getIPLocation(begin_off);
		// System.out.println("COUNTRY=" +test_loc.country);
		// System.out.println("AREA=" +test_loc.area);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Hashtable query2(String dotted_ip) throws Exception {
		Hashtable ret_hash = new Hashtable();

		byte[] ip_array = new byte[4];
		ip_array = InetAddress.getByName(dotted_ip).getAddress();

		ipFile = new RandomAccessFile(QueryIP.class.getResource("/qqwry.dat").getPath(), "r");
		// file_out = new PrintWriter("out.csv");
		long index_begin_off = readLong4(0);
		long index_end_off = readLong4(4);

		// System.out.println("index_begin_off ="+index_begin_off);
		// System.out.println("index_end_off ="+index_end_off);

		long start_num = index_begin_off;
		long end_num = index_end_off;

		for (long i = start_num; i <= end_num; i += 7) {

			byte[] ip_start = new byte[4];
			byte[] ip_end = new byte[4];

			ipFile.seek(i);
			ipFile.readFully(ip_start);
			long ip_record_off = readLong3(i + 4);

			ipFile.seek(ip_record_off);
			ipFile.readFully(ip_end);

			ip_start = toNetOrder(ip_start);
			ip_end = toNetOrder(ip_end);

			// Only search half: first-half OR second-half
			// long half_index_off = i + ( (index_end_off-i)/14 ) * 7 ;
			// if( ip_array[0]<ip_start[0] ) {
			// i=half_index_off;
			// }else{
			// i=half_index_off;
			// }

			if (ip_start[0] >= ip_array[0] && ip_array[0] <= ip_end[0]) {
				if (ip_start[1] >= ip_array[1] && ip_array[1] <= ip_end[1]) {
					if (getLong4(ip_array) >= getLong4(ip_start)
							&& getLong4(ip_array) <= getLong4(ip_end)) {
						// System.out.println("ip_start = ["+getDotedIP(ip_start)+"]");
						// System.out.println("ip_end = ["+getDotedIP(ip_end)+"]");
						// System.out.println("ip_array = ["+getDotedIP(ip_array)+"]");

						IPLocation loc_get = getIPLocation(ip_record_off);

						// System.out.println("COUNTRY=" +loc_get.country);
						// System.out.println("AREA=" +loc_get.area);
						ret_hash.put("COUNTRY", loc_get.country);
						ret_hash.put("AREA", loc_get.area);
						break;
					}
				}
			}

		}

		long info_index = readLong3(index_end_off + 4);
		IPLocation loc_info = getIPLocation(info_index);
		// System.out.println(loc_info.country + "," +loc_info.area);
		ret_hash.put("IP_INFO_SOURCE", loc_info.country + "," + loc_info.area);
		return ret_hash;

	}

	public String getDotedIP(byte[] ip_addr) throws Exception {
		return InetAddress.getByAddress(ip_addr).getHostAddress();
	}

	/**
	 * 给定一个ip国家地区记录的偏移，返回一个IPLocation结构
	 * 
	 * @param offset
	 *            国家记录的起始偏移
	 * @return IPLocation对象
	 */
	private IPLocation getIPLocation(long offset) {
		try {
			// 跳过4字节ip
			ipFile.seek(offset + 4);
			// 读取第一个字节判断是否标志字节
			byte b = ipFile.readByte();
			if (b == REDIRECT_MODE_1) {
				// 读取国家偏移
				long countryOffset = readLong3();
				// 跳转至偏移处
				ipFile.seek(countryOffset);
				// 再检查一次标志字节，因为这个时候这个地方仍然可能是个重定向
				b = ipFile.readByte();
				if (b == REDIRECT_MODE_2) {
					loc.country = readString(readLong3());
					ipFile.seek(countryOffset + 4);
				} else
					loc.country = readString(countryOffset);
				// 读取地区标志
				loc.area = readArea(ipFile.getFilePointer());
			} else if (b == REDIRECT_MODE_2) {
				loc.country = readString(readLong3());
				loc.area = readArea(offset + 8);
			} else {
				loc.country = readString(ipFile.getFilePointer() - 1);
				loc.area = readArea(ipFile.getFilePointer());
			}
			return loc;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 从offset偏移开始解析后面的字节，读出一个地区名
	 * 
	 * @param offset
	 *            地区记录的起始偏移
	 * @return 地区名字符串
	 * @throws IOException
	 *             地区名字符串
	 */
	private String readArea(long offset) throws IOException {
		ipFile.seek(offset);
		byte b = ipFile.readByte();
		if (b == REDIRECT_MODE_1 || b == REDIRECT_MODE_2) {
			long areaOffset = readLong3(offset + 1);
			if (areaOffset == 0)
				// return LumaQQ.getString("unknown.area");
				return "unknown area";
			else
				return readString(areaOffset);
		} else
			return readString(offset);
	}

	/**
	 * 从offset位置读取3个字节为一个long，因为java为big-endian格式，所以没办法 用了这么一个函数来做转换
	 * 
	 * @param offset
	 *            整数的起始偏移
	 * @return 读取的long值，返回-1表示读取文件失败
	 */
	private long readLong3(long offset) {
		long ret = 0;
		try {
			ipFile.seek(offset);
			ipFile.readFully(b3);
			ret |= (b3[0] & 0xFF);
			ret |= ((b3[1] << 8) & 0xFF00);
			ret |= ((b3[2] << 16) & 0xFF0000);
			return ret;
		} catch (IOException e) {
			return -1;
		}
	}

	private long readLong4(long offset) {
		long ret = 0;
		try {
			ipFile.seek(offset);
			ipFile.readFully(b4);
			ret |= (b4[0] & 0xFF);
			ret |= ((b4[1] << 8) & 0xFF00);
			ret |= ((b4[2] << 16) & 0xFF0000);
			ret |= ((b4[3] << 24) & 0xFF000000);
			return ret;
		} catch (IOException e) {
			return -1;
		}
	}

	private long getLong4(byte[] b4) {
		long ret = 0;

		ret |= (b4[3] & 0xFF);
		ret |= ((b4[2] << 8) & 0xFF00);
		ret |= ((b4[1] << 16) & 0xFF0000);
		ret |= ((b4[0] << 24) & 0xFF000000);
		return ret;

	}

	private byte[] toNetOrder(byte[] in_b4) {
		byte[] out_b4 = new byte[4];
		out_b4[0] = in_b4[3];
		out_b4[1] = in_b4[2];
		out_b4[2] = in_b4[1];
		out_b4[3] = in_b4[0];
		return out_b4;
	}

	/**
	 * 从当前位置读取3个字节转换成long
	 * 
	 * @return 读取的long值，返回-1表示读取文件失败
	 */
	private long readLong3() {
		long ret = 0;
		try {
			ipFile.readFully(b3);
			ret |= (b3[0] & 0xFF);
			ret |= ((b3[1] << 8) & 0xFF00);
			ret |= ((b3[2] << 16) & 0xFF0000);
			return ret;
		} catch (IOException e) {
			return -1;
		}
	}

	/**
	 * 从offset偏移处读取一个以0结束的字符串
	 * 
	 * @param offset
	 *            字符串起始偏移
	 * @return 读取的字符串，出错返回空字符串
	 */
	private String readString(long offset) {
		try {
			ipFile.seek(offset);
			int i;
			for (i = 0, buf[i] = ipFile.readByte(); buf[i] != 0; buf[++i] = ipFile
					.readByte())
				;
			if (i != 0)
				// return Utils.getString(buf, 0, i, "GBK");
				return new String(buf, 0, i, "GBK");
		} catch (IOException e) {
			// log.error(e.getMessage());
			System.out.println(e.getMessage());
		}
		return "";
	}

	class IPLocation {

		public String country;

		public String area;
	}

}
