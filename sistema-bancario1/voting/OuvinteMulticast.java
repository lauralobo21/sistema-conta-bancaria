package voting;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import com.google.gson.Gson; // <-- ADICIONE ESTA LINHA DE VOLTA
import voting.NotaInformativa;

public class OuvinteMulticast implements Runnable {
    
    @Override
    public void run() {
        Gson gson = new Gson(); 
        
        try (MulticastSocket socket = new MulticastSocket(6789)) {
            InetAddress group = InetAddress.getByName("230.0.0.0");
            socket.joinGroup(group);

            System.out.println(">>> Ouvinte de notas informativas iniciado <<<");
            byte[] buffer = new byte[1024];
            
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet); 
                
                String jsonNota = new String(packet.getData(), 0, packet.getLength());
                
                NotaInformativa nota = gson.fromJson(jsonNota, NotaInformativa.class); // <-- Precisa
                
                System.out.println("\n--- AVISO DO ADMIN ---");
                System.out.println("> " + nota.getMensagem());
                System.out.println("------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}