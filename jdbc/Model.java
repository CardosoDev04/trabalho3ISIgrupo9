/*
 * @Author: Matilde Pato (mpato)
 * @Date: 2023-11-27 09:30:00
 * @Last Modified time: 2023-12-06 18:30:00
 * ISEL - DEETC
 * Introdução a Sistemas de Informação
 * MPato, 2023-2024
 * 
 * IMPORTANT:
 * This class should implement all the options listed in the statement. 
 * You may need to import more classes.
 */

package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Model {

    static String inputData(String str){
        // IMPLEMENTED
        java.util.Scanner key = new Scanner(System.in);
        System.out.print("Enter corresponding values, separated by commas of: \n" + str);
        String values = key.nextLine();
        return values;
    }

    static void addGPSDevice(GPSDevice device){
        //IMPLEMENTED
        final String INSERT_CMD = "INSERT INTO dispositivo VALUES (?,?,?,?)";
        try(
            Connection conn = DriverManager.getConnection(App.getInstance().getConnectionString());
            PreparedStatement pstmt = conn.prepareStatement(INSERT_CMD);
        ){
            conn.setAutoCommit(false);
            pstmt.setInt(1,device.getSerialNumber());
            pstmt.setDouble(2,device.getLatitude());
            pstmt.setDouble(3,device.getLongitude());
            pstmt.setDouble(4,device.getBattery());
            pstmt.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);

            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e){
            e.getMessage();
            System.out.println("Error on insert values");
            }    
        }

    /**
    To implement from this point forward. Do not need to change the code above.
    -------------------------------------------------------------------------------     
        IMPORTANT:
    --- DO NOT MOVE IN THE CODE ABOVE. JUST HAVE TO IMPLEMENT THE METHODS BELOW ---
    -------------------------------------------------------------------------------
    */
    

    static void addClassicBike(ClassicBike bike)
    {

        String query = "INSERT INTO CLASSICA (bicicleta, nomudanca) VALUES (?, ?)";
        try (
                Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
                PreparedStatement ps = con.prepareStatement(query);

        ) {
            ps.setInt(1, bike.getId());
            ps.setInt(2, bike.getNoMudanca());
            ps.executeUpdate();

            System.out.println("Classic bike registered!!!!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error on insert values");
        }
    }

    static void addElectricBike(ElectricBike bike)
    {
        String query = "INSERT INTO ELETRICA (id, autonomia, velocidade_max) VALUES (?, ?, ?)";

        try (
                Connection con = DriverManager.getConnection(App.getInstance().getConnectionString());
                PreparedStatement ps = con.prepareStatement(query);

        ) {         
            ps.setInt(1, bike.getId());
            ps.setDouble(2, bike.getAutonomia());
            ps.setDouble(3, bike.getVelocidadeMaxima());

            ps.executeUpdate();
            System.out.println("Electric bike registered!!!!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error on insert values");
        }
    }

}