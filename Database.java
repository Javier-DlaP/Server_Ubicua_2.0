import java.sql.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
import java.text.*;
import java.util.Date;

public class Database {

    private Connection conexion;
    private String url = "jdbc:postgresql://localhost:5432/postgres";
    private String user = "postgres";
    private String password = "postgres";

    public Database() {
        try {
            Class.forName("org.postgresql.Driver").newInstance();
            conexion = DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("No se ha podido conectar a la base de datos.");
        }
    }

    public void insertarValores(Objeto[] array) {
        Statement s = conexion.createStatement();

        for (Objeto streetlight: array) {
            String insert = "insert into \"DataDay\" values (" + streetlight.getId + ", ";
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            insert += "\'" + dtf.format(streetlight.date) + "\', ";
    
            float[] sensorArray = streetlight.getSensorArray();
            float[] lightArray = streetlight.getSensorArray();
    
            for (int i = 0; i < sensorArray.length && i < lightArray.length; i++) {
                if (i == sensorArray.length - 1 && i == lightArray.length - 1) {
                    insert += ((double) Math.round(sensorArray[i] * 10000d) / 10000d) + ", " + ((double) Math.round(lightArray[i] * 10000d) / 10000d) + ");";
                }
                else {
                    insert += ((double) Math.round(sensorArray[i] * 10000d) / 10000d) + ", " + ((double) Math.round(lightArray[i] * 10000d) / 10000d) + ", ";
                }
            }
            
            s.executeUpdate(insert);
        }
    }
}