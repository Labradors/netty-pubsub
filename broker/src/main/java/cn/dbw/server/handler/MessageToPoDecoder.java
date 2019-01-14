package cn.dbw.server.handler;

import java.io.IOException;
import java.util.List;

import cn.dbw.config.FuncodeEnum;
import cn.dbw.dto.Message;
import cn.dbw.exception.IllegalDataHeaderException;
import cn.dbw.util.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ReplayingDecoder;

public class MessageToPoDecoder extends ReplayingDecoder<Void> {
	
	private final byte BODY_HEAD=(byte) 0xA8;
    
	 //Э���ʽ 1�ֽڹ̶���ͷ  1�ֽڹ�����  1�ֽڣ��ж��Ƿ����topic�ֶΣ� 4�ֽڹ̶������ֶ�   12�ֽڹ̶�topic���Ǳ��룩  ʣ���ֽ�����
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		//System.out.println("�������յ�");
		byte fixHead = in.readByte();//��ȡ�̶�ͷ�� 0xA8
		if(!checkHead(fixHead)){
			System.out.println("�������յ�ͷ����"+fixHead);
			ctx.channel().close();
			throw new IllegalDataHeaderException(fixHead);
		}
		byte funCode = in.readByte();//��ȡ������
		byte ishaveTopic = in.readByte(); //��ȡ�ж��ֶ�
		int bodyLength=in.readInt(); //��ȡ�̶����ݳ����ֶ� 
	    byte[] topic=null;
	    byte[] body=null;
	    if(ishaveTopic==1){
	    	topic=new byte[12];
	    	in.readBytes(topic);
	    	bodyLength=bodyLength-12; //����topic�����ֶ� ���ȡbody���ֶγ���-12
	    }
//	    int readableBytes = in.readableBytes();//��ȡʣ��ɶ���
//	    System.out.println("������ʣ��ɶ�ȡ��"+readableBytes);
	    	body=new byte[bodyLength];
	    	in.readBytes(body);
	    //System.out.println("���MSG�����룺"+FuncodeEnum.getEumInstanceByType(funCode));
	    out.add(new Message(FuncodeEnum.getEumInstanceByType(funCode), ishaveTopic, topic,bodyLength, body));
		
//		
//		FuncodeEnum funcodeEnum = FuncodeEnum.getEumInstanceByType(funCode);
//		switch(funcodeEnum){
//		   case HEART_BEAT:
//			   break;
//		   case AUTH_USER:
//			   ChannelPipeline pipeline = ctx.pipeline();
//			   pipeline.addLast("auth",new AuthenticationHandler());
//		   case MESSAGE_SEND:
//			   int length = in.readInt();
//			   ByteBuf byteBuf = in.readBytes(length);
//			   byte[] data = byteBuf.array();
//			   //�����л�dataתΪmessage po
//			   Message message = SerializationUtil.deserialize(data, Message.class);
//			   out.add(message);
//			   break;
//				   
//		}
		
	}
	
	
	/**
	 * ��ͷУ��
	 * @param b
	 * @return
	 */
	private boolean checkHead(byte b){
		if(b==BODY_HEAD)
			return true;
		else 
			return false;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		// TODO Auto-generated method stub
//		super.exceptionCaught(ctx, cause);
        if(cause instanceof IOException)
		    System.err.println(ctx.channel().remoteAddress()+" "+cause.getMessage());
        else
        	cause.printStackTrace();
	}
}
