package cn.dbw.exception;

public class IllegalDataHeaderException extends RuntimeException {
	
	public IllegalDataHeaderException(byte msg) {
		super("�Ƿ�������ͷ��->"+msg);
	}

}
