package cn;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ClientGUI {
    private JFrame frame;
    private JTextField keywordsField;
    private JTextArea optionsField;
    private JTextArea resultArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ClientGUI().createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        frame = new JFrame("DICTIONARY");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        keywordsField = new JTextField(20);
        optionsField = new JTextArea(10, 20);
        optionsField.setEditable(false);
        resultArea = new JTextArea(10, 20);
        resultArea.setEditable(false);

        JButton sendButton = new JButton("Send Request");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendRequest();
            }
        });

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(new JLabel("Keywords:"), gbc);

        gbc.gridy++;
        panel.add(keywordsField, gbc);

        gbc.gridy++;
        panel.add(new JLabel("Options:"), gbc);

        gbc.gridy++;
        panel.add(new JScrollPane(optionsField), gbc);

        gbc.gridy++;
        panel.add(sendButton, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(resultArea), gbc);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void sendRequest() {
        try {
            Socket socket = new Socket("10.70.0.211", 1239);

            // Set up input and output streams
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Send keywords to the server
            out.println(keywordsField.getText());

            // Receive and display the options from the server
            StringBuilder optionsBuilder = new StringBuilder();
            String option;
            while ((option = in.readLine()) != null && !option.equals("END_OPTIONS")) {
                optionsBuilder.append(option).append("\n");
            }
            System.out.println("Options received from server:\n" + optionsBuilder.toString());
            optionsField.setText(optionsBuilder.toString());

            // Receive and display the dictionary from the server
            String dictionary = in.readLine();
            System.out.println("Meaning received from server:\n" + dictionary);
            resultArea.setText("Meaning received from server:\n" + dictionary);

            // Close connections
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

