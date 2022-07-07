package com.spring.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Configuration
@Slf4j
public class SnowFlakeConfig {


	@Bean(value="SnowFlakeService")
	public SnowFlake snowFlakeService() throws Exception {
		// 雪花算法需要设置一个数据中心, 机器标识
		SnowFlake snowFlake = new SnowFlake(this.getDatacenterId(), this.getMachineId());
		return snowFlake;
	}

	// 获取机器ip
	private String getHostAddress() {
		String resultIP = null;
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = allNetInterfaces.nextElement();
				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = addresses.nextElement();
					if (address != null && address instanceof Inet4Address) {
						if(resultIP == null) {
							resultIP = address.getHostAddress();
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("获取IP失败: ", e);
		}
		log.info("===========本机IP: " + resultIP);
		return resultIP;
	}

	// 数据中心
	private long getDatacenterId() throws IllegalAccessException {
		return 0L;
	}

	// 获取雪花算法机器标识
	private long getMachineId() {
		return this.ipToLong(this.getHostAddress());
	}

	private long ipToLong(String ip) {
		String[] ipArray = ip.split("\\.");
		List ipNums = new ArrayList();
		for (int i = 0; i < 4; ++i) {
			ipNums.add(Long.valueOf(Long.parseLong(ipArray[i].trim())));
		}
		long ipNumTotal = ((Long) ipNums.get(0)).longValue() * 256L * 256L * 256L
				+ ((Long) ipNums.get(1)).longValue() * 256L * 256L + ((Long) ipNums.get(2)).longValue() * 256L
				+ ((Long) ipNums.get(3)).longValue();

		return ipNumTotal;
	}
}