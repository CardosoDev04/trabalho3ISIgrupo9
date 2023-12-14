/*
 * @Author: Matilde Pato (mpato)
 * @Date: 2023-11-27 09:30:00
 * @Last Modified time: 2023-11-27 09:30:00
 * ISEL - DEETC
 * Introdução a Sistemas de Informação
 * MPato, 2023-2024
 * 
 * NOTA:
 * This class requires that all constraints outlined in the instructions are met. 
 * Constraints described in the statement that have not been implemented in the DB.
 * For instance:
 * 1. System of bicycle gears, which can take {1, 6, 18, 24} as values. 
 *  It can be NULL if CLASSICA has no gears. The description can be changed or added to.
 * 2. Maintenance bike will not be available. 
 */

package jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Restriction {
   static boolean inState(String state, Connection con, int bikeID){

       String currentState = getCurrentState(con,bikeID);

        switch (state) {
            case "em manutencao" -> {
                return currentState.equals("em manutencao");
            }
            case "ocupado" -> {
                return currentState.equals("ocupado");
            }
            case "livre" -> {
                return currentState.equals("livre");
            }
        }
        return false;
    }

    static String getCurrentState(Connection con, int bikeID){
        String currentState = "";
        String query = "SELECT estado FROM BICICLETA WHERE id = ?";

        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, bikeID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    currentState = rs.getString("estado");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return currentState;
    }

    static void setCurrentState(Connection con, int bikeID, String newState) {
        String updateQuery = "UPDATE BICICLETA SET estado = ? WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(updateQuery)) {
            ps.setString(1, newState);
            ps.setInt(2, bikeID);
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Bike state updated successfully to 'em manutençao'.");
            } else {
                System.out.println("Error: Bike state not updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    static boolean brandExists(String brand, Connection con){
        String query = "SELECT marca FROM BICICLETA WHERE marca = ?";

        try(PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, brand);
            try (ResultSet rs = ps.executeQuery()) {
                if(rs != null){
                    return true;
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}