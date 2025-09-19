package sec03_udpcommunication.EX04_ConnectedUDP_File;

/* 연결성(connected) UDP를 이용한 File 전송 (ClientA 측) : File 수신측 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ConnectedUDP_File_ClientA {
	public static void main(String[] args) {
		
		System.out.println("<<ClientB>> - File");
		
		//#1. DatagramSocket 생성 (binding 포함)
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(20000);
			ds.connect(new InetSocketAddress("localhost", 10000));			
		} catch (SocketException e) { e.printStackTrace();	}
		
		//#2. 파일로딩
		File file = new File("files_clientB/ImageFileUsingUDP.jpg");
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) { e.printStackTrace(); }
		
		//#3. 데이터그램 패킷 전송
		DatagramPacket sendPacket = null;
		
		//@3-1. 파일이름 전송
		String fileName = file.getName();
		sendPacket = new DatagramPacket(fileName.getBytes(), fileName.getBytes().length); //수신지 정보 없음
		try {
			ds.send(sendPacket);
		} catch (IOException e) {e.printStackTrace(); }
		
		//@3-2. 파일전송 시작을 알리는 사인 전송 (/start)
		String startSign = "/start";
		sendPacket = new DatagramPacket(startSign.getBytes(), startSign.getBytes().length); //수신지 정보 없음
		try {
			ds.send(sendPacket);
		} catch (IOException e) {e.printStackTrace(); }
		
		//@3-3. 2048 사이즈로 나누어 실제 파일데이터 전송
		int len;
		byte[] filedata = new byte[2048]; //최대 65508 byte이지만 2048로 나누어 전송
		try {
			while((len=bis.read(filedata)) !=-1) {
				sendPacket = new DatagramPacket(filedata, len);
				ds.send(sendPacket);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//@3-4. 파일전송 끝을 알리는 사인 전송 (/end)
		String endSign = "/end";
		sendPacket = new DatagramPacket(endSign.getBytes(), endSign.getBytes().length); //수신지 정보 없음
		try {
			ds.send(sendPacket);
		} catch (IOException e) {e.printStackTrace(); }
		
		//#4. 데이터그램 텍스트 패킷 수신
		byte[] receivedData = new byte[65508];
		DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);
		try {
			ds.receive(receivedPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("수신데이터 : "+new String(receivedPacket.getData()).trim());
	}
}


