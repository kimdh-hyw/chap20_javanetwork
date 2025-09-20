package sec04_multicastcommunication.EX02_Multicast_Text;

/* Multicast 통신을 이용한 Text 송수신 (ClientA) (2025.09 수정) */ 

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

public class Multicast_Text_ClientA {
		public static void main(String[] args) {
		
		System.out.println("<<ClientA>> - Text 수신대기 ... ");
		
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
		
		//#4. 데이터그램 패킷 수신 대기
		receiveMessage(mcs); //ClientA가 보낸 메시지 수신
		
		//#5. 전송 데이터그램 패킷 생성 + 전송
		byte[] sendData = "반갑습니다!(ClientA)".getBytes();
		
		DatagramPacket sendPacket = 
				new DatagramPacket(sendData, sendData.length, multicastAddress, multicastPort);
		try {
			mcs.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//#6. 데이터그램 패킷 수신 대기
		receiveMessage(mcs); //자기가 보낸 메시지 수신
		
		//#7. 멀티캐스트 그룹 나가기
		try {
			//mcs.leaveGroup(multicastAddress);
			
			//변경 버전 2025.09 
			//NetworkInterface는 어느 네트워크 인터페이스(예: eth0, wlan0)를 사용할지 지정합니다. 멀티캐스트는 네트워크 인터페이스에 따라 다르게 동작할 수 있기 때문에 명시해야 함
			mcs.leaveGroup(new InetSocketAddress(multicastAddress,multicastPort), NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//#8. 소켓 닫기
		mcs.close();
		
	}
	
	static void receiveMessage(MulticastSocket mcs) {
		byte[] receivedData;
		DatagramPacket receivedPacket;
		
		receivedData = new byte[65508];
		receivedPacket = new DatagramPacket(receivedData, receivedData.length);
		try {
			mcs.receive(receivedPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("보내온 주소 : " + receivedPacket.getSocketAddress());
		System.out.println("보내온 내용 : " + new String(receivedPacket.getData()).trim());
	}	
	
}





