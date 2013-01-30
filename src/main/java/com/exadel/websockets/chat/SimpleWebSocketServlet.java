package com.exadel.websockets.chat;

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

/**
 * @author aa.ilyin
 */
@WebServlet(urlPatterns = "/websockets", loadOnStartup = 1)
public class SimpleWebSocketServlet extends WebSocketServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleWebSocketServlet.class);

    @Override
    protected StreamInbound createWebSocketInbound(String s, HttpServletRequest httpServletRequest) {
        return new WebSocketConnection();
    }

    @Override
    protected boolean verifyOrigin(String origin){
        LOGGER.trace("Origin: ", origin);
        return true;
    }

    private static class WebSocketConnection extends MessageInbound {

        @Override
        protected void onOpen(WsOutbound outbound){
            LOGGER.info("Connection is opened");
        }

        @Override
        protected void onClose(int status){
            LOGGER.info("Connection is closed");
        }

        @Override
        protected void onBinaryMessage(ByteBuffer byteBuffer) throws IOException {
            LOGGER.warn("No support binary messages");
            throw new UnsupportedOperationException("No support binary messages");
        }

        @Override
        protected void onTextMessage(CharBuffer charBuffer) throws IOException {
            final String user = charBuffer.toString();
            LOGGER.debug("Message was received: ", user);
            getWsOutbound().writeTextMessage(CharBuffer.wrap("Welcome "+user+" from WebSocket"));
        }
    }

}