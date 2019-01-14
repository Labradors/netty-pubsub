package cn.dbw.client.handler;



import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;


import java.util.Date;

import cn.dbw.client.ChannelHolder;
import cn.dbw.client.NettyClient;
import cn.dbw.config.FuncodeEnum;
import cn.dbw.dto.Message;
import cn.dbw.util.DateUtils;


@ChannelHandler.Sharable
public class IdleStateTrigger extends ChannelInboundHandlerAdapter {

   
	private final static NettyClient CLIENT=NettyClient.INSTANCE;

    //private static  final ByteBuf HEARTBEATE= Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("$FF$", CharsetUtil.UTF_8));
    private static final  Message HEARTBEATE=new Message(FuncodeEnum.HEART_BEAT, (byte)0,null,"$FF$".getBytes().length , "$FF$".getBytes());
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            switch (state){
                case READER_IDLE:
                	 //�����¼�����������
                    //System.out.println("����������\t"+ DateUtils.getCurrentDateTime());
                    //ctx.channel().writeAndFlushֱ�Ӵ�outβ�����һ��handler��ջ
                    //ctx.writeAndFlush �ӵ�ǰ����һ����β�˳�ջ
                    ChannelFuture channelFuture = ctx.channel().writeAndFlush(HEARTBEATE.clone());
                    channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                    break;
            }
        }else{
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	 if(!ctx.executor().isShuttingDown()){
    		 System.out.println("�����ѶϿ���������\t"+ DateUtils.getCurrentDateTime());
    		 super.channelInactive(ctx);
    	     ChannelHolder.setChannel(null);
    	     CLIENT.start();
    	 }else{
    		 System.out.println("ϵͳ���ڹر�...");
    	 }
    }
}
