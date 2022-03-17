package rmi.services;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BoardState extends Remote {

    List<Integer> getPositions() throws RemoteException;

    void update(final List<Integer> positions, BoardWrapper source) throws RemoteException;

    void registerBoard(BoardWrapper board) throws RemoteException;

}
