package com.company.app;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class ServerUI extends JFrame{
    private JButton startServerButton;
    private JTextArea textArea1;
    private JPanel panel3;
    private boolean hasServerStarted = false;
    Dictionary dictionary = new Dictionary();


    public ServerUI(String title) {
        super(title);
        this.setContentPane(panel3);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Runnable r = new Runnable() {
            public void run() {
                try {

                    startServer();

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        };
        final Thread runningThread = new Thread(r);


        startServerButton.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e) {
                if(!hasServerStarted){
                    startServerButton.setEnabled(false);
                    try{
                        dictionary.readFile();
                        runningThread.start();
                        //dictionary.readFile();
                    }
                    catch (Exception e1){
                        textArea1.append(e1.toString() + "\n");
                    }
                    hasServerStarted = !hasServerStarted;
                    startServerButton.setText("Terminate Server");
                    startServerButton.setEnabled(true);

                }
                else {
                        runningThread.interrupt();
                        hasServerStarted = !hasServerStarted;
                        dispose();
                        System.exit(0);

                }


                }

        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch(Exception ignored){}
            JFrame frame = new ServerUI("ServerUI");
            frame.setVisible(true);
    }


    public void startServer()throws IOException {


        java.rmi.registry.LocateRegistry.createRegistry(1099);
        ClientHandle handle = new ClientHandle(textArea1, dictionary);
        Naming.rebind("rmi://127.0.0.1/myabc",handle);
        textArea1.append("Server Started: Waiting for Clients\n");



        }
    }






class ClientHandle extends UnicastRemoteObject implements DictionaryServerIF{
    JTextArea jTextArea;
    Dictionary dictionary;

    // Constructor
    public ClientHandle(JTextArea jTextArea,Dictionary dictionary) throws RemoteException {
        this.jTextArea = jTextArea;
        this.dictionary = dictionary;
    }





    private String getReplyFromDictionary(String received) {

                if(received.endsWith("Query_Word")){
                     return dictionary.getMeaning(received.split("Query_Word")[0]);
                }
                else if(received.endsWith("Add_Word")){
                    return dictionary.addWord(received.split("Add_Word")[0]);
                }
                else if(received.endsWith("Remove_Word")){
                    return dictionary.removeWord(received.split("Remove_Word")[0]);
                }
                else {
                    jTextArea.setText("massive error");
                    return "unexpected error";
                }
    }



    public synchronized void getRequest(DictionaryClientIF dictionaryClientIF, String message) throws RemoteException {
        dictionaryClientIF.display(getReplyFromDictionary(message));
    }


    public void display(String message) throws RemoteException {
           jTextArea.append(message);
    }


}










