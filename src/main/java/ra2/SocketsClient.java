package ra2;

import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Scanner;

public class SocketsClient {

    public static void main(String[] args) throws Exception {
        System.out.println("== Client SecureChat RA3 ==\n");

        Socket socket = new Socket("localhost", 5000);
        PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("Escriu missatge: ");
            String msg = sc.nextLine();
            salida.println(LocalTime.now() + " Client -> " + msg);
        }
    }
}
