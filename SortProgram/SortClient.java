import java.net.*;
import java.io.*;
import java.util.*;

public class SortClient {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
		if(args.length < 2) { 
			System.out.println("���� : java LoginClient �ּ� ��Ʈ��ȣ"); 
		}
		
		Socket socket = new Socket(args[0], Integer.parseInt(args[1]));	// ���� ���� �� ���� (���� �ּ�, ��Ʈ)
		OutputStream os = socket.getOutputStream();
       		InputStream is = socket.getInputStream();
		
		Protocol protocol = new Protocol();
		byte[] buf = protocol.getPacket();
		
		String line;
		
		while(true) {
			protocol = new Protocol();
			buf = protocol.getPacket();	
			is.read(buf); // buf �Է� 
			int packetType = buf[0]; // ��Ŷ Ÿ�Կ� �Է� ���� 
			protocol.setPacket(packetType, buf); // ��Ŷ�� ������ ���� 
			
			if(packetType == Protocol.PT_EXIT) { // ���� ��Ŷ Ÿ���� ���� 
				System.out.println("Ŭ���̾�Ʈ ����");
				break;
			}
			
			switch(packetType) {
				case Protocol.PT_INPUT_REQ:	// ��Ŷ Ÿ�� ��û 
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					System.out.print("�����ϰ��� �ϴ� �ܾ� �Է� : ");
					String words = br.readLine(); // �ܾ� ������ �Է� 
					System.out.print("�����ϰ��� �ϴ� ���� �Է� : ");
					String nums = br.readLine(); // ���� ������ �Է�
					protocol = new Protocol(Protocol.PT_INPUT_RES);	// ������ ���� Ÿ�� �������� ���� 
					protocol.setWords(words); // �ܾ� ������ ��Ŷ�� �ʱ�ȭ
					protocol.setNums(nums);	// ���� ������ ��Ŷ�� �ʱ�ȭ
					System.out.println("������ ���ϴ� �ܾ�, ���� ���� ����");
					os.write(protocol.getPacket());	// ��Ŷ ���� 
					break;
					
				case Protocol.PT_SORT_RES: // ���ĵ� ������ ���� 
					System.out.println("������ ���� ��� ����.");
					
					System.out.println("�ܾ� ���� ��� : " + protocol.getWords());
					System.out.println("�ܾ� ���� ��� : " + protocol.getNums());
					
					protocol = new Protocol(Protocol.PT_EXIT); // ���� Ÿ�� �������� ����   
					System.out.println("���� ��Ŷ ����");
					os.write(protocol.getPacket());	// ��Ŷ ���� 
					break;		
			}
		}
		is.close();
		os.close();
		socket.close();
	}
}
 