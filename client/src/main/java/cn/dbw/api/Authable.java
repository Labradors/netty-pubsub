package cn.dbw.api;

import cn.dbw.api.PubAndSubClient.AutuListener;

public interface Authable {

    //�ͻ�����֤
	void auth(String username,String password,AutuListener autuListener);
}
