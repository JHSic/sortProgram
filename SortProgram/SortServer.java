import java.net.*;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.io.*;

public class SortServer {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
		ServerSocket sSocket = new ServerSocket(3000); // TCP ���� ���� 
		System.out.println("Ŭ���̾�Ʈ ���� �����...");		
		Socket socket = sSocket.accept(); // Ŭ���̾�Ʈ ���� ��� 	
		System.out.println("Ŭ���̾�Ʈ ����");	
		
		OutputStream os = socket.getOutputStream();	// ���� �б� 
       		InputStream is = socket.getInputStream(); // ���� ���� 
		
		Protocol protocol = new Protocol(Protocol.PT_INPUT_REQ); // �������� ��ü ���� �� ��ûŸ�� ���� 
		os.write(protocol.getPacket());	// ��Ŷ ���� 
		
		boolean program_stop = false; // ��ž Ÿ�� ����
		
		while(true) {
			protocol = new Protocol();
			byte[] buf = protocol.getPacket(); // ���ۿ� ��Ŷ����(Ÿ�� ��������) 
			is.read(buf); // ��Ŷ ���� 
			int packetType = buf[0];			
			protocol.setPacket(packetType,buf);	// ������ ��Ŷ, Ÿ�� ���� 
			
			switch(packetType) {	
				case Protocol.PT_EXIT: // ���� Ÿ���� ���
					protocol = new Protocol(Protocol.PT_EXIT); //��ŶŸ�� ���� ����
					os.write(protocol.getPacket());	// ��Ŷ ���� 
					program_stop = true; // ��ž Ÿ�� ����
					System.out.println("���� ����");			
					break;
					
				case Protocol.PT_INPUT_RES:
					System.out.println("Ŭ���̾�Ʈ�� �����ϰ��� �ϴ� ������ ���½��ϴ�.");	
					System.out.print("�ܾ� ���� : ");
					String words = protocol.getWords();	// ���� �ܾ���� �޾ƿ��� 
					System.out.println(words);
					System.out.print("���� ���� : ");
					String nums = protocol.getNums(); // ���� ���� ������ �޾ƿ��� 
					System.out.println(nums);
					words = mySort(words); // ���� �ܾ� ������ ���� ���� ���� 
					nums = myNumSort(nums);	// ���� ���� ������ ���� ���� ���� 
					protocol = new Protocol(Protocol.PT_SORT_RES); // ��Ŷ Ÿ�� �������� ���� 
					System.out.println("���ĵ� �ܾ� ���� : " + words);	
					System.out.println("���ĵ� ���� ���� : " + nums);	
					protocol.setWords(words); // �ܾ� ������ ��Ŷ�� ���� 
					protocol.setNums(nums);	// ���� ������ ��Ŷ�� ����
					
					System.out.println("���� ��� ����");
					os.write(protocol.getPacket());	// ��Ŷ ���� 
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
			arr[index] = st.nextToken(); // �迭�� �� ��ū�� ��� 
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
			arr[index] = Integer.parseInt(st.nextToken()); // �迭�� int������ ��ȯ�Ͽ� �� ��ū�� ��� 
			index++; 
		}
		
		Arrays.sort(arr);
		
		for(int i = 0; i < arrLength; i++) {
			res += arr[i] + " ";
		}
		return res;
	}
}
