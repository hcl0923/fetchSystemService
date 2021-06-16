package com.yc.bean03;

import java.io.IOException;

public class NetScan implements Runnable{
	private String ipPrefix;//ip地址前三组
	private int start;//ip地址最后- -组的起始值
	private int end;//终止值
	private String hostname;
	private NotifyNetScan notifyNetScan;
	public NetScan(String ipPrefix,int start,int end,NotifyNetScan notifyNetScan) {
		this.ipPrefix =ipPrefix;
		this.start =start;
		this.end=end;
		this.notifyNetScan=notifyNetScan;
	}
	public NetScan(String hostname,NotifyNetScan notifyNetScan) {
		this.hostname=hostname;
		this.start=-1;
		this.notifyNetScan=notifyNetScan;
	}
	@Override
	public void run(){
		if(this.start!=-1) {
			for(int i=start;i<=end;i++) {
				String ip=ipPrefix+"."+i;
				Ping p=new Ping();
				try {
					String result=p.getPingResult(ip);
					IpInfo info=p.isOnline(ip,result);
					//通知主线程获取到对象
					this.notifyNetScan.notify(info);
				} catch(IOException e) {
					System.out.println("网址中地址为: "+hostname+ "的机器连接有问题");
					e.printStackTrace();
				}
			}
		}else {
			Ping p=new Ping();
			String result;
			try {
				result = p.getPingResult(hostname);
				IpInfo info =p.isOnline(hostname,result);
				this.notifyNetScan.notify(info);
			} catch (IOException e) {
				System.out.println("网址中地址为:"+hostname+"的机器连接有问题");
				e.printStackTrace();
			}
		}

}
}