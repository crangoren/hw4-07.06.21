package Lessons;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class BaseAuthService  {




    private class Entry {
        private final String nick;
        private final String login;
        private final String pass;

        public Entry(String nick, String login, String pass) {
            this.nick = nick;
            this.login = login;
            this.pass = pass;
        }
    }

    private final List<Entry> entries;

    public BaseAuthService() {
        entries = List.of(
                new Entry("nick1", "login1", "pass1"),
                new Entry("nick2", "login2", "pass2"),
                new Entry("nick3", "login3", "pass3")
        );
    }




    public Optional<String> getNickByLoginAndPass(String login, String password) {
        return entries.stream()
                .filter(entry -> entry.login.equals(login) && entry.pass.equals(password))
                .map(entry -> entry.nick)
                .findFirst();
       /* for (Entry entry : entries) {
            if (entry.login.equals(login) && entry.pass.equals(pass)) {
                return entry.nick;
            }
        }*/
        //return null;
    }




}