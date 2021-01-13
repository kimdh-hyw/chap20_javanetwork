package sec03_udpcommunication.EX02_DatagramSocketObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class DatagramSocketObject {
	public static void main(String[] args) {
		// #1. DatagramSocket 객체생성
		//   (모든 DatagramSocket은 바인딩 되어 있어야 한다)
		DatagramSocket ds1 = null;
		DatagramSocket ds2 = null;
		DatagramSocket ds3 = null;
		DatagramSocket ds4 = null;
		try {
			ds1 = new DatagramSocket(); // 비어있는 포트로 자동 바인딩
			ds2 = new DatagramSocket(10000);
			ds3 = new DatagramSocket(10001, InetAddress.getByName("localhost"));
			ds4 = new DatagramSocket(new InetSocketAddress("localhost", 10002));
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
		// #2. DatagramSocket 메서드
		// @2-1. 소켓의 바이딩 정보
		System.out.println("ds1의 바인딩 정보 : " + ds1.getLocalAddress() + ":" + ds1.getLocalPort());
		System.out.println("ds2의 바인딩 정보 : " + ds2.getLocalAddress() + ":" + ds2.getLocalPort());
		System.out.println("ds3의 바인딩 정보 : " + ds3.getLocalAddress() + ":" + ds3.getLocalPort());
		System.out.println("ds4의 바인딩 정보 : " + ds4.getLocalAddress() + ":" + ds4.getLocalPort());
		// @2-2. 원격지 정보 저장 (connect() 메서드 사용)
		System.out.println("원격지 주소 정보 : " + ds4.getInetAddress() + ":" + ds4.getPort());
		try {
			ds4.connect(new InetSocketAddress("localhost", 10003));
		} catch (SocketException e) {
			e.printStackTrace();
		}
		System.out.println("원격지 주소 정보 : " + ds4.getInetAddress() + ":" + ds4.getPort());
		ds4.disconnect();
		System.out.println();
		// @2-3. send()/connect(), receive()
		// 전송 데이터그램패킷 2개 생성
		byte[] data1 = "수신지 주소가 없는 데이터그램 패킷".getBytes();
		byte[] data2 = "수신지 주소가 있는 데이터그램 패킷".getBytes();
		DatagramPacket dp1 = new DatagramPacket(data1, data1.length);
		DatagramPacket dp2 = new DatagramPacket(data2, data2.length, new InetSocketAddress("localhost", 10002));
		try {
			/*
			 * 불가능: 소켓-연결(connect) 정보없음 + 패킷-수신지 정보없음 ds1.send(dp1); ds2.send(dp1);
			 * ds3.send(dp1);
			 */
			ds1.connect(new InetSocketAddress("localhost", 10002));
			ds2.connect(new InetSocketAddress("localhost", 10002));
			ds3.connect(new InetSocketAddress("localhost", 10002));
			//가능: 소켓-연결(connect) 정보있음 + 패킷-수신지 정보없음
			ds1.send(dp1);
			ds2.send(dp1);
			ds3.send(dp1);
			ds1.disconnect();
			ds2.disconnect();
			ds3.disconnect();
			//가능: 소켓-연결(connect) 정보없음 + 패킷-수신지 정보있음
			ds1.send(dp2);
			ds2.send(dp2);
			ds3.send(dp2);
			//소켓-연결(connect) 정보있음 + 패킷-수신지 정보있음
			//가능 : 두 주소 일치 : 소켓-연결(connect) 주소 = 패킷-수신지 주소
			ds3.connect(new InetSocketAddress("localhost", 10002));
			ds3.send(dp2);
			ds3.disconnect();
			/*
			 * 불가능 : 두 주소 불일치: 소켓-연결(connect) 주소 != 패킷-수신지 주소 ds3.connect(new
			 * InetSocketAddress("localhost", 10003)); ds3.send(dp2); //
			 * IllegalArgumentException ds3.disconnect();
			 */
			// @ 데이터 수신
			byte[] receivedData = new byte[65508];
			DatagramPacket dp = new DatagramPacket(receivedData, receivedData.length);
			for (int i = 0; i < 7; i++) {
				ds4.receive(dp);
				System.out.print("송신자 정보 : " + dp.getAddress() + ":" + dp.getPort());
				System.out.println(" : " + new String(dp.getData()).trim());
			}
			// @송수신 데이터 버퍼
			System.out.println("송신버퍼 크기 : " + ds1.getSendBufferSize());
			System.out.println("수신버퍼 크기 : " + ds1.getReceiveBufferSize());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
