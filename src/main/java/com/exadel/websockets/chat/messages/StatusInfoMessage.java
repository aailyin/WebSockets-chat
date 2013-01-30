package com.exadel.websockets.chat.messages;

/**
 * @author aa.ilyin
 */
public class StatusInfoMessage {

    public enum STATUS {CONNECTED, DISCONNECTED}

    private final StatusInfo statusInfo;

    public StatusInfoMessage(String user, STATUS status){
        this.statusInfo = new StatusInfo(user, status);
    }

    public StatusInfo getStatusInfo() {
        return statusInfo;
    }

    class StatusInfo {

        private final String user;

        private final STATUS status;

        public StatusInfo(String user, STATUS status){
            this.user = user;
            this.status = status;
        }

        public STATUS getStatus() {
            return status;
        }

        public String getUser() {
            return user;
        }
    }

}