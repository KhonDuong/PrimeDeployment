import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class Server {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Prime Checking Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(600, 420);
        frame.setLayout(null);
        frame.setVisible(true);

        // Create instructions for how to use the program
        JTextArea textArea = new JTextArea();
        textArea.setBounds(0, 0, 600, 420);
        textArea.setEditable(false);
        java.util.Date date = new java.util.Date();
        textArea.setText("   Server started at " + date);
        frame.add(textArea);
        frame.repaint();
        frame.revalidate();

        try {
            // Listen for a connection request
            ServerSocket serverSocket = new ServerSocket(8000);
            Socket socket = serverSocket.accept();

            // Create data input and output streams
            DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
            DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

            while (true) {
                // Receive a number from the client
                int number = inputFromClient.readInt();
                textArea.setText( "   Number received from client is " + number + "\n" +  textArea.getText());
                frame.repaint();
                frame.revalidate();

                // Compute area
                boolean isPrime = isPrime(number);

                // Send yes for prime, no for not
                if (isPrime) {
                    outputToClient.writeUTF("yes");
                } else {
                    outputToClient.writeUTF("no");
                }
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    private static boolean isPrime(int number) {
        // dividing the number by two leaves you with a smaller amount of possible factors to work with
        // and anything past the halfway point is just going to repeat.
        int factors = number / 2;

        // 0 and 1 can't be prime, so return false
        if (number <= 1) {
            return false;
        } else {
            for (int i = 2; i <= factors; i++) {
                // if the number modulo i is equal to 0, it means that it is divisible by i and thus not prime
                if (number % i == 0) {
                    return false;
                }
            }
        }
        return true;
    }

}