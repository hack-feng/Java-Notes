# SpringBoot整合WebSocket

## WebSocket简介

WebSocket 是 HTML5 开始提供的一种在单个 TCP 连接上进行全双工通讯的协议。
   
WebSocket 使得客户端和服务器之间的数据交换变得更加简单，允许服务端主动向客户端推送数据。在 WebSocket API 中，浏览器和服务器只需要完成一次握手，两者之间就直接可以创建持久性的连接，并进行双向数据传输。
   
在 WebSocket API 中，浏览器和服务器只需要做一个握手的动作，然后，浏览器和服务器之间就形成了一条快速通道。两者之间就直接可以数据互相传送。
   
现在，很多网站为了实现推送技术，所用的技术都是 Ajax 轮询。轮询是在特定的的时间间隔（如每1秒），由浏览器对服务器发出HTTP请求，然后由服务器返回最新的数据给客户端的浏览器。这种传统的模式带来很明显的缺点，即浏览器需要不断的向服务器发出请求，然而HTTP请求可能包含较长的头部，其中真正有效的数据可能只是很小的一部分，显然这样会浪费很多的带宽等资源。
   
HTML5 定义的 WebSocket 协议，能更好的节省服务器资源和带宽，并且能够更实时地进行通讯。

## Java端实现

1. 在pom.xml引入所需依赖 
~~~
<!-- 引入websocket -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
~~~

2. 如果是你采用springboot内置容器启动项目的，则需要配置一个Bean。如果是采用外部的容器，则可以不需要配置
这里使用springboot内置容器启动项目，创建<WebSocketConfig.java>
~~~java
package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 开启WebSocket支持
 * @author ZhangFZ
 * @date 2020/9/29 10:29
 **/
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
~~~

3. 编写自定义的推送对象类型<MessageVo.java>，不同的项目根据自己的实际需要进行修改，这里只是简单的代码，省略了部分业务代码
~~~java
package bean;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统全局-系统消息表
 * </p>
 *
 * @author ZhangFz
 * @since 2020-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Message对象", description="系统全局-系统消息表")
public class MessageVo{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "接收人id")
    private Long receiverId;

    @ApiModelProperty(value = "发送人id")
    private Long createId;

    @ApiModelProperty(value = "消息标题")
    private String title;

    @ApiModelProperty(value = "消息内容")
    private String content;
}
~~~

4. 编写WebSocketServer.java的服务类类
~~~java
package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import bean.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServerEndpoint 这个注解用于标识作用在类上
 * 它的主要功能是把当前类标识成一个WebSocket的服务端
 * 注解的值用户客户端连接访问的URL地址
 * @author ZhangFZ
 * @date 2020/9/29 10:30
 **/
@ServerEndpoint("/socket/{userId}")
@Component
@Slf4j
public class WebSocketServer {
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。这里暂不做考虑*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId = null;

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) {
        this.session = session;
        this.userId=userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
            //加入set中
        }else{
            webSocketMap.put(userId,this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        try {
            sendMessageText("连接成功");
        } catch (IOException e) {
            log.error("用户:"+userId+",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message) {
        log.info("用户消息:"+userId+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人(防止串改)
                jsonObject.put("fromUserId",this.userId);
                String toUserId=jsonObject.getString("toUserId");
                //传送给对应toUserId用户的websocket
                if(toUserId != null && webSocketMap.containsKey(toUserId)){
                    webSocketMap.get(toUserId).sendMessageText(jsonObject.toJSONString());
                }else{
                    log.error("请求的userId:"+toUserId+"不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    private void sendMessageText(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 实现服务器主动推送
     * MessageVo 自定义推送的消息实体
     */
    private void sendMessage(MessageVo message) throws IOException {
        this.session.getBasicRemote().sendText(JSONObject.toJSONString(message));
    }

    /**
     * 发送自定义消息
     * MessageVo 自定义推送的消息实体
     */
    public void sendInfo(MessageVo messageInfo) {
        if(messageInfo.getReceiverId() == null){
            return;
        }
        if(messageInfo.getReceiverId() != null && webSocketMap.containsKey(String.valueOf(messageInfo.getReceiverId()))){
            try {
                webSocketMap.get(String.valueOf(messageInfo.getReceiverId())).sendMessage(messageInfo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            log.error("用户"+ messageInfo.getReceiverId()+",不在线！");
        }
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
~~~

## 客户端实现

摘抄自菜鸟教程：https://www.runoob.com/html/html5-websocket.html
~~~html
<!DOCTYPE HTML>
<html lang="zh-cn">
   <head>
   <meta charset="utf-8">
   <title>websocket测试</title>
    
      <script type="text/javascript">
         function WebSocketTest()
         {
            if ("WebSocket" in window)
            {
               alert("您的浏览器支持 WebSocket!");
               
               // 打开一个 web socket
               var ws = new WebSocket("ws://localhost:9998/websocket/1");
                
               ws.onopen = function()
               {
                  // Web Socket 已连接上，使用 send() 方法发送数据
                  ws.send("发送数据");
                  alert("数据发送中...");
               };
                
               ws.onmessage = function (evt) 
               { 
                  var received_msg = evt.data;
                  alert("数据已接收...");
               };
                
               ws.onclose = function()
               { 
                  // 关闭 websocket
                  alert("连接已关闭..."); 
               };
            }
            
            else
            {
               // 浏览器不支持 WebSocket
               alert("您的浏览器不支持 WebSocket!");
            }
         }
      </script>
        
   </head>
   <body>
   
      <div id="sse">
         <a href="javascript:WebSocketTest()">运行 WebSocket</a>
      </div>
      
   </body>
</html>
~~~

