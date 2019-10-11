package org.yui.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP 压缩工具
 * @author Frank
 *
 * 这个方法是从亿美的官方java demo里面
 * 复制过来的。
 */
public abstract class GzipUtil {

	public static void main(String[] args) throws IOException {
		String sst = "hahahah";
		System.out.println(sst);
		System.out.println(System.currentTimeMillis());
		System.out.println("size:" + sst.length());
		byte[] bytes = sst.getBytes();
		System.out.println("length:" + bytes.length);
		System.out.println(System.currentTimeMillis());
		byte[] end = compress(bytes);
		System.out.println(System.currentTimeMillis());
		System.out.println("length:" + end.length);
		System.out.println(System.currentTimeMillis());
		byte[] start = decompress(end);
		System.out.println(System.currentTimeMillis());
		System.out.println("length:" + start.length);
		System.out.println(new String(start));
	}

	/**
	 * 数据压缩传输
	 * 
	 * @param bytes
	 * @param outputStream
	 * @throws Exception
	 */
	public static void compressTransfer(byte[] bytes, OutputStream outputStream) throws IOException {
		try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
			gzipOutputStream.write(bytes);
			gzipOutputStream.finish();
			gzipOutputStream.flush();
		}
	}
	
	/**
	 * 数据压缩
	 * 
	 * @param bytes
	 * @throws Exception
	 */
	public static byte[] compress(byte[] bytes) throws IOException {

		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			 GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
			gzipOutputStream.write(bytes);
			gzipOutputStream.finish();
			gzipOutputStream.flush();
			return byteArrayOutputStream.toByteArray();
		}
	}
	
	/**
	 * 数据解压
	 * 
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	public static byte[] decompress(byte[] bytes) throws IOException {

		GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes));
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int count;
		byte[] datas = new byte[1024];
		while ((count = gzipInputStream.read(datas)) != -1) {
			byteArrayOutputStream.write(datas, 0, count);
		}
		byteArrayOutputStream.flush();
		byteArrayOutputStream.close();
		gzipInputStream.close();
		return byteArrayOutputStream.toByteArray();
	}

}
