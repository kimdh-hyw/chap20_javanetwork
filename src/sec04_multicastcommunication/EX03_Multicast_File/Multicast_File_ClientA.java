package sec04_multicastcommunication.EX03_Multicast_File;

/* Multicast 통신을 이용한 File 송수진 (ClientA) (2025.09 수정) */ 

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

public class Multicast_File_ClientA {
	public static void main(String[] args) {
		
		System.out.println("<<ClientA>> - File 수신 대기 ...");
		
		//#1. 멀티캐스팅 주소지 생성
		InetAddress multicastAddress = null;
		try {
			multicastAddress = InetAddress.getByName("234.234.234.234");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		int multicastPort = 10000;
		
		//#2. 멀티캐스트소켓 생성
		MulticastSocket mcs = null;
		try {
			mcs = new MulticastSocket(multicastPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//#3. 멀티캐스트 그룹에 조인
		try {
			//mcs.joinGroup(multicastAddress);
			
			//변경 버전 2025.09 
			//NetworkInterface는 어느 네트워크 인터페이스(예: eth0, wlan0)를 사용할지 지정합니다. 멀티캐스트는 네트워크 인터페이스에 따라 다르게 동작할 수 있기 때문에 명시해야 함
			mcs.joinGroup(new InetSocketAddress(multicastAddress,multicastPort), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//#4. 파일 데이터 수신
		byte[] receivedData;
		DatagramPacket receivedPacket;
		
		//@4-1. 파일 이름 수신
		receivedData = new byte[65508];
		receivedPacket = new DatagramPacket(receivedData, receivedData.length);		
		try {
			mcs.receive(receivedPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String fileName = new String(receivedPacket.getData()).trim();
		System.out.println(fileName + " 파일 수신 시작");
		
		//@4-2. 파일 저장을 위한 파일 출력 스트림 생성
		File file = new File("files_clientA/"+fileName);
		BufferedOutputStream bos=null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//@4-3. 시작태그부터 끝태그까지 모든 데이터패킷의 내용을 파일에 기록
		String startSign = "/start";
		String endSign = "/end";
		receivedData = new byte[65508];
		receivedPacket = new DatagramPacket(receivedData, receivedData.length);	
		
		try {
			mcs.receive(receivedPacket);
			if(new String(receivedPacket.getData(), 0, receivedPacket.getLength()).equals(startSign)) {
				while(true) {
					mcs.receive(receivedPacket);
					if(new String(receivedPacket.getData(), 0, receivedPacket.getLength()).equals(endSign))
						break;
					bos.write(receivedPacket.getData(), 0, receivedPacket.getLength());
					bos.flush();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("파일 수신 완료");
				
		//#5. 멀티캐스트 그룹 나가기
		try {
			//mcs.leaveGroup(multicastAddress);
			
			//변경 버전 2025.09 
			//NetworkInterface는 어느 네트워크 인터페이스(예: eth0, wlan0)를 사용할지 지정합니다. 멀티캐스트는 네트워크 인터페이스에 따라 다르게 동작할 수 있기 때문에 명시해야 함
			mcs.leaveGroup(new InetSocketAddress(multicastAddress,multicastPort), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//#6. 전송 데이터그램 생성 + 전송
		byte[] sendData = "(ClientB) 파일 수신 완료".getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, multicastAddress, multicastPort);		
		try {
			mcs.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//#7. 소켓닫기
		mcs.close();
		
	}
}





