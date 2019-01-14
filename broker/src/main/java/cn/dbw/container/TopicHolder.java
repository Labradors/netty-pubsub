package cn.dbw.container;

import java.util.Map;
import java.util.Set;

import io.netty.channel.Channel;

public interface TopicHolder {
	
	//ɾ������
    void remove(byte[] topic);
    //ȡ�����������������
    void remove(byte[] topic,Channel channel);
    //ͨ��channel �Ƴ��������������Ƴ�
    void remove(Channel channel);
    
    //��������
    void subscribe(byte[] topic,Channel channel);
    
    
    //��ȡ����
    Set<Channel> getTopic(byte [] topic);
    
    //��ȡ��������
    public Map<String, Set<Channel>> getTopicContainner();
    
    
	

}
