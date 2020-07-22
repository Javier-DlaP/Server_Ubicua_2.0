package Ubicua;

import java.util.concurrent.*;
import java.net.*;

public class Main {

    private static ServerSocket serverSocket;
    private static CyclicBarrier barreraDatos;
    private static CyclicBarrier barreraMensajes;
    private static Datos datos;

    public static void main(String[] args) {
        try {
            int puerto = 30000;
            serverSocket = new ServerSocket(puerto);
            ExecutorService conexiones = Executors.newCachedThreadPool();
            barreraDatos = new CyclicBarrier(10);
            // barreraMensajes = new CyclicBarrier(x); x = nº arduino || nº ard + nº farolas
            datos = new Datos();
            System.out.println("Servidor encendido.");

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    Hilo conexion = new Hilo(socket, barreraDatos, barreraMensajes, datos);
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
