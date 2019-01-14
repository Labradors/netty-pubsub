package cn.dbw.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.dbw.config.ConfigFactory;
import cn.dbw.config.FuncodeEnum;
import cn.dbw.config.PropertyConfigFactory;
import cn.dbw.config.ServerConfig;
import cn.dbw.dto.Message;
import cn.dbw.po.LastLoginRecord;
import cn.dbw.po.MessageWrapper;
import cn.dbw.util.MD5Util;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import jodd.json.JsonObject;
import jodd.util.StringUtil;

/**
 * ��Ȩ��֤
 * @author dbw
 *
 */
public class AuthenticationHandler extends SimpleChannelInboundHandler<Message> {
	
	PropertyConfigFactory configFac=new PropertyConfigFactory();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
		//��ȡ�û���Ϣ
		byte[] data = msg.getData();
		String res = new String(data,"utf-8");
		//�ж��Ƿ��һ�ε�½
		if(res.startsWith("{")){
			JSONObject jsonObject = JSON.parseObject(res);
			System.out.println("�յ���Ϣ"+jsonObject.toString());
			if(null!=jsonObject){
				String username = jsonObject.getString("username");
				String password=jsonObject.getString("password");
				ServerConfig config = configFac.getConfig(ConfigFactory.filaPath);
				String value = config.getVerifiedAccount().getOrDefault(username, "");
				if(StringUtil.isEmpty(value)||!value.equals(password)){
					//��֤ʧ�ܣ�����ʧ����Ϣ�Ͽ�����
					//MessageWrapper messageWrapper = new MessageWrapper(FuncodeEnum.ERROR_INFO, "��֤ʧ��");
					ChannelFuture channelFuture = ctx.channel().writeAndFlush(new Message(FuncodeEnum.ERROR_INFO, (byte)0,null , "��֤ʧ��".getBytes().length, "��֤ʧ��".getBytes()));
					channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
						@Override
						public void operationComplete(Future<? super Void> future) throws Exception {
							// TODO Auto-generated method stub
							if(future.isSuccess()){
								ctx.close();
							}
						}
					});   
				}else{
					//��֤�ɹ�֮�󣬽��˺ź�����ƴ�ӽ���MD5��������token�����ظ��ͻ���
					String token=MD5Util.getPwd(username+password).substring(0, 16);
					//��¼��½��
					LastLoginRecord.INSTANCE().register(username, password);
					ChannelFuture channelFuture = ctx.channel().writeAndFlush(new Message(FuncodeEnum.NOTICE_AUTH_OK, (byte)0,null , token.getBytes().length, token.getBytes()));
					//��֤�ɹ������ܵ���Ϣ
					ctx.pipeline().addLast( "message-process", new MessageProcessHandler());
				}
				
			}	
		}else if(!res.equals("$FF$")){
			System.out.println("��һ�ε�½:"+res);
			//����ϴ��ѵ�½
			if(LastLoginRecord.INSTANCE().isLogin(res)){
				//��֤�ɹ������ܵ���Ϣ
				ctx.pipeline().addLast( "message-process", new MessageProcessHandler());
			}else{
				//��֤ʧ�ܶϿ�����
				ctx.close();
			}
		}
	
		ctx.pipeline().remove(this);
	}
	
	



}
