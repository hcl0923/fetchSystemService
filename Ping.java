package com.yc.bean03;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ping {
	//获取ping命令执行结果
	public String getPingResult(String ip) throws IOException{
		//执行dos命令
		Process p=Runtime.getRuntime().exec("ping "+ip);
		//获取输入流
		InputStream iis = p.getInputStream();
		//解决中文乱码
		InputStreamReader ir = new InputStreamReader(iis,Charset.forName("GBK"));
		char []cs =new char[1024];
		int length=-1;
		StringBuffer sb=new StringBuffer();
		while((length=ir.read(cs))!=-1){
			sb.append(new String(cs,0,length));
		}
		ir.close();
		iis.close();
		return sb.toString();
	}
	
	public IpInfo isOnline(String ip,String result) {
		IpInfo ipInfo = new IpInfo();
		if(result.indexOf("无法访问目标主机")!=-1||result.indexOf("请求找不到主机")!=-1
				||result.indexOf("请求超时")!=-1){
			ipInfo.ip=ip;
			ipInfo.isOnline=false;
			ipInfo.linkTime=Integer.MAX_VALUE;
		}else{
			ipInfo.ip=ip;
			ipInfo.hostName=ip;
			ipInfo.isOnline=true;
			//如果是网址
			if(result.contains("[")) {
				//重置IP公
				//正则表达式
				if(matchPattern("\\[(.*)\\]", result.toString())!=null) {
					ipInfo.ip=matchPattern("\\[(.*)\\]",result.toString());
				}
			}
			//计算连接平均耗时
			int avg=0;
			if(matchPattern("平均 = (.*)ms",result)!=null) {
				avg=Integer.parseInt(matchPattern("平均 = (.*)ms",result));
			}
			ipInfo.linkTime=avg;
			
			//匹配丢包率
			int lost =0;
			if (matchPattern("\\((.*)\\% 丢失",result)!=null){
				lost=Integer.parseInt(matchPattern("\\((.*)\\% 丢失",result));
			}
			ipInfo.lost=lost;
			//连接速度
			Pattern pattern=Pattern.compile("时间=(.*)ms");
			Matcher matcher=pattern.matcher(result);
			int total =0;
			while (matcher.find()){
				total+=Integer.parseInt(matcher.group(1));
			}
			double speed=(32*8*1000)/(total*1024);
			ipInfo.speed =speed;
		}
		return ipInfo;
	}
	//抽取匹配规则
	private String matchPattern (String pattern, String macther) {
		Pattern mpPattern=Pattern.compile(pattern);
		Matcher mMatcher=mpPattern.matcher(macther);
		String result =null;
		while (mMatcher.find()) {
			result =mMatcher.group(1);
		}
		return result;
	}
}
