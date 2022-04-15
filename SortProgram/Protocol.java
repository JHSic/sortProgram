public class Protocol{
	//�������� Ÿ�Կ� ���� ����
	public static final int PT_UNDEFINED = -1; //���������� �����Ǿ� ���� ���� ���
	public static final int PT_EXIT = 0; //���α׷� ����
	public static final int PT_INPUT_REQ = 1; //��û
	public static final int PT_INPUT_RES = 2; //����
	public static final int PT_SORT_RES = 3; //���� ���
	
	public static final int PT_TYPE_LEN = 1; //�������� Ÿ�� ����
	public static final int LEN_WORDS = 10000; //�ܾ� ����
	public static final int LEN_NUMS = 10000; //���� ����
	public static final int LEN_MAX = 50000; //�ִ� ������ ����
	
	protected int protocolType;
	private byte[] packet;
	
	public Protocol() { //������
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
	    packet[0] = (byte)protocolType;	// packet ����Ʈ �迭�� ù ��° ����Ʈ�� �������� Ÿ�� ����
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