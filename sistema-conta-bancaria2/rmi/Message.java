package rmi;

import java.io.Serializable;

public class Message implements Serializable {
    public int messageType;
    public int requestId; 
    public String objectReference;
    public int methodId;   
    public byte[] arguments; 

    public Message(int messageType, int requestId, String objectRef, int methodId, byte[] args) {
        this.messageType = messageType;
        this.requestId = requestId;
        this.objectReference = objectRef;
        this.methodId = methodId;
        this.arguments = args;
    }
}