package com.company.app;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientUI extends JFrame {
    private JPanel panel1;
    private JTextField Search_Field_word;
    private JButton searchButton;
    private JTextField Add_Field_word;
    private JTextField Add_Field_meaning;
    private JButton addWordButton;
    private JButton removeWordButton;
    private JTextField Remove_Field_word;
    private JTextArea Server_Field_result;
    private JButton connectButton;
    DictionaryServerIF dictionaryServer;
    DictionaryClient dictionaryClient;
    boolean isConnected = false;



    public ClientUI(String title) {
        super(title);
        this.setContentPane(panel1);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        searchButton.setEnabled(false);
        addWordButton.setEnabled(false);
        removeWordButton.setEnabled(false);
        removeWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    try{

                        String retrievedBundle = Remove_Field_word.getText();
                        if(!retrievedBundle.isEmpty()){

                            dictionaryServer.getRequest(dictionaryClient,retrievedBundle+"Remove_Word");

                        }
                        else{
                            Server_Field_result.append("Invalid Input\n");
                        }
                        Remove_Field_word.setText("");
                    }        catch (Exception e1){
                        Server_Field_result.append(e1.toString());
                    }

            }
        });
        addWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    if(!Add_Field_meaning.getText().isEmpty() && !Add_Field_word.getText().isEmpty()){

                        dictionaryServer.getRequest(dictionaryClient,
                                Add_Field_word.getText()+"==__=="+Add_Field_meaning.getText()+"Add_Word"
                        );

                        Add_Field_word.setText("");
                        Add_Field_meaning.setText("");

                    }
                    else{
                        Server_Field_result.append("Invalid Input\n");
                    }


                }catch (Exception e1){
                    Server_Field_result.append(e1.toString());
                }

            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String retrievedBundle = Search_Field_word.getText();
                    if(!retrievedBundle.isEmpty()){
                        //String cnn = s.isConnected()?"is\n":"aint\n";
                       // Server_Field_result.append(cnn);

                        dictionaryServer.getRequest(dictionaryClient,Search_Field_word.getText()+"Query_Word");
                        Search_Field_word.setText("");
                    }
                    else {
                        Server_Field_result.append("Invalid Input\n");
                    }


                }catch (Exception e1)
                {
                    Server_Field_result.append(e1.toString());
                }
            }
        });
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isConnected){
                    StartClient();
                    isConnected = !isConnected;
                    connectButton.setText("Exit");
                }
                else{
                    dispose();
                    System.exit(0);

                }


            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {
        }
        JFrame frame = new ClientUI("ClientUI");
        frame.setVisible(true);
    }


    private void StartClient()  {

        try{
            dictionaryClient = new DictionaryClient(Server_Field_result);
            dictionaryServer = (DictionaryServerIF)Naming.lookup("rmi://127.0.0.1/myabc");
            searchButton.setEnabled(true);
            addWordButton.setEnabled(true);
            removeWordButton.setEnabled(true);
            dictionaryServer.display("Client has connected");
        }
        catch (Exception e){
            Server_Field_result.append(e.toString());
        }

    }


}

class DictionaryClient extends UnicastRemoteObject implements DictionaryClientIF{

    JTextArea jTextArea;
    public DictionaryClient(JTextArea jTextArea) throws RemoteException{
        this.jTextArea = jTextArea;
    }


    public void display(String message) throws RemoteException {
        jTextArea.append("\n");
        jTextArea.append(message);
    }
}


