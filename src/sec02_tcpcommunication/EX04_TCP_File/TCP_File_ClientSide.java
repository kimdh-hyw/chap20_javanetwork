package sec02_tcpcommunication.EX04_TCP_File;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCP_File_ClientSide {
	public static void main(String[] args) {
		System.out.println("<<Client>>");
		try {
			Socket socket = new Socket(InetAddress.getByName("localhost"), 10000);
			System.out.println("Server에 접속 완료");
			System.out.println("접속 Server 주소 : "+socket.getInetAddress()+":"+socket.getPort());	
			
			DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
			File file = new File("src/sec02_tcpcommunication/files_client/ImageFileUsingTCP.jpg");
			
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			System.out.println("파일전송:"+ file.getName());
			
			//#1. 파일이름 전송
			dos.writeUTF(file.getName());
			
			//#2. 파일데이터 전송
			byte[] data = new byte[2048];
			int len;
			while((len=bis.read(data))!=-1) {
				dos.writeInt(len); //읽은데이터의 길이
				dos.write(data, 0, len); //전송데이터
				dos.flush();
			}
			dos.writeInt(-1);
			dos.flush();
			
			String str = dis.readUTF();
			System.out.println(str);			
			
		} 
		catch (UnknownHostException e) {}
		catch (IOException e) {e.printStackTrace();}
	}
}
