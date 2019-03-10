package com.zwt.framework.utils.util.host;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

//@Slf4j
public class HostUtil {

	private static Logger log = LoggerFactory.getLogger(HostUtil.class);

	private static final String LOCAL_IP = "127.0.0.1";

	private static InetAddress ADDR_LOCALHOST = null;
	private static InetAddress ADDR_NETWORK = null;

	static {
		ADDR_LOCALHOST = getInetAddressFromLocalHost();
		ADDR_NETWORK=getInetAddressFromNetwork();
	}

	private static final InetAddress getInetAddressFromLocalHost() {
		try {
			InetAddress address = InetAddress.getLocalHost();
			if (address == null) {
				return null;
			}

			String ip = address.getHostAddress();
			if (StringUtils.isBlank(ip)) {
				return null;
			}

			if (LOCAL_IP.equals(ip.trim())) {
				return null;
			}

			return address;

		} catch (UnknownHostException e) {
			log.error("HostUtils getInetAddressFromLocalHost failed." + e);
			return null;
		}
	}

	private static final InetAddress getInetAddressFromNetwork() {
		try {
			InetAddress inetaddress = null;
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
			OUT: while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					InetAddress address = inetAddresses.nextElement();
					/*
					 * 当IP地址是地区本地地址（SiteLocalAddress）时返回true，否则返回false.
					 * IPv4的地址本地地址分为三段：10.0.0.0 ~ 10.255.255.255、172.16.0.0 ~
					 * 172.31.255.255、192.168.0.0 ~
					 * 192.168.255.255.IPv6的地区本地地址的前12位是FEC
					 * ，其他的位可以是任意取值，如FED0：：、FEF1：：都是地区本地地址。
					 */
					if (!address.isSiteLocalAddress()) {
						continue;
					}
					/*
					 * 当IP地址是loopback地址时返回true，否则返回false.loopback地址就是代表本机的IP地址。
					 * IPv4的loopback地址的范围是127.0.0.0 ~
					 * 127.255.255.255，也就是说，只要第一个字节是127
					 * ，就是lookback地址。如127.1.2.3、127.0.200.200都是
					 * loopback地址。IPv6的loopback地址是0：0：0：0：0：0：0：1，也可以简写成：：1.
					 */
					if (address.isLoopbackAddress()) {
						continue;
					}

					String ip = address.getHostAddress().trim();

					if (ip.indexOf(":") >= 0) {
						continue;
					}

					if (ip.equals(LOCAL_IP)) {
						continue;
					}

					inetaddress = address;
					break OUT;
				}
			}

			return inetaddress;
		} catch (SocketException e) {
			log.error("HostUtils init fail." + e);
			return null;
		}
	}

	/**
     * 获取本机IP
	 * @return
     */
	public static String getIp() {
		try{
			if(ADDR_NETWORK!=null){
				return ADDR_NETWORK.getHostAddress();
			}
			return ADDR_LOCALHOST.getHostAddress();
		}catch(Exception ex){
			return "unknown-ip";
		}
	}

	/**
     * 获取主机名
	 * @return
     */
	public static String getHostName() {
		try{
			if(ADDR_LOCALHOST!=null){
				return ADDR_LOCALHOST.getHostName();
			}
			return ADDR_NETWORK.getHostName();
		}catch(Exception ex){
			return "unknown-host";
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(HostUtil.getIp() + ", " + HostUtil.getHostName());
	}

}
