import java.net.*;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.io.*;

public class SortServer {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
		ServerSocket sSocket = new ServerSocket(3000); // TCP 소켓 생성 
		System.out.println("클라이언트 접속 대기중...");		
		Socket socket = sSocket.accept(); // 클라이언트 연결 대기 	
		System.out.println("클라이언트 접속");	
		
		OutputStream os = socket.getOutputStream();	// 소켓 읽기 
       		InputStream is = socket.getInputStream(); // 소켓 쓰기 
		
		Protocol protocol = new Protocol(Protocol.PT_INPUT_REQ); // 프로토콜 객체 생성 및 요청타입 설정 
		os.write(protocol.getPacket());	// 패킷 전송 
		
		boolean program_stop = false; // 스탑 타입 설정
		
		while(true) {
			protocol = new Protocol();
			byte[] buf = protocol.getPacket(); // 버퍼에 패킷저장(타입 상태저장) 
			is.read(buf); // 페킷 수신 
			int packetType = buf[0];			
			protocol.setPacket(packetType,buf);	// 수신한 패킷, 타입 설정 
			
			switch(packetType) {	
				case Protocol.PT_EXIT: // 종료 타입일 경우
					protocol = new Protocol(Protocol.PT_EXIT); //패킷타입 종료 변경
					os.write(protocol.getPacket());	// 패킷 전송 
					program_stop = true; // 스탑 타입 설정
					System.out.println("서버 종료");			
					break;
					
				case Protocol.PT_INPUT_RES:
					System.out.println("클라이언트가 정렬하고자 하는 정보를 보냈습니다.");	
					System.out.print("단어 정보 : ");
					String words = protocol.getWords();	// 수신 단어데이터 받아오기 
					System.out.println(words);
					System.out.print("정수 정보 : ");
					String nums = protocol.getNums(); // 수신 정수 데이터 받아오기 
					System.out.println(nums);
					words = mySort(words); // 수신 단어 데이터 오름 차순 정렬 
					nums = myNumSort(nums);	// 수신 정수 데이터 오름 차순 정렬 
					protocol = new Protocol(Protocol.PT_SORT_RES); // 패킷 타입 수신으로 변경 
					System.out.println("정렬된 단어 정보 : " + words);	
					System.out.println("정렬된 정수 정보 : " + nums);	
					protocol.setWords(words); // 단어 데이터 패킷에 설정 
					protocol.setNums(nums);	// 정수 데이터 패킷에 설정
					
					System.out.println("정렬 결과 전송");
					os.write(protocol.getPacket());	// 패킷 전송 
					break;
			}
			if(program_stop) {
				break;
			}
		}
		is.close();
		os.close();
		socket.close();
	}
	
	public static String mySort(String line) {
		StringTokenizer st = new StringTokenizer(line, " ");

		String res = "";
		int index = 0;
		int arrLength = st.countTokens();
		String[] arr = new String[arrLength];
		
		while (st.hasMoreTokens()){
			arr[index] = st.nextToken(); // 배열에 한 토큰씩 담기 
			index++; 
		}
		
		Arrays.sort(arr);
		
		for(int i = 0; i < arrLength; i++) {
			res += arr[i] + " ";
		}
		return res;
	}
	
	public static String myNumSort(String line) {
		StringTokenizer st = new StringTokenizer(line, " ");

		String res = "";
		int index = 0;
		int arrLength = st.countTokens();
		int[] arr = new int[arrLength];
		
		while (st.hasMoreTokens()){
			arr[index] = Integer.parseInt(st.nextToken()); // 배열에 int형으로 변환하여 한 토큰씩 담기 
			index++; 
		}
		
		Arrays.sort(arr);
		
		for(int i = 0; i < arrLength; i++) {
			res += arr[i] + " ";
		}
		return res;
	}
}
