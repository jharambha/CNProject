package cnProj;

import java.io.*;
import java.util.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetAddress;

public class ServerProj {
	
	public ServerProj(int port) {
		
		try{  
    		ServerSocket server = new ServerSocket(port); 
    		Socket socket = server.accept();
    		System.out.println("Accepted connection from " + socket.getInetAddress());

    		handleClient(socket);
    		
    		socket.close();  
    		}catch(Exception e){System.out.println(e);}  
    		
	}

    public static void main(String[] args) {
    	new ServerProj(6658);
    	
    }
    public static void handleClient(Socket socket)throws IOException {
    	
    	BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    	
    	String clientInput = input.readLine();
        String[] minMaxSymbols = clientInput.split(",");
        int min = Integer.parseInt(minMaxSymbols[0]);
        int max = Integer.parseInt(minMaxSymbols[1]);
        String s = minMaxSymbols[2];
        String[] symbols = s.split("");
        
        String[] dictionary = generateDictionary(symbols, min, max);
      
        writeDictionaryToFile(dictionary,"Output.txt");
        sendFileToClient(socket,"Output.txt");
        
        input.close();
        output.close();
    	
        
    }
    

    public static String[] generateDictionary(String[] symbols, int min, int max) {
        Set<String> dictionary = new HashSet<>();
        generateCombinations(symbols, min, max, 0, new StringBuilder(), dictionary);
        return dictionary.toArray(new String[0]);
    }

    private static void generateCombinations(String[] symbols, int minLen, int maxLen, int index, StringBuilder current, Set<String> result) {
        int currentLen = current.length();
        if (currentLen >= minLen && currentLen <= maxLen) {
            result.add(current.toString());
        }

        if (index == symbols.length) {
            return;
        }

        generateCombinations(symbols, minLen, maxLen, index + 1, current.append(symbols[index]), result);
        current.deleteCharAt(current.length() - 1);
        generateCombinations(symbols, minLen, maxLen, index + 1, current, result);
    }
    
    
    private static void writeDictionaryToFile(String[]  dictionary, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
        	String data = String.join("\n", dictionary);
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void sendFileToClient(Socket clientSocket, String filename) {
        try (BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(filename));
             OutputStream outputStream = clientSocket.getOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
}
}
