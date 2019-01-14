package cn.dbw.client.handler;

import cn.dbw.dto.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Message> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
		out.writeByte(msg.getBODY_HEAD());
		out.writeByte(msg.getFunCode().getCode());
		out.writeByte(msg.getIsHaveTopic());
		if(msg.getIsHaveTopic()==1){
			out.writeInt(msg.getBodyLength()+12);//����topic�ֶι̶�����12
			out.writeBytes(msg.getTopic());
		}else{
			out.writeInt(msg.getBodyLength());
		}
		out.writeBytes(msg.getData());
		//System.out.println("�ͻ��˷���"+msg);
		//System.out.println("���ģ�"+out);
	}

}
