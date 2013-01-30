package com.exadel.websockets.chat;


import com.exadel.websockets.chat.messages.ConnectionInfoMessage;
import com.exadel.websockets.chat.messages.MessageInfoMessage;
import com.exadel.websockets.chat.messages.StatusInfoMessage;
import com.google.gson.Gson;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.*;

/**
 * @author aa.ilyin
 */
@WebServlet
public class WebSocketCharServlet extends WebSocketServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketCharServlet.class);

    private static final Map<String, ChatConnection> connections = new HashMap<String, ChatConnection>();

    @Override
    protected StreamInbound createWebSocketInbound(String s, HttpServletRequest httpServletRequest) {
        final String connectionId = httpServletRequest.getSession().getId();
        final String userName = httpServletRequest.getParameter("userName");
        return new ChatConnection(connectionId, userName);
    }

    @Override
    protected boolean verifyOrigin(String origin){
        return true;
    }

    private static class ChatConnection extends MessageInbound {

        private final String connectionId;

        private final String userName;

        private final Gson jsonProcessor;

        private ChatConnection(String connectionId, String userName){
            this.connectionId = connectionId;
            this.userName = userName;
            this.jsonProcessor = new Gson();
        }

        @Override
        protected void onOpen(WsOutbound outbound){
            sendConnectionInfo(outbound);
            sendStatusInfoToOtherUsers(new StatusInfoMessage(userName, StatusInfoMessage.STATUS.CONNECTED));
            connections.put(connectionId, this);
        }

        private void sendConnectionInfo(WsOutbound outbound) {
            final List<String> activeUsers = getActiveUsers();
            final ConnectionInfoMessage connectionInfoMessage = new ConnectionInfoMessage(userName, activeUsers);
            try{
                outbound.writeTextMessage(CharBuffer.wrap(jsonProcessor.toJson(connectionInfoMessage)));
            } catch (IOException e){
                LOGGER.error("Impossible to send message", e);
            }

        }

        @Override
        protected void onClose(int status){
            sendStatusInfoToOtherUsers(new StatusInfoMessage(userName, StatusInfoMessage.STATUS.DISCONNECTED));
            connections.remove(connectionId);
        }

        private void sendStatusInfoToOtherUsers(StatusInfoMessage message){
            final Collection<ChatConnection> otherUsersConnections = getAllChatConnectionsExceptThis();
            for(ChatConnection chatConnection: otherUsersConnections){
                try{
                    chatConnection.getWsOutbound().writeTextMessage(CharBuffer.wrap(jsonProcessor.toJson(message)));
                } catch (IOException e){
                    LOGGER.error("Could not send the message", e);
                }
            }
        }

        @Override
        protected void onBinaryMessage(ByteBuffer byteBuffer) throws IOException {
            throw new UnsupportedOperationException("No support binary messages");
        }

        @Override
        protected void onTextMessage(CharBuffer charBuffer) throws IOException {
            final MessageInfoMessage message = jsonProcessor.fromJson(charBuffer.toString(), MessageInfoMessage.class);
            final ChatConnection destinationConnection = getDestinationUserConnection(message.getMessageInfo().getTo());
            if(destinationConnection != null){
                final CharBuffer jsonMessage = CharBuffer.wrap(jsonProcessor.toJson(message));
                destinationConnection.getWsOutbound().writeTextMessage(jsonMessage);
            } else {
                LOGGER.warn("Trying to send message to user not connected");
            }
        }

        private ChatConnection getDestinationUserConnection(String destinationUser) {
            for(ChatConnection connection: connections.values()){
                if(destinationUser.equals(connection.getUserName())){
                    return connection;
                }
            }
            return null;
        }

        public Collection<ChatConnection> getAllChatConnectionsExceptThis() {
            final Collection<ChatConnection> allConnections = connections.values();
            allConnections.remove(this);
            return allConnections;
        }

        private List<String> getActiveUsers() {
            final List<String> activeUsers = new ArrayList<String>();
            for (ChatConnection chatConnection : connections.values()) {
                activeUsers.add(chatConnection.getUserName());
            }
            return activeUsers;
        }

        public String getUserName() {
            return userName;
        }
    }
}