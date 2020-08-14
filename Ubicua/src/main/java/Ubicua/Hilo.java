package Ubicua;

import java.net.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;
import java.util.concurrent.CyclicBarrier;

public class Hilo implements Runnable {
    private Socket socket;
    private CyclicBarrier barreraMensajes;
    private Datos datos;

    public Hilo(Socket socket, Datos datos) {
        this.socket = socket;
        this.datos = datos;
    }

    private String mensajeAEnviarArduino(ArrayList<Integer> ids){
        String mensaje = "";
        for (int i = 0; i < ids.size(); i++) {
            mensaje += datos.getIntensidad(i, datos.getIdHora());
            
            if (i != ids.size() - 1) mensaje += ",";
        }
        return mensaje;
    }

    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter salida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Database conexionBBDD = new Database();

            boolean esApp = false;

            while (!esApp) {
                String mensajeRecibido = entrada.readLine();
                StringTokenizer st = new StringTokenizer(mensajeRecibido, ":");
                String tipoMensaje = st.nextToken();
                String interiorMensaje = st.nextToken();

                switch (tipoMensaje) {
                    // En caso de que reciba un mensaje del arduino
                    case "ARD":
                        StringTokenizer st2 = new StringTokenizer(interiorMensaje, ";");
                        ArrayList<Mensaje> farolas = new ArrayList<>();
                        ArrayList<Integer> ids = new ArrayList<>();

                        barreraMensajes = new CyclicBarrier(1+st2.countTokens()); //x = nº arduino || nº ard + nº farolas
                        
                        while (st2.hasMoreTokens()) {
                            String aux = st2.nextToken();
                            farolas.add(new Mensaje(aux, datos, barreraMensajes));

                            StringTokenizer st3 = new StringTokenizer(aux, ",");
                            ids.add(Integer.parseInt(st3.nextToken()));
                        }
                        
                        for (Mensaje farola: farolas) {
                            farola.start();
                        }

                        // Barrera de espera para posteriormente recoger los datos del objeto Datos y enviarselo al arduino
                        barreraMensajes.await();

                        salida.write(mensajeAEnviarArduino(ids));
                        salida.newLine();
                        salida.flush();

                        break;
                        
                    // En caso de que reciba un mensaje de la aplicacion
                    case "APP":
                        esApp = true;
                        StringTokenizer st3 = new StringTokenizer(interiorMensaje, ";");
                        String accion = st3.nextToken();

                        switch (accion) {
                            case "conexion inicial":
                                salida.write("conectado;");
                                salida.newLine();
                                salida.flush();

                                // Implementar bucle para que la conexion se quede abierta y envie el estado actual de las farolas a la aplicacion
                                break;
                            case "recibir fechas farola":
                                int idFarola = Integer.parseInt(st3.nextToken());
                                MensajeAplicacion aplicacion = new MensajeAplicacion(idFarola);
                                salida.write(aplicacion.obtenerFechasHistorico());
                                salida.newLine();
                                salida.flush();
                                break;
                            case "recibir datos media":
                                int idFarola2 = Integer.parseInt(st3.nextToken());
                                MensajeAplicacion aplicacion2 = new MensajeAplicacion(idFarola2);
                                salida.write(aplicacion2.obtenerDatosMedia());
                                salida.newLine();
                                salida.flush();
                                break;
                            case "recibir datos fecha":
                                int idFarola3 = Integer.parseInt(st3.nextToken());
                                Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(st3.nextToken());
                                MensajeAplicacion aplicacion3 = new MensajeAplicacion(idFarola3, fecha);
                                salida.write(aplicacion3.obtenerDatosFecha());
                                salida.newLine();
                                salida.flush();
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
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