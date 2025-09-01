package sec01_managingaddress.EX02_SocketAddressObjectAndMethod;

/* InetSocketAddress(IP주소+Port주소)를 이용한 네트워크 주소저장(객체 생성 및 주요 메서드)*/

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class SocketAddressObjectAndMethod {
	public static void main(String[] args) throws UnknownHostException {
		
		//#1. SocketAddress 객체 생성 (InetSocketAddress 생성자 사용)
		InetAddress ia = InetAddress.getByName("www.google.com");
		int port = 10000;
		InetSocketAddress isa1 = new InetSocketAddress(port); //port 정보만 지정
		InetSocketAddress isa2 = new InetSocketAddress("www.google.com", port);//hostName+IP정보+port정보 지정
		InetSocketAddress isa3 = new InetSocketAddress(ia, port);//InetAddress+port 정보 지정
		
		System.out.println(isa1);
		System.out.println(isa2);
		System.out.println(isa3);
		System.out.println();

		//#2. SocketAddress의 메서드
		System.out.println(isa2.getAddress());
		System.out.println(isa2.getHostName());
		System.out.println(isa2.getPort());	
		
	}
}


