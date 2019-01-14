package cn.dbw.config;

/**
 * ������
 * @author dbw
 *
 */
public enum FuncodeEnum {
	
	HEART_BEAT("����",(byte)1),
	AUTH_USER("�û���֤",(byte)2),
	MESSAGE_SEND("��Ϣ����",(byte)3),
	MESSAGE_BROAD("��Ϣ�㲥",(byte)11),
	ERROR_INFO("������Ϣ",(byte)4),
	TOPIC_SUBSCRIBE("��Ϣ����",(byte)5),
	TOPIC_UNSUBSCRIBE("ȡ������",(byte)6),
	NOTICE_SUBSCRIBE_OK("���ĳɹ�֪ͨ",(byte)7),
	NOTICE_UNSUBSCRIBE_OK("ȡ������֪ͨ",(byte)8),
	NOTICE_AUTH_OK("��֤�ɹ�֪ͨ",(byte)9),
	NOTICE_AUTH_FAIL("��֤ʧ��֪ͨ",(byte)10),
	;
	
	private String desc;//��������
	private Byte code;  //������

	
	private FuncodeEnum(String desc, Byte code) {
		this.desc = desc;
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Byte getCode() {
		return code;
	}
	public void setCode(Byte code) {
		this.code = code;
	}
	
	 /**
     * ������ ����code�ֶλ�ȡeumʵ��
     * @param type
     * @return
     */
    public static FuncodeEnum getEumInstanceByType(byte code){
        for(FuncodeEnum funcodeEnum:FuncodeEnum.values()){
            if(funcodeEnum.getCode()==code){
                return funcodeEnum;
            }
        }
        return  null;
    }
	
	
	

}
