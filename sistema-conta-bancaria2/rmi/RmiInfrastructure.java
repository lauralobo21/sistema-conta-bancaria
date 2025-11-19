package rmi;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class RmiInfrastructure {

    private static final int PACKET_SIZE = 4096; // Tamanho do buffer UDP

    public byte[] doOperation(String objectRef, int methodId, byte[] arguments) {
        try {
            DatagramSocket socket = new DatagramSocket();
            
            // 1. Empacotar a Mensagem (Serialização Java simples para o objeto Message wrapper)
            Message reqMsg = new Message(0, 1, objectRef, methodId, arguments);
            byte[] data = serializeMessage(reqMsg);

            // 2. Enviar (Request)
            InetAddress serverAddr = InetAddress.getByName("localhost");
            int serverPort = 12345;
            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddr, serverPort);
            socket.send(packet);

            // 3. Aguardar (Wait/Reply)
            byte[] buffer = new byte[PACKET_SIZE];
            DatagramPacket replyPacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(replyPacket);

            // 4. Desempacotar
            Message replyMsg = deserializeMessage(replyPacket.getData());
            socket.close();
            
            return replyMsg.arguments; // Retorna apenas o payload da resposta

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // --- LADO DO SERVIDOR
    public Message getRequest(DatagramSocket socket) throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[PACKET_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        
        // Fica bloqueado esperando chegar algo
        socket.receive(packet);

        // Deserializa para entender o cabeçalho
        Message msg = deserializeMessage(packet.getData());
        
        msg.objectReference += ":" + packet.getAddress().getHostAddress() + ":" + packet.getPort(); 
        return msg;
    }

    // --- LADO DO SERVIDOR  ---
    public void sendReply(byte[] replyData, InetAddress clientHost, int clientPort) {
        try {
            // Cria mensagem de resposta (Tipo 1 = Reply)
            Message replyMsg = new Message(1, 1, "Server", 0, replyData);
            byte[] data = serializeMessage(replyMsg);

            DatagramSocket socket = new DatagramSocket(); // Socket temporário para envio
            DatagramPacket packet = new DatagramPacket(data, data.length, clientHost, clientPort);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helpers de Serialização do Objeto "Message" (Envelope)
    private byte[] serializeMessage(Message msg) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        return bos.toByteArray();
    }

    private Message deserializeMessage(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (Message) ois.readObject();
    }
}