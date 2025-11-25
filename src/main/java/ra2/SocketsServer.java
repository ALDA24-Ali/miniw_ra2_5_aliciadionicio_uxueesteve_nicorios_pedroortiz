package ra2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketsServer {

    public static void main(String[] args) throws Exception {
        System.out.println("== SecureChat: Servidor Sockets RA3 ==\n");

        MessageBuffer buffer = new MessageBuffer();
        ServerSocket servidor = new ServerSocket(5000);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Hilo que procesa lo que llega al buffer
        executor.submit(() -> {
            try {
                while (true) {
                    String msg = buffer.take();
                    System.out.println(time() + " [PROCESSADOR] Msg -> " + msg);
                }
            } catch (Exception e) { e.printStackTrace(); }
        });

        // Aceptamos múltiples clientes
        while (true) {
            Socket client = servidor.accept();
            System.out.println(time() + " + Cliente conectado: " + client.getInetAddress());

            executor.submit(() -> {
                try {
                    BufferedReader entrada = new BufferedReader(
                            new InputStreamReader(client.getInputStream())
                    );
                    String msg;
                    while ((msg = entrada.readLine()) != null) {
                        buffer.put(msg);
                    }
                } catch (Exception e) { e.printStackTrace(); }
            });
        }
    }

    private static String time() {
        return LocalTime.now().toString();
    }
}
