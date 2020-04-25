import java.net.*;
import java.io.*;
import java.util.*;

public class Hilo implements Runnable {

    private Socket socket;

    public Hilo(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter salida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Database conexionBBDD = new Database();

            switch (entrada.readLine()) {
                case "arduino":
                    Mensaje mensaje = new Mensaje();
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