package sec04_multicastcommunication.EX02_Multicast_Text;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Multicast_Text_ClientA {
	public static void main(String[] args) {
		System.out.println("<<ClientA>> - Text");
		
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
			mcs.joinGroup(multicastAddress);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//#4. 전송 데이터그램 패킷 생성 + 전송
		byte[] sendData = "안녕하세요!(ClientA)".getBytes();
		
		DatagramPacket sendPacket = 
				new DatagramPacket(sendData, sendData.length, multicastAddress, multicastPort);
		try {
			mcs.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//#5. 데이터그램 패킷 수신
		receiveMessage(mcs); //자기가 보낸 데이터 수신
		receiveMessage(mcs); //상대편이 보낸 데이터 수신
		
		//#6. 멀티캐스트 그룹 나가기
		try {
			mcs.leaveGroup(multicastAddress);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//#7. 소켓 닫기
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












