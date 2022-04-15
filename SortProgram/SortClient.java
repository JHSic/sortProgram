import java.net.*;
import java.io.*;
import java.util.*;

public class SortClient {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
		if(args.length < 2) { 
			System.out.println("사용법 : java LoginClient 주소 포트번호"); 
		}
		
		Socket socket = new Socket(args[0], Integer.parseInt(args[1]));	// 소켓 생성 및 수신 (소켓 주소, 포트)
		OutputStream os = socket.getOutputStream();
       		InputStream is = socket.getInputStream();
		
		Protocol protocol = new Protocol();
		byte[] buf = protocol.getPacket();
		
		String line;
		
		while(true) {
			protocol = new Protocol();
			buf = protocol.getPacket();	
			is.read(buf); // buf 입력 
			int packetType = buf[0]; // 패킷 타입에 입력 지정 
			protocol.setPacket(packetType, buf); // 패킷에 데이터 저장 
			
			if(packetType == Protocol.PT_EXIT) { // 수신 패킷 타입이 종료 
				System.out.println("클라이언트 종료");
				break;
			}
			
			switch(packetType) {
				case Protocol.PT_INPUT_REQ:	// 패킷 타입 요청 
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					System.out.print("정렬하고자 하는 단어 입력 : ");
					String words = br.readLine(); // 단어 데이터 입력 
					System.out.print("정렬하고자 하는 정수 입력 : ");
					String nums = br.readLine(); // 정수 데이터 입력
					protocol = new Protocol(Protocol.PT_INPUT_RES);	// 데이터 요정 타입 프로토콜 시작 
					protocol.setWords(words); // 단어 데이터 패킷에 초기화
					protocol.setNums(nums);	// 정수 데이터 패킷에 초기화
					System.out.println("정렬을 원하는 단어, 정수 정보 전송");
					os.write(protocol.getPacket());	// 패킷 전송 
					break;
					
				case Protocol.PT_SORT_RES: // 정렬된 데이터 수신 
					System.out.println("서버가 정렬 결과 전송.");
					
					System.out.println("단어 정렬 결과 : " + protocol.getWords());
					System.out.println("단어 정렬 결과 : " + protocol.getNums());
					
					protocol = new Protocol(Protocol.PT_EXIT); // 종료 타입 프로토콜 시작   
					System.out.println("종료 패킷 전송");
					os.write(protocol.getPacket());	// 패킷 전송 
					break;		
			}
		}
		is.close();
		os.close();
		socket.close();
	}
}
 