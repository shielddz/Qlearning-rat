package RL.FXMLs;
import RL.World;

public class LinkedListBoard {
    int[][] board;
    private LinkedListBoard next;
    private LinkedListBoard previous;

    public LinkedListBoard(int[][] board){
        this.board = cloneBoard(board);
        next = null;
        previous = null;
    }
    public LinkedListBoard add(int[][] board){
        LinkedListBoard element = new LinkedListBoard(cloneBoard(board));
        next = element;
        element.previous = this;
        return element;
    }
    public LinkedListBoard next(){
        return this.next;
    }
    public LinkedListBoard previous(){
        return this.previous;
    }
    public boolean hasNext(){
        return (this.next != null);
    }
    public boolean hasPrevious(){
        return (this.previous != null);
    }
    private int[][] cloneBoard(int[][] board){
        int[][] clone = new int[World.gridHeight][World.gridWidth];
        for (int i = 0; i < World.gridHeight; i++) {
            for (int j = 0; j < World.gridWidth; j++) {
                clone[i][j] = board[i][j];
            }
        }
        return clone;
    }
}
