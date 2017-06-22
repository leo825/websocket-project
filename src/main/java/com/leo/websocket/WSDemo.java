package com.leo.websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.CopyOnWriteArraySet;


@ServerEndpoint("/myecho")
public class WSDemo {

//    Logger log = Logger.getLogger(getClass());

    //当前在线人数
    private static int onlineCount = 0;
    //用一个set集合保存几个websocket实例
    private static CopyOnWriteArraySet<WSDemo> wsSet = new CopyOnWriteArraySet<WSDemo>();
    //websocket的session
    private Session session;

    /**
     * 客户端新建websocket时会触发(握手协议后)
     * 并加入当前的set集合中
     * @param session
     */
    @OnOpen
    public void wsOpen(Session session) {
        this.session = session;
        wsSet.add(this);//加入集合
        // 在线人数加1
        addOnlineCount();
    }

    //当websocket退出的时候触发，并在set集合中删除当前websocket
    @OnClose
    public void wsClose(){
        wsSet.remove(this); //删除
        //在线人数-1
        subOnlineCount();
    }

    /**
     * 接收到客户端发来的消息并处理，同时也像客户端发送消息
     * @param message
     * @param session
     */
    @OnMessage
    public void wsMessage(String message, Session session) {
        sendMessage(message);
        System.out.println("=====客户端发来消息:" + message);
        System.out.println("======websocket 数量:" + wsSet.size());
        //群发消息
        for(WSDemo wss: wsSet) {
            wss.sendMessage("服务端发来的消息"); //向客户端发送消息
        }
    }

    //websocket错误的时候丢出一个异常
    @OnError
    public void wsError(Session session, Throwable throwable) {
        throw new IllegalArgumentException(throwable);
    }

    //send message  发送消息处理方法
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
            System.out.println("===============发送了消息:" + message);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        }
    }

    // get onlinecount
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    // +1
    public static synchronized void addOnlineCount() {
        WSDemo.onlineCount++;
        System.out.println("++++++++++++++上线人数+1:" + onlineCount);
    }

    //-1
    public static synchronized void subOnlineCount() {
        WSDemo.onlineCount--;
        System.out.println("---------------线上人数-1:" + onlineCount);
    }

}