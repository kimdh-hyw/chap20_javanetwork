package sec02_tcpcommunication.EX04_TCP_File;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCP_File_ServerSide {
	public static void main(String[] args) {
		System.out.print("<<Server>>");
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(10000);
		} catch (IOException e) {
			System.out.println("해당포트를 열 수 없습니다.");
			System.exit(0);	//프로그램 종료
		}
		
		System.out.println(" - Client 접속 대기...");
		
		try {
			Socket socket = serverSocket.accept();
			
			System.out.println("Client 연결 수락");
			System.out.println("접속 client 주소:"+socket.getInetAddress()+":"+socket.getPort());
			
			DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
			//전송받은 파일 이름 출력
			String receivedFileName = dis.readUTF();
			System.out.println("파일수신:"+receivedFileName);
			
			File file = new File("src/sec02_tcpcommunication/files_server/"+receivedFileName);			
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			
			byte[] data = new byte[2048];
			int len;
			while((len=dis.readInt())!=-1) {
				dis.read(data,0,len);
				bos.write(data,0,len);
				bos.flush();
			}
			System.out.println("파일 수신 완료");
			dos.writeUTF("파일 전송 완료");
			dos.flush();
			
		} catch (IOException e) {}
	}
}


















