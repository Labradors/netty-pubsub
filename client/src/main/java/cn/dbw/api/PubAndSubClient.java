package cn.dbw.api;

import cn.dbw.api.PubAndSubClient.SubscribListener;
import cn.dbw.dto.Message;

public interface PubAndSubClient {
	//�ͻ�������
	void connect(String host,Integer port);

	//�ͻ��˶�����Ϣ
	void subscribe(String topic,SubscribListener subscribListener);
	//�ͻ���ȡ������
	void unsubscribe(String topic);
	//�ͻ��˷�����Ϣ
	void publish(String topic,String str);
	//�ͻ��˹㲥
	void broadcast(String data);
	
	
	public static interface AutuListener extends Listener{
		//��֤�ɹ��ص�
		void  authOk(Message message);
        
		//��֤ʧ�ܻص�
		void authFail(Message message);
	}
	
	/**
	 * ���Ļص��ӿ�
	 * @author dbw
	 *
	 */
	public static interface SubscribListener extends Listener{
		//���ĳɹ��ص�
		default void subOk(Message message){};
		
		//����ʧ�ܻص�
//		default void subFail(Message message){};
		
		//��Ϣ����ص�
		default void msgActive(Message message){};
		
		//ȡ�����Ļص�
		default void unSubOk(Message message){};
	    
	}
	
	
	
	
	
	public static interface Listener{
		
	}





}
