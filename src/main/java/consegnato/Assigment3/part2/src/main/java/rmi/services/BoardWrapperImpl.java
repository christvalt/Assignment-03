package rmi.services;

import rmi.puzzle.PuzzleBoard;
import rmi.Player;

import java.rmi.RemoteException;
import java.util.List;

public class BoardWrapperImpl implements BoardWrapper{

    private final PuzzleBoard board;
    private final Player myPlayer;

    public BoardWrapperImpl(final int n, final int m, final String imagePath, final Player myPlayer){
        this.board = new PuzzleBoard(n, m, imagePath, this);
        this.board.setVisible(true);
        this.myPlayer = myPlayer;
    }
    @Override
    public void updateBoard(List<Integer> positions) throws RemoteException {
        this.board.setCurrentPositions(positions);
    }

    public void updateState(List<Integer> positions) throws RemoteException {
        this.myPlayer.update(positions);
    }

    @Override
    public List<Integer> getPositions() throws RemoteException {
        return this.board.getCurrentPositions();
    }


}
