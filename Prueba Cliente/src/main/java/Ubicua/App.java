package Ubicua;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

/**
 * Hello world!
 *
 */
public class App {

    private static Socket socket;
    private static Socket socket2;
    private static BufferedReader entrada;
    private static BufferedWriter salida;
    private static BufferedReader entrada2;
    private static BufferedWriter salida2;

    public static void main(String[] args) {
        try {
            socket = new Socket("192.168.1.125", 30000);
            socket2 = new Socket("192.168.1.125", 30000);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            entrada2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
            salida2 = new BufferedWriter(new OutputStreamWriter(socket2.getOutputStream()));
            while (true) {
                salida.write("ARD:0,1,200;9,0,330;8,1,999");
                salida.newLine();
                salida.flush();

                System.out.println(entrada.readLine());

                Thread.sleep(2000);

                salida2.write("ARD:3,1,200");
                salida2.newLine();
                salida2.flush();

                System.out.println(entrada2.readLine());

                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
