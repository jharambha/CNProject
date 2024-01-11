package cn;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.*;
import javax.swing.*;

public class ClientGUI {
	 public static void main(String[] args) {
		 SwingUtilities.invokeLater(() -> {
             new ClientGUI().createAndShowGUI();
         });
	 }
	  public static JTextField symbolsField;
	  public static JTextField minLenField;
	  public static JTextField maxLenField;
	  public static JTextArea dictionaryArea;
	 
     public static void createAndShowGUI() {
         JFrame frame = new JFrame("DICTIONARY");
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         symbolsField = new JTextField(10);
         minLenField = new JTextField(10);
         maxLenField = new JTextField(10);
         dictionaryArea = new JTextArea(10, 20);
         dictionaryArea.setEditable(false);


           Font font = new Font("Arial", Font.PLAIN, 24);
           symbolsField.setFont(font);
           minLenField.setFont(font);
           maxLenField.setFont(font);
           dictionaryArea.setFont(font);
           Font fontButton = new Font("Arial", Font.PLAIN, 20);

         JButton sendButton = new JButton("Send Request for Dictionary");
         sendButton.setFont(fontButton);
         sendButton.setBackground(new Color(173, 216, 230));
         sendButton.setForeground(Color.DARK_GRAY);
         sendButton.setBorder(BorderFactory.createLineBorder(new Color(255, 105, 180), 5));

         sendButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
               sendRequest();
             }
         });

         JPanel panel = new JPanel(new GridBagLayout());
         panel.setBackground(new Color(255, 182, 193));

         symbolsField.setBackground(Color.WHITE); 
         minLenField.setBackground(Color.WHITE);
         maxLenField.setBackground(Color.WHITE);
         dictionaryArea.setBackground(Color.WHITE);
         
         symbolsField.setForeground(Color.RED);
         minLenField.setForeground(Color.RED);
         maxLenField.setForeground(Color.RED);
         dictionaryArea.setForeground(Color.RED);

         symbolsField.setBorder(BorderFactory.createLineBorder(Color.PINK, 2)); 
         minLenField.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
         maxLenField.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
         dictionaryArea.setBorder(BorderFactory.createLineBorder(Color.PINK, 3));
         
         
         GridBagConstraints gbc = new GridBagConstraints();
         gbc.gridx = 0;
         gbc.gridy = 0;

         gbc.insets = new Insets(5, 15, 15,15);

         gbc.gridy++;
         panel.add(new JLabel("Minimum Length:"), gbc);

         gbc.gridy++;
         panel.add(new JScrollPane(minLenField), gbc);

         gbc.gridy++;
         panel.add(new JLabel("Maximum Length:"), gbc);

         gbc.gridy++;
         panel.add(new JScrollPane(maxLenField), gbc);

         gbc.gridy++;
         panel.add(new JLabel("Symbols:"), gbc);

         gbc.gridy++;
         panel.add(symbolsField, gbc);

         gbc.gridy++;
         panel.add(sendButton, gbc);

         gbc.gridy++;
         gbc.fill = GridBagConstraints.BOTH;
         panel.add(new JScrollPane(dictionaryArea), gbc);

         frame.getContentPane().add(panel);
         frame.pack();
         frame.setLocationRelativeTo(null);
         frame.setVisible(true);
     }

     public static void sendRequest() {
    	    try (Socket socket = new Socket("localhost", 6658);
    	         BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	         BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    	         PrintWriter fileWriter = new PrintWriter("Output.txt")) {

    	        String min = minLenField.getText();
    	        String max = maxLenField.getText();
    	        String symbols = symbolsField.getText();

    	        writer.write(min + "," + max + "," + symbols);
    	        writer.newLine(); 
    	        writer.flush(); 

    	        String dictionary;
    	        StringBuilder result = new StringBuilder();
    	        while ((dictionary = reader.readLine()) != null) {
    	            fileWriter.println(dictionary);
    	            result.append(dictionary).append("\n");
    	        }

    	        SwingUtilities.invokeLater(() -> {
    	            dictionaryArea.setText("Dictionary received from server:\n" + result.toString());
    	        });

    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	}

	
	
}