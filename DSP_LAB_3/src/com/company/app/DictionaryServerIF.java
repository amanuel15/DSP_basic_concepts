package com.company.app;

import javax.swing.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DictionaryServerIF extends Remote {

    void getRequest(DictionaryClientIF dictionaryClientIF,String message) throws RemoteException;
    void display(String message) throws RemoteException;

}
