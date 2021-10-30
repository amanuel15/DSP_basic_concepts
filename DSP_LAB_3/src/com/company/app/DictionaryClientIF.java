package com.company.app;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DictionaryClientIF extends Remote {

    public void display(String message) throws RemoteException;
}
