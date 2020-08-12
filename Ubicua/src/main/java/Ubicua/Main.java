package Ubicua;

import java.util.concurrent.*;
import java.net.*;

public class Main {

    private static ServerSocket serverSocket;
    private static Datos datos;

    public static void main(String[] args) {
        try {
            int puerto = 30000;
            serverSocket = new ServerSocket(puerto);
            ExecutorService conexiones = Executors.newCachedThreadPool();
            datos = new Datos();
            System.out.println("Servidor encendido.");
            new HiloHora(datos).start();
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    Hilo conexion = new Hilo(socket, datos);
                    conexiones.execute(conexion);
                } catch (Exception e) {
                    System.out.println("No se ha podido conectar con el cliente o se ha desconectado.");
                }
            }
        } catch (Exception ex) {
            System.out.println("Conexión inválida.");
            try {
                serverSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
