package cn.dbw.server.handler;


import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;


public class ChanelInitializerHandler extends ChannelInitializer {

	@Override
	protected void initChannel(Channel ch) throws Exception {
	       ChannelPipeline pipeline = ch.pipeline();
	       pipeline.addLast(new IdleStateHandler(0,0,35));
	       pipeline.addLast(new IdleStateTrigger());
	       //��ư��ĸ�ʽ 1�ֽڹ̶���ͷ  1�ֽڹ�����  1�ֽڣ��ж��Ƿ����topic�ֶΣ� 4�ֽڹ̶������ֶ�   12�ֽڹ̶�topic���Ǳ��룩  ʣ���ֽ�����
	       pipeline.addLast(new LengthFieldBasedFrameDecoder(2048, 3, 4, 0, 0));
	       pipeline.addLast(new MessageToPoDecoder());
	       //�����֤�Ĵ�����
	       //pipeline.addLast("auth",new AuthenticationHandler());
	       //���Э�鴦����
	       pipeline.addLast( "message-process", new MessageProcessHandler());
	       pipeline.addLast(new MessageEncoder());
	       //pipeline.addLast("auth",new AuthenticationHandler());
	       //pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
	}

}
