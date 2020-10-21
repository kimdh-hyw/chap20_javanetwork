package sec04_multicastcommunication.EX03_Multicast_File;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Multicast_File_ClientA {
	public static void main(String[] args) {
		
		System.out.println("<<ClientA>> - File");
		
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
		
		//#3. 파일 로딩
		File file = new File("src\\sec04_multicastcommunication\\files_clientA\\ImageFileUsingMulticast.jpg");
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}
		
		//#4. 파일데이터 전송
		DatagramPacket sendPacket = null;
		
		//@4-0. 파일이름 전송
		String fileName = file.getName();
		sendPacket = new DatagramPacket(fileName.getBytes(), fileName.length(), multicastAddress, multicastPort);
		try {
			mcs.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(fileName + " 파일 전송 시작");
		
		//@4-1. 파일 시작 태그를 전송 (/start) 
		String startSign = "/start";
		sendPacket = new DatagramPacket(startSign.getBytes(), startSign.length(), multicastAddress, multicastPort);
		try {
			mcs.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//@4-2. 실제 파일 데이터 전송 (2048사이즈로 나누어서 파일 전송)
		int len;
		byte[] filedata = new byte[2048];
		try {
			while((len=bis.read(filedata))!=-1) {
				sendPacket = new DatagramPacket(filedata, len, multicastAddress, multicastPort);
				mcs.send(sendPacket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//@4-3. 파일 시작 태그를 전송 (/start) 
		String endSign = "/end";
		sendPacket = new DatagramPacket(endSign.getBytes(), endSign.length(), multicastAddress, multicastPort);
		try {
			mcs.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//#5. 멀티캐스트 그룹에 조인
		try {
			mcs.joinGroup(multicastAddress);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//#6. 데이터 수신 대기		
		receiveMessage(mcs);
		
		//#7. 멀티캐스트 그룹 나가기
		try {
			mcs.leaveGroup(multicastAddress);
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





















