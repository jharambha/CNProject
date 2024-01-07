package cnProj;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ServerProj {
    private ServerSocket serverSocket = null;

    public ServerProj(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started");

            // Display options from the server
            HashMap<String, String> keywordsDictionary = new HashMap<>();
            keywordsDictionary.put("Java", "Object-oriented programming language");
            keywordsDictionary.put("Socket", "Communication endpoint");
            keywordsDictionary.put("GUI", "Graphical User Interface");

            while (true) {
                System.out.println("Waiting for a client ...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client accepted");

                // Create a new thread to handle the client
                ClientHandler clientHandler = new ClientHandler(clientSocket, keywordsDictionary);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private HashMap<String, String> keywordsDictionary;

        public ClientHandler(Socket clientSocket, HashMap<String, String> keywordsDictionary) {
            this.clientSocket = clientSocket;
            this.keywordsDictionary = keywordsDictionary;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                // Display options from the server
                for (String option : keywordsDictionary.keySet()) {
                    out.println(option);
                }
                out.println("END_OPTIONS");

                String keyword = in.readLine();

                // Getting meaning
                String meaning = Dictionary(keyword, keywordsDictionary);

                // Send the dictionary back to the client
                out.println(meaning);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private static String Dictionary(String keyword, HashMap<String, String> keywordsDictionary) {
            return keywordsDictionary.getOrDefault(keyword, "Keyword not found");
        }
    }

    public static void main(String[] args) {
        // Start the server on a specified port
        new ServerProj(1239);
    }
}
