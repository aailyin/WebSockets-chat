package com.exadel.websockets.chat.messages;

import java.util.List;

/**
 * @author aa.ilyin
 */
public class ConnectionInfoMessage {

    private final ConnectionInfo connectionInfo;

    public ConnectionInfoMessage(String user, List<String> activeUsers){
        this.connectionInfo = new ConnectionInfo(user, activeUsers);
    }

    public ConnectionInfo getConnectionInfo(){
        return connectionInfo;
    }

    class ConnectionInfo {

        private final String user;

        private final List<String> activeUsers;

        private ConnectionInfo(String user, List<String> activeUsers){
            this.activeUsers = activeUsers;
            this.user = user;
        }

        public String getUser(){
            return user;
        }

        public List<String> getActiveUsers(){
            return activeUsers;
        }
    }
}