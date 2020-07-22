package Ubicua;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class Hilo implements Runnable {
    private Socket socket;
    private CyclicBarrier barreraDatos;
    private CyclicBarrier barreraMensajes;
    private Datos datos;

    public Hilo(Socket socket, CyclicBarrier barreraDatos, CyclicBarrier barreraMensajes, Datos datos) {
        this.socket = socket;
        this.barreraDatos = barreraDatos;
        this.barreraMensajes = barreraMensajes;
        this.datos = datos;
    }

    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter salida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Database conexionBBDD = new Database();

            String mensajeRecibido = entrada.readLine();
            StringTokenizer st = new StringTokenizer(mensajeRecibido, ":");
            String tipoMensaje = st.nextToken();
            String interiorMensaje = st.nextToken();

            switch (tipoMensaje) {
                // En caso de que reciba un mensaje del arduino
                case "ARD":
                    StringTokenizer st2 = new StringTokenizer(interiorMensaje, ";");
                    ArrayList<Mensaje> farolas = new ArrayList<>();

                    while (st2.hasMoreTokens()) {
                        farolas.add(new Mensaje(st2.nextToken(), datos, barreraDatos));
                    }

                    for (Mensaje farola: farolas) {
                        farola.start();
                    }

                    // Barrera de espera para posteriormente recoger los datos del objeto Datos y enviarselo al arduino + insertar en base de datos
                    barreraMensajes.await();
                    
                // En caso de que reciba un mensaje de la aplicacion
                case "APP":
                    System.out.println(interiorMensaje);
                    salida.write("prueba");
                    salida.newLine();
                    salida.flush();
                    break;
                default:
                    break;
            }
            entrada.close();
            salida.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}