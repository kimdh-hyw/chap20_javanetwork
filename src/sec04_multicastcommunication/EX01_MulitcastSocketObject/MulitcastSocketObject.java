package sec04_multicastcommunication.EX01_MulitcastSocketObject;

/* MulticastSocket 객체 생성 + MulticastSocket의 주요 메서드 */ 

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MulitcastSocketObject {
	public static void main(String[] args) {
		
		//#멀티캐스트 : 클래스 D 224.0.0.0 ~ 239.255.255.255
		
		//#1. MulticastSocket 객체 생성
		MulticastSocket mcs1 = null;
		MulticastSocket mcs2 = null;
		MulticastSocket mcs3 = null;
		try {
			mcs1 = new MulticastSocket(); //기본생성자를 사용하는 경우 비어있는 포트로 자동 바인딩
			mcs2 = new MulticastSocket(10000);
			//mcs3 = new MulticastSocket(new InetSocketAddress(InetAddress.getByName("localhost"), 10000)); //127.0.0.1은 불가
			mcs3 = new MulticastSocket(new InetSocketAddress(InetAddress.getLocalHost(), 10000)); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(mcs1.getLocalSocketAddress());
		System.out.println(mcs2.getLocalSocketAddress());
		System.out.println(mcs3.getLocalSocketAddress());
		System.out.println();
		
		//#2. MulticastSocket 주요 메서드 (이외 IllegalArgumentException 발생)
		//@ getTimeToLive(), setTimeToLive(): 0~255
		try {
			System.out.println("TimeToLive: " + mcs1.getTimeToLive()); //1
			mcs1.setTimeToLive(50);
			System.out.println("TimeToLive: " + mcs1.getTimeToLive()); //50
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println();
		
		//@ joinGroup(IP 정보:InetAddress), leaveGroup(IP 정보:InetAddress)
		//  send(데이터그램패킷), receive(데이터그램패킷)
		
		//joinGroup()
		try {
			mcs1.joinGroup(InetAddress.getByName("234.234.234.234"));
			mcs2.joinGroup(InetAddress.getByName("234.234.234.234"));
			mcs3.joinGroup(InetAddress.getByName("234.234.234.234"));
			
			/* 변경 버전 2025.09
			//NetworkInterface는 어느 네트워크 인터페이스(예: eth0, wlan0)를 사용할지 지정합니다. 멀티캐스트는 네트워크 인터페이스에 따라 다르게 동작할 수 있기 때문에 명시해야 함
			mcs1.joinGroup(new InetSocketAddress(InetAddress.getByName("234.234.234.234"),mcs1.getLocalPort()), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
			mcs2.joinGroup(new InetSocketAddress(InetAddress.getByName("234.234.234.234"),10000), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
			mcs3.joinGroup(new InetSocketAddress(InetAddress.getByName("234.234.234.234"),10000), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
			*/
			
			byte[] sendData = "안녕하세요".getBytes();
			
			DatagramPacket sendPacket = 
					new DatagramPacket(sendData, sendData.length, InetAddress.getByName("234.234.234.234"), 10000);
			mcs1.send(sendPacket);
			
			byte[] receivedData;
			DatagramPacket receivedPacket;
			
			receivedData = new byte[65508];
			receivedPacket = new DatagramPacket(receivedData, receivedData.length);
			mcs2.receive(receivedPacket);
			
			System.out.print("mcs2가 수신한 데이터 : " + new String(receivedPacket.getData()).trim());
			System.out.println("  송신지 : "+receivedPacket.getSocketAddress());
						
			receivedData = new byte[65508];
			receivedPacket = new DatagramPacket(receivedData, receivedData.length);
			mcs3.receive(receivedPacket);
			
			System.out.print("mcs3가 수신한 데이터 : " + new String(receivedPacket.getData()).trim());
			System.out.println("  송신지 : "+receivedPacket.getSocketAddress());
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

