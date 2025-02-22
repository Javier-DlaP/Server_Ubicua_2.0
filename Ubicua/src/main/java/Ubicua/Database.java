package Ubicua;

import java.sql.*;
import java.util.*;
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

    public String selectFechasHistorico(int idFarola) throws SQLException, ParseException {
        Statement statement = conexion.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM public.\"DataHistoric\" where id_streetlight = " + idFarola);

        Date fechaMin;
        Date fechaMax;

        resultSet.next();

        fechaMin = resultSet.getDate("date");
        fechaMax = resultSet.getDate("date");

        while (resultSet.next()) {
            if (compararFechas(resultSet.getDate("date"), fechaMin) < 0) {
                fechaMin = resultSet.getDate("date");
            } else if (compararFechas(resultSet.getDate("date"), fechaMax) > 0) {
                fechaMax = resultSet.getDate("date");
            }
        }

        return ";" + fechaMin.toString() + ";" + fechaMax.toString() + ";";
    }

    public String selectDatosMedia(int idFarola) throws SQLException {
        String resultado = "";
        Statement statement = conexion.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM public.\"DataAverage\" where id_streetlight = " + idFarola);

        resultSet.next();

        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                resultado += ";" + resultSet.getFloat("sensor_0" + i + "_00") + ";" + resultSet.getFloat("light_0" + i + "_00") + ";" + resultSet.getFloat("sensor_0" + i + "_30") + ";" + resultSet.getFloat("light_0" + i + "_30");
            } else {
                resultado += ";" + resultSet.getFloat("sensor_" + i + "_00") + ";" + resultSet.getFloat("light_" + i + "_00") + ";" + resultSet.getFloat("sensor_" + i + "_30") + ";" + resultSet.getFloat("light_" + i + "_30");
            }
        }

        return resultado + ";";
    }

    public String selectDatosFecha(int idFarola, Date fecha) throws SQLException {
        String resultado = "";
        Statement statement = conexion.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM public.\"DataHistoric\" where id_streetlight = " + idFarola + " and date = \'" + fecha + "\'");

        resultSet.next();

        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                resultado += ";" + resultSet.getFloat("sensor_0" + i + "_00") + ";" + resultSet.getFloat("light_0" + i + "_00") + ";" + resultSet.getFloat("sensor_0" + i + "_30") + ";" + resultSet.getFloat("light_0" + i + "_30");
            } else {
                resultado += ";" + resultSet.getFloat("sensor_" + i + "_00") + ";" + resultSet.getFloat("light_" + i + "_00") + ";" + resultSet.getFloat("sensor_" + i + "_30") + ";" + resultSet.getFloat("light_" + i + "_30");
            }
        }

        return resultado + ";";
    }

    public ArrayList<Float> selectDatosMediaLuz(int idFarola) throws SQLException {
        ArrayList<Float> resultado = new ArrayList<>();
        Statement statement = conexion.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM public.\"DataAverage\" where id_streetlight = " + idFarola);

        resultSet.next();

        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                resultado.add(resultSet.getFloat("light_0" + i + "_00"));
                resultado.add(resultSet.getFloat("light_0" + i + "_30"));
            } else {
                resultado.add(resultSet.getFloat("light_" + i + "_00"));
                resultado.add(resultSet.getFloat("light_" + i + "_30"));
            }
        }

        return resultado;
    }

    public ArrayList<Float> selectDatosMediaSensor(int idFarola) throws SQLException {
        ArrayList<Float> resultado = new ArrayList<>();
        Statement statement = conexion.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM public.\"DataAverage\" where id_streetlight = " + idFarola);

        resultSet.next();

        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                resultado.add(resultSet.getFloat("sensor_0" + i + "_00"));
                resultado.add(resultSet.getFloat("sensor_0" + i + "_30"));
            } else {
                resultado.add(resultSet.getFloat("sensor_" + i + "_00"));
                resultado.add(resultSet.getFloat("sensor_" + i + "_30"));
            }
        }

        return resultado;
    }

    public void insertDatosFecha(Farola farola) throws SQLException {
        Calendar cal = Calendar.getInstance();
        Date fecha = cal.getTime();
        DateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        String fechaFinal = sdformat.format(fecha);
        String query = "INSERT INTO public.\"DataDay\" VALUES(";

        query += farola.getId() + ", ";
        query += "' " + fechaFinal + "', ";

        for (int i = 0; i < 48; i++) {
            query += Math.round(farola.getSensores()[i]*10000)/10000 + ", ";
            query += Math.round(farola.getLuces()[i]*10000)/10000;
            if (i != 47) query += ", ";
        }

        query += ")";

        PreparedStatement statement = conexion.prepareStatement(query);
        int affectedrows = 0;

        affectedrows = statement.executeUpdate();

        if (affectedrows == 1) System.out.println("Se ha guardado en la base de datos.");
        else System.out.println("Error al guardar en la base de datos");
    }

    private int compararFechas(Date fecha1, Date fecha2) throws ParseException {
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = sdformat.parse(fecha1.toString());
        Date d2 = sdformat.parse(fecha2.toString());

        return d1.compareTo(d2);
    }
}