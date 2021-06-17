package Lessons;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MyServer {

    private List<ClientHandler> clients;
    private AuthService authService;
    private static Connection connectionHistory;
    private static Statement stmt;
    private HistoryWriter historyShower = new HistoryWriter();




    public MyServer() {

        try (ServerSocket server = new ServerSocket(ChatConstants.PORT)) {
            authService = new DataBaseAuthService();
            authService.start();
            clients = new ArrayList<>();
            connectionTimeout();

            while (true) {
                System.out.println("Сервер ожидает подключения");
                Socket socket = server.accept();
                System.out.println("Клиент подключился");
                StringBuilder historyString = null;
                try {
                    historyString = historyShower.getHistoryChat();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(historyString != null){
                    String[] mass = historyString.toString().split("\n");
                    for (int i = 0; i < mass.length; i++) {
                        String[] partsMass = mass[i].split(" ", 2);
                        System.out.println((partsMass[0] + partsMass[1]));
                    }
                }

                //               Client.readHistory();
                new ClientHandler(this, socket);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

//    public synchronized void newClient(String nick, String login, String pass) {
//        clients.add()
//    }

    public synchronized boolean isNickBusy(String nick) {
        return clients.stream().anyMatch(client -> client.getName().equals(nick));

    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClients();
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClients();
    }


    public synchronized void broadcastMessage(String message) {
        clients.forEach(client -> client.sendMsg(message));

    }

    public synchronized void broadcastClients() {
        String clientsMessage = ChatConstants.CLIENTS_LIST +
                " " +
                clients.stream()
                        .map(ClientHandler::getName)
                        .collect(Collectors.joining(" "));
        clients.forEach(c -> c.sendMsg(clientsMessage));
    }


    public synchronized void privateMessage(String message, List<String> targeted) {
        for (ClientHandler client : clients) {
            if (!targeted.contains(client.getName())) {
                continue;
            }
            String name = targeted.get(1);
            client.sendMsg("Лично от " + "[" + name + "]:" + " " + message);
        }
    }

    private void connectionTimeout() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    LocalDateTime now = LocalDateTime.now();

                    Iterator<ClientHandler> i = clients.iterator();
                    while (i.hasNext()) {
                        ClientHandler client = i.next();
                        if (!client.isActive()
                                && Duration.between(client.getConnectTime(), now).getSeconds() > ChatConstants.MAX_DELAY_TIME) {
                            System.out.println("Тайм-аут ожидания авторизации. Соединение закрыто");
                            client.closeConnection();
                            i.remove();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).setDaemon(true);
    }
}





