package cn.dbw.disruptor;

import com.lmax.disruptor.RingBuffer;

import cn.dbw.dto.Message;
import cn.dbw.po.MessageWrapper;
import io.netty.channel.ChannelHandlerContext;

public class MessageProducer {
	
	
	private String producerId;
	
	private RingBuffer<MessageWrapper> ringBuffer;

	public MessageProducer(String producerId, RingBuffer<MessageWrapper> ringBuffer) {
		this.producerId = producerId;
		this.ringBuffer = ringBuffer;
	}
	
	public void onData(ChannelHandlerContext ctx,Message message){
		//��ȡ��һ�����õ����к�
		long sequence = ringBuffer.next();
		try {
			//��ȡ���������
			MessageWrapper messageWrapper = ringBuffer.get(sequence);
			messageWrapper.setCtx(ctx);
			messageWrapper.setMessage(message);
		} finally {
			//��������
			ringBuffer.publish(sequence);
		}
	}

}
