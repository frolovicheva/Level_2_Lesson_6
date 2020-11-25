package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {

    public static final int SERVER_PORT = 8189;

    public static void main(String[] args) {

        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {  /*Закрыть по завершении*/
            System.out.println("Ожидаем подключение...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Соединение установлено!");

            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            serverSendMessage (in, out); /*Сервер отправляет сообщения обратно в чат меняя "Я" на "Ты" */
            consoleSendMessage(out); /*Отправка сообщений в чат из консоли*/

        }
        catch (IOException e) {
            System.out.println("Порт уже занят");
            e.printStackTrace();
        }
    }

    private static void serverSendMessage(DataInputStream in, DataOutputStream out) {
        Thread thread = new Thread( () -> {
            while (true) {
                try {
                    String message = in.readUTF ();
                    System.out.println ("Cообщение пользователя: " + message);
                    if (message.equals ("/exit")) {
                        break;
                    }
                    out.writeUTF ("Ответ от сервера: " + message.replaceAll ("Я", "Ты"));
                } catch (IOException e) {
                    e.printStackTrace ();
                    System.out.println ("Проблема соединения с сервером.");
                    break;
                }
            }
        });
        thread.start ();
    }

    private static void consoleSendMessage(DataOutputStream out) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine ();
            try {
                out.writeUTF("Сообщение из консоли: " + message);
            if (message.equals("/exit")) {
                break;
            }
            } catch (IOException e) {
                e.printStackTrace ();
                System.out.println ("Проблема соединения с сервером.");
                break;
            }
        }
    }
}
