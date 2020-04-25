import java.util.concurrent.*;
import java.net.*;

public class Servidor {

    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            int puerto = 0;
            serverSocket = new ServerSocket(puerto);
            ExecutorService conexiones = Executors.newCachedThreadPool();

            System.out.println("Servidor encendido.");

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    Hilo conexion = new Hilo(socket);
                    conexiones.execute(conexion);
                } catch (Exception e) {
                    System.out.println("No se ha podido conectar con el cliente o se ha desconectado.");
                }
            }
        } catch (Exception ex) {
            System.out.println("Conexión inválida.");
            try {
                serverSocket.close();
            } catch (Exception e) {}
        }
    }
}