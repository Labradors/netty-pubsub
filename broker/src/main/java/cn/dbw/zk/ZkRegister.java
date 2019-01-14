package cn.dbw.zk;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;

import cn.dbw.container.MemoryTopicHoder;
import cn.dbw.server.BrokeServer;

public class ZkRegister {
	
	/** zookeeper��Ⱥ��ַ */
	static final String CONNECT_ADDR_CLUSTER = "192.168.1.31:2181,192.168.1.32:2181,192.168.1.33:2181";
	/** zookeeper���ڵ��ַ */
	static final String CONNECT_ADDR_SINGLE="127.0.0.1:2181";
	/** session��ʱʱ�� */
	static final int SESSION_OUTTIME = 10000;//ms
	
	static final String ROOT_PATH="/broker_active";
	
	private final Logger LOGGER=Logger.getLogger(ZkRegister.class);
	
	private ZkClient zkc=null;
	
	private String currentZKPath=null;
	
	
	private static volatile ZkRegister zkRegister=null;
	
	
	
	private ZkRegister() {
	}
	
	
	public static ZkRegister getInstance(){
		synchronized (ZkRegister.class) {
			if(zkRegister==null){
				synchronized (ZkRegister.class) {
					zkRegister=new ZkRegister();
				}
			}
			return zkRegister;
		}
	}
		
	/**
	 * zookeeper�ڵ�ע��
	 * @param path  �ӽڵ����·��
	 * @param data  �ӽڵ�ip:port
	 */
	public void register(String path,Object data){
		  zkc=new ZkClient(new ZkConnection(CONNECT_ADDR_SINGLE),SESSION_OUTTIME);
		  if(!zkc.exists(ROOT_PATH)){
	        	zkc.createPersistent(ROOT_PATH,"brokerList");
	        	LOGGER.info("��zk�ڵ��ʼ���ɹ���");
	        };
	        //������ʱ�ڵ�
	        String createEphemeralSequential = zkc.createEphemeralSequential(path, data);
	        //���õ�ǰzk·��
	        this.currentZKPath=createEphemeralSequential;
	        LOGGER.info("��brokerע�᡿path->"+createEphemeralSequential+" data->"+data+" status=SUCCESS");
	}
	
	public void recordZkTopic(String path,String topic){
		String createPath=path+"/"+topic;
		if(!zkc.exists(createPath))
		   zkc.createEphemeral(path+"/"+topic);  
	}
	
	public void removeZkTopicRecord(String path,String topic){
		zkc.delete(path+"/"+topic);
	}
	
	public String getCurrentZKPath() {
		return currentZKPath;
	}
   
	public void close(){
		if(zkc!=null){
			zkc.close();
		}
	}
	
	
	public static void main(String[] args) {
		ZkClient client=new ZkClient(new ZkConnection(CONNECT_ADDR_SINGLE),SESSION_OUTTIME);
		List<String> children = client.getChildren("/broker_active");
		children.forEach((rs)->{
			Object readData = client.readData("/broker_active/"+rs);
			System.out.println(readData);
		});	
	}
	

}
