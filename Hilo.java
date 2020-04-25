import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class Hilo implements Runnable {

    private Socket socket;
    private CyclicBarrier barreraDatos;

    public Hilo(Socket socket, CyclicBarrier barreraDatos) {
        this.socket = socket;
        this.barreraDatos = barreraDatos;
    }

    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter salida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Database conexionBBDD = new Database();

            switch (entrada.readLine()) {
                case "arduino":
                    Mensaje mensaje = new Mensaje(barreraDatos);
                    while (true) {

                    }
                case "usuario":

                    break;
                default:
                    break;
            }
            entrada.close();
            socket.close();
        } catch (Exception e) {}
        finally {
            try {
                socket.close();
            } catch (Exception e) {}
        }
    }
}