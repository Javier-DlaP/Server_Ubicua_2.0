package Ubicua;

import java.sql.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
import java.text.*;
import java.util.Date;

public class Database {

    private Connection conexion;
    private String url = "jdbc:postgresql://localhost:5432/street_light_monitoring";
    private String user = "postgres";
    private String password = "postgres";

    public Database() {
        try {
            conexion = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            // Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("No se ha podido conectar a la base de datos.");
        }
    }

    /*public void insertarValores(Objeto[] array) {
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

            PreparedStatement statement = conexion.prepareStatement(insert);
            statement.executeUpdate();
        }
    }*/

    /* Pruebas para la conexion con base de datos
    public static void ejemploSelect() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/TIENDA", "postgres", "postgres");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM public.\"Tienda\"");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("Id_tienda") + resultSet.getString("Nombre"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ejemploInsert() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/TIENDA", "postgres", "postgres");
            PreparedStatement statement = connection.prepareStatement("UPDATE public.\"Tienda\" SET \"Provincia\" = ? WHERE \"Id_tienda\" = ?");
            int affectedrows = 0;

            statement.setString(1, "Valencia");
            statement.setInt(2, 1);

            affectedrows = statement.executeUpdate();

            System.out.println(affectedrows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
}