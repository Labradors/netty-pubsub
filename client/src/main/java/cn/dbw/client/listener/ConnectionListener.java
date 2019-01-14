package cn.dbw.client.listener;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import cn.dbw.api.PubAndSubClient.AutuListener;
import cn.dbw.client.ChannelHolder;
import cn.dbw.client.NettyClient;
import cn.dbw.client.SubRecorder;
import cn.dbw.config.FuncodeEnum;
import cn.dbw.dto.Message;
import cn.dbw.po.LastLoginRecord;
import cn.dbw.util.DateUtils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import jodd.util.StringUtil;

public class ConnectionListener implements ChannelFutureListener {
	
	    private final  NettyClient CLIENT=NettyClient.INSTANCE;
        
	    private final Logger LOGGER=Logger.getLogger(ChannelFutureListener.class);
	
	    private  static   AtomicInteger retryCount=new AtomicInteger(0);

	    private final int TRY_LIMITE=15; //���������������

	    private int dalayTime=1; //��ʱ����ʱ��

		@Override
		public void operationComplete(ChannelFuture future) throws Exception {
	           if(!future.isSuccess()){
	               EventLoop eventExecutors = future.channel().eventLoop();
	               future.channel().close();
	               if(!eventExecutors.isShuttingDown()){
	               eventExecutors.schedule(()->{
	                   if(retryCount.get()<=TRY_LIMITE) {      
	                	   LOGGER.error("���ͻ���״̬��STATUS=failed,TIME="+ DateUtils.getCurrentDateTime()+",msg=���ڳ�������,retrys="+retryCount.getAndIncrement());
	                	   NettyClient.INSTANCE.start();
	                   }else{
	                	   NettyClient.INSTANCE.stop();
	                       LOGGER.error("���������桿�ѳ��������������������ر�");
	                   }
	               },dalayTime,TimeUnit.SECONDS);
	               dalayTime=dalayTime<<1;//��������Խ�࣬�ӳ�ʱ��Խ��
	               }
	           }else{
	        	   LOGGER.info("���ͻ���״̬��STATUS=ACTIVE,TIME="+ DateUtils.getCurrentDateTime());
	        	   ChannelHolder.setChannel(future.channel());
//	        	   //�ж��ϴ��Ƿ��½
//	        	   if(!StringUtil.isEmpty(LastLoginRecord.INSTANCE().getLastToken())){
//	        		   //��broker������֤ƾ֤
//	        		   System.out.println("���͵�½ƾ֤");
//	        		   future.channel().writeAndFlush(new Message(FuncodeEnum.AUTH_USER,(byte)0 , null, LastLoginRecord.INSTANCE().getLastToken().getBytes().length, LastLoginRecord.INSTANCE().getLastToken().getBytes()));
//	        	   }
	               //�������ɹ��ָ��������
	        	   SubRecorder.recover();
	               dalayTime=1;
	               retryCount.set(0);
	           }
			
		}
	    
	    
	
}
