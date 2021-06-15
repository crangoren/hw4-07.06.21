package Lessons;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class DataBaseApp {

    private static Connection connection;
    private static Connection connectionHistory;
    private static Statement stmt;
    private static PreparedStatement psInsert;
    private static PreparedStatement stringAdd;

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        connect();
        createDB();
//        createHistoryDB();
        fillTableBatch();
        CloseDB();
//        fillHistory();
    }



    public static void connect() throws ClassNotFoundException, SQLException {
        connection = null;
        connectionHistory = null;
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:main.db");
        connectionHistory = DriverManager.getConnection("jdbc:sqlite:history.db");
    }

    public static void createDB() throws SQLException {
        stmt = connection.createStatement();
        stmt.execute("DROP TABLE IF EXISTS auth;");
        stmt.execute("CREATE TABLE IF NOT EXISTS auth (" +
                "login      STRING          NOT NULL UNIQUE,\n" +
                "psw        STRING          NOT NULL,\n" +
                "nickname   STRING          NOT NULL UNIQUE);");
        System.out.println("Таблица 'auth' готова");
    }

//    public static void createHistoryDB() throws SQLException {
//
//        stmt = connection.createStatement();
//        stmt.execute("CREATE TABLE IF NOT EXISTS history (\n "
//                + " id integer AUTO_INCREMENT,\n"
//                + " string text\n"
//                + ");");
//        System.out.println("history");
//    }

    private static void fillTableBatch() throws SQLException {
//        long begin = System.currentTimeMillis();
        psInsert = connection.prepareStatement("INSERT INTO auth (login, psw, nickname) VALUES (?, ?, ?);");
        connection.setAutoCommit(false);

        for (int i = 1; i <= 10; i++) {
            psInsert.setString(1, "log" + i);
            psInsert.setString(2, "psw" + i);
            psInsert.setString(3, "nick" + i);
            psInsert.executeUpdate();
        }
        psInsert.executeBatch();

        psInsert = connection.prepareStatement("INSERT INTO auth (login, psw, nickname) VALUES (?, ?, ?);");

        connection.setAutoCommit(true);


    }


    public static void CloseDB() throws ClassNotFoundException, SQLException {
        connection.close();
        //       connectionHistory.close();
        stmt.close();
    }
}



