package sec01_managingaddress.EX01_InetAddressObjectAndMethod;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class InetAddressObjectAndMethod {
	public static void main(String[] args) throws IOException {
		
		//#1. InetAddress 객체 생성
		//@1-1. 원격지IP 객체 생성
		InetAddress ia1 = InetAddress.getByName("www.google.com");
		InetAddress ia2 = InetAddress.getByAddress(new byte[] {(byte)172,(byte)217,(byte)161,36});
		InetAddress ia3 = InetAddress.getByAddress("www.google.com", new byte[] {(byte)172,(byte)217,(byte)161,36});
		System.out.println(ia1);
		System.out.println(ia2);
		System.out.println(ia3);
		System.out.println();
		
		//@1-2. 로컬/로프백IP
		InetAddress ia4 = InetAddress.getLocalHost();
		InetAddress ia5 = InetAddress.getLoopbackAddress();
		System.out.println(ia4);
		System.out.println(ia5);
		System.out.println();
		
		
		//@1-3. 하나의 호스트가 여러 개의 IP를 가지고 있는 경우
		InetAddress[] ia6 = InetAddress.getAllByName("www.naver.com");
		System.out.println(Arrays.toString(ia6));
		System.out.println();
		
		//#2. InetAddress 메서드
		byte[] address = ia1.getAddress();
		System.out.println(Arrays.toString(address));
		System.out.println(ia1.getHostAddress());
		System.out.println(ia1.getHostName());
		System.out.println();
		
		System.out.println(ia1.isReachable(1000)); //true
		System.out.println(ia1.isLoopbackAddress()); //false
		System.out.println(ia1.isMulticastAddress()); //false 224-239.0-255.0-255.0-255
		System.out.println(InetAddress.getByAddress(new byte[] {127,0,0,1}).isLoopbackAddress()); //true
		System.out.println(InetAddress.getByAddress(new byte[] {(byte)234,(byte)234,(byte)234,(byte)234}).isMulticastAddress()); //true

		
		
		
	}
}
