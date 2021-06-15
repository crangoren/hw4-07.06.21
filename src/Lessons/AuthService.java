package Lessons;

import java.util.Optional;

public interface AuthService {

    void start();


    void stop();


    Optional<String> getNickByLoginAndPass(String login, String pass);

    String isLoginExist(String login);

    String isNicknameExist(String nickname);

    boolean registerNewUser(String login, String password, String nickname);

    boolean changeNickname(String oldNickname, String newNickname);




}
