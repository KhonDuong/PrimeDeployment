import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class Client {
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    public static void main(String[] args) {
        Client client = new Client();
        client.createUI();
    }

    private void createUI() {
        JFrame frame = new JFrame("Prime Checking Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(600, 420);
        frame.setLayout(null);
        frame.setVisible(true);

        // Create instructions for how to use the program
        JLabel instructionLabel = new JLabel();
        instructionLabel.setBounds(50, 25, 250, 25);
        instructionLabel.setText("Write numbers into the text field:");
        frame.add(instructionLabel);

        JTextArea textArea = new JTextArea();
        textArea.setBounds(0, 75, 600, 345);
        textArea.setEditable(false);


        // Whenever the text area is typed in,
        JTextField textField = new JTextField();
        textField.setBounds(300, 25, 200, 25);
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int number = Integer.parseInt(textField.getText());

                textArea.setText("   The number you wrote is " + number + "\n" +textArea.getText());

                String message = communicateWithServer(number);
                if (Objects.equals(message, "yes")) {
                    textArea.setText("   " + number + " is a prime number." + "\n" +textArea.getText()  );
                } else if (Objects.equals(message, "no")) {
                    textArea.setText("   " + number + " is not a prime number." + "\n" + textArea.getText()  );
                }
                frame.repaint();
                frame.revalidate();
            }
        });
        frame.add(textField);
        frame.add(textArea);
        frame.repaint();
        frame.revalidate();


        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);
            // Socket socket = new Socket("130.254.204.36", 8000);
            // Socket socket = new Socket("drake.Armstrong.edu", 8000);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex) {
            instructionLabel.setText(instructionLabel.getText() + "\n" + "Connection to server failed.");
        }
    }
    public String communicateWithServer(int number) {
        try {
            // Send number to server to check if prime
            toServer.writeInt(number);
            toServer.flush();

            // Get message back from server whether it is prime or not
            String message = fromServer.readUTF();
            System.out.println(message);
            return message;
        }
        catch (IOException ex) {
            System.out.println("Failed to communicate with the server.");
        }

        return "Could not communicate with server";
    }
}
