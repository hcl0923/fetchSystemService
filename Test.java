package com.yc.bean03;

public class Test {
	public static void main(String[] args) {
		System.out.println("网络中上线的机器如下: ");
		System.out.println ("地址\t\t主机\t\t连接时间\t\t丢包率\t\t网速");
		NetScan ns =new NetScan("123.123.12",12,20, new NotifyNetScan() {
			@Override
			public void notify(IpInfo info){
				System.out.println(info.hostName+"\t"+info.ip+"\t"+info.linkTime+"ms"+"\t\t"+info.lost+"%"+"\t\t"+info.speed);
			}
		});
		new Thread(ns).start();
		
		NetScan ns2 =new NetScan("www.baidu.com",new NotifyNetScan() {
			public void notify (IpInfo info) {
				System.out.println(info.hostName+"\t"+info.ip+"\t"+info.linkTime+"ms"+"\t\t"+info.lost+"%"+"\t\t"+info.speed);
			}
		});
		//new Thread(ns2).start();
	}
}
