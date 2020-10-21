package sec02_tcpcommunication.EX02_SeverSocketObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class SeverSocketObject {
	public static void main(String[] args) throws IOException {
		
		//#1. ServerSocket 객체 생성
		ServerSocket serverSocket1 = new ServerSocket();
		ServerSocket serverSocket2 = new ServerSocket(20000);
		
		//#2. ServerSocket 메서드
		//@bind
		System.out.println(serverSocket1.isBound()); //false
		System.out.println(serverSocket2.isBound()); //true
		System.out.println();
		
		//serverSocket1.bind(new InetSocketAddress("127.0.0.1", 10000));
		serverSocket1.bind(new InetSocketAddress(InetAddress.getLocalHost(), 10000));
		
		System.out.println(serverSocket1.isBound()); //true
		System.out.println(serverSocket2.isBound()); //true
		
		System.out.println();
		
		//@사용중인 TCP 포트 확인하기
		for(int i=0; i<65536; i++) {
			try {
				ServerSocket serverSocket = new ServerSocket(i);
			} catch(IOException e) {
				System.out.println(i+"번째 포트 사용중 ...");
			}
		}
		System.out.println();
		
		//@accept() 일반적으로는 별도의 쓰레드에서 실행
		//@setSoTimeout(.) : accept() 대기 시간
		serverSocket1.setSoTimeout(2000);
		try {
			Socket socket = serverSocket1.accept(); //blocking
		} catch(IOException e) {
			System.out.println(serverSocket1.getSoTimeout()+"ms 시간이 지나 접속대기를 종료합니다.");
		}
		
	}
}




















