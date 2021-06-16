package com.yc.bean03;

import java.io.IOException;
import java.io.InputStream;

public class TestPing {
	public static void main(String[] args) throws IOException {
		Process p=Runtime.getRuntime().exec("ping 123.123.12.12");
		InputStream iis=p.getInputStream();
		byte[] bt=new byte[1024];
		int length=-1;
		StringBuffer sb=new StringBuffer();
		while((length=iis.read(bt))!=-1) {
			sb.append(new String(bt,0,length,"GBK"));
		}
		iis.close();
		System.out.println(sb.toString());
	}
}
