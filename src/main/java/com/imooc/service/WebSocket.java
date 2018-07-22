package com.imooc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author: icych
 * @description: 向浏览器推送消息
 * @date: Created on 22:43 2018/7/21
 */
@Component
@ServerEndpoint("/webSocket")
@Slf4j
public class WebSocket {

    private Session session;

    /**
     * WebSocket容器
     */
    private static CopyOnWriteArraySet<WebSocket> webSocketSet =
            new CopyOnWriteArraySet<>();

    /**
     * 监听客户端连接事件
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        log.info("[websocket消息] 有新的连接, 总数:{}", webSocketSet.size());
    }

    /**
     * 监听客户端断开连接事件
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        log.info("[websocket消息] 连接断开, 总数:{}", webSocketSet.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("[websocket消息]收到客户端发来的消息:{}", message);
    }

    /**
     * 向客户端发送消息
     *
     * @param message
     */
    public void send(String message) {
        for (WebSocket webSocket : webSocketSet) {
            log.info("[websocket消息] 广播消息, message={}", message);
            try {
                webSocket.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("[websocket消息] 发送消息异常,{}", e);
            }
        }
    }

}
