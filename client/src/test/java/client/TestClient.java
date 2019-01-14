package client;

import java.io.UnsupportedEncodingException;

import cn.dbw.api.PubAndSubClient.SubscribListener;
import cn.dbw.client.NettyPubAndSubClient;
import cn.dbw.dto.Message;



public class TestClient {
	
	public static void main(String[] args) {
		NettyPubAndSubClient client = NettyPubAndSubClient.getInstance();
		client.connect("127.0.0.1",9999);
//		client.auth("dbw", "123", new AutuListener() {
//			
//			@Override
//			public void authOk(Message message) {
//				System.out.println("ok");
//				
//			}
//			
//			@Override
//			public void authFail(Message message) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		client.subscribe("data", new SubscribListener() {
//			@Override
//			public void subOk(Message message) {
//				System.out.println("���ĳɹ�");
//			}
//			
//			@Override
//			public void msgActive(Message message) {
//				try {
//					System.out.println("�յ���Ϣ��"+new String(message.getData(),"utf-8"));
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		
		client.subscribe("mm", new SubscribListener() {
			@Override
			public void subOk(Message message) {
				System.out.println("���ĳɹ�");
			}
			
			@Override
			public void msgActive(Message message) {
				try {
					System.out.println("�յ���Ϣmm��"+new String(message.getData(),"utf-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		client.acceptBraodCast(new SubscribListener() {
			@Override
			public void msgActive(Message message) {
                   try {
					System.out.println("���չ㲥��Ϣ��"+new String(message.getData(),"utf-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		//client.publish("mm", "����");
		//client.shutdown();
	}

}
