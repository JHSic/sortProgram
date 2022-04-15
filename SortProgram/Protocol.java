public class Protocol{
	//프로토콜 타입에 관한 변수
	public static final int PT_UNDEFINED = -1; //프로토콜이 지정되어 있지 않은 경우
	public static final int PT_EXIT = 0; //프로그램 종료
	public static final int PT_INPUT_REQ = 1; //요청
	public static final int PT_INPUT_RES = 2; //응답
	public static final int PT_SORT_RES = 3; //정렬 결과
	
	public static final int PT_TYPE_LEN = 1; //프로토콜 타입 길이
	public static final int LEN_WORDS = 10000; //단어 길이
	public static final int LEN_NUMS = 10000; //정수 길이
	public static final int LEN_MAX = 50000; //최대 데이터 길이
	
	protected int protocolType;
	private byte[] packet;
	
	public Protocol() { //생성자
		this(PT_UNDEFINED);
	}
	
	public Protocol(int protocolType) {
		this.protocolType = protocolType;
		getPacket(protocolType);
	}
	
	public byte[] getPacket(int protocolType){
	    if(packet==null){
			switch(protocolType){
				case PT_INPUT_REQ :
					packet = new byte[PT_TYPE_LEN];
					break;
				case PT_INPUT_RES :
					packet = new byte[PT_TYPE_LEN + LEN_WORDS + LEN_NUMS];
					break;
				case PT_UNDEFINED : 
					packet = new byte[LEN_MAX];
					break;
				case PT_SORT_RES : 
					packet = new byte[PT_TYPE_LEN + LEN_WORDS + LEN_NUMS];
					break;
				case PT_EXIT : 
					packet = new byte[PT_TYPE_LEN];
					break;
			} // end switch
	    } // end if
	    packet[0] = (byte)protocolType;	// packet 바이트 배열의 첫 번째 바이트에 프로토콜 타입 설정
	    return packet;
	}
		
	public byte[] getPacket() {
		return packet;
	}
	
	public void setProtocolType(int protocolType) {
		this.protocolType = protocolType;
	}
	
	public int getProtocolType() {
		return protocolType;
	}
	
	public void setPacket(int type, byte[] buf) {
		packet = null;
		packet = getPacket(type);
		protocolType = type;
		System.arraycopy(buf, 0, packet, 0, packet.length);
	}
	
	public String getWords() {
		return new String(packet, PT_TYPE_LEN, LEN_WORDS).trim();
	}
	
	public void setWords(String words) {
		System.arraycopy(words.trim().getBytes(), 0, packet, PT_TYPE_LEN, words.trim().getBytes().length);
		packet[PT_TYPE_LEN + words.trim().getBytes().length] = '\0';
	}
	
	public String getNums() {
		return new String(packet, PT_TYPE_LEN + LEN_WORDS, LEN_NUMS).trim();
	}
	
	public void setNums(String nums) {
		System.arraycopy(nums.trim().getBytes(), 0, packet, PT_TYPE_LEN + LEN_NUMS, nums.trim().getBytes().length);
		packet[PT_TYPE_LEN + LEN_WORDS + nums.trim().getBytes().length] = '\0';
	}
}