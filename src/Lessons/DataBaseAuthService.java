package Lessons;

import java.sql.*;
import java.util.Optional;
import java.util.logging.Logger;

public class DataBaseAuthService implements AuthService{
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement prst;

    private static final Logger logger = Logger.getLogger(BaseAuthService.class.getName());


    @Override
    public String isLoginExist(String login) {
        return sqlSelect(String.format("SELECT login FROM auth WHERE login = '%s'", login));
    }

    @Override
    public String isNicknameExist(String nickname) {
        return sqlSelect(String.format("SELECT nickname FROM auth WHERE nickname = '%s'", nickname));
    }
    private String sqlSelect(String sqlSelectQuery) {
        try {
            ResultSet rs = stmt.executeQuery(sqlSelectQuery);
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean registerNewUser(String login, String password, String nickname) {
        try {
            stmt.executeUpdate(String.format("INSERT INTO auth (login, psw, nickname) VALUES ('%s', '%s', '%s');", login, password, nickname));
            logger.info("Зарегистрирован новый пользователь: " + nickname);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
            return false;
        }
    }

    public boolean changeNickname (String oldNickname, String newNickname) {
        try {
            stmt.executeUpdate(String.format("UPDATE auth SET nickname='%s' WHERE nickname='%s';", newNickname, oldNickname));
            logger.info("Пользователь " + oldNickname + " изменил никнейм на " + newNickname);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
            return false;
        }
    }

    private static void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:auth.db");
            stmt = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
        }
    }

    private static void disconnect() {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void start() {
        try {
            connect();

        } catch (SQLException e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void stop() {
        disconnect();

    }

    @Override
    public Optional<String> getNickByLoginAndPass(String login, String pass) {
        return Optional.empty();
    }
}
