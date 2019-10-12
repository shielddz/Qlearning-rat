package RL;

import java.util.ArrayList;

public class Environnement {
    final int boardRows;
    final int boardColumns;
    private final int cheese;
    private final int doubleCheese;
    private final int poison;
    final double bigCheeseReward = 10;
    private final double cheeseReward = 1;
    private final double doubleCheeseReward = 2;
    private final double voidReward = -0.1;
    final double poisonReward = -5;
    public int[][] board;
    private final int initMouseS;
    public int mouseS;//5
    private ArrayList<Integer> cheeseS = new ArrayList<>();//1
    private ArrayList<Integer> doubleCheeseS = new ArrayList<>();//2
    private int bigCheeseS;//3
    private ArrayList<Integer> poisonS = new ArrayList<>();//4

    //initializing the game
    public Environnement(int boardRows, int boardColumns, int prcCheese, int prcDoubleCheese, int prcPoison) {
        this.boardRows = boardRows;
        this.boardColumns = boardColumns;
        cheese = (boardRows*boardColumns*prcCheese/100);
        doubleCheese = (boardRows*boardColumns*prcDoubleCheese/100);
        poison = (boardRows*boardColumns*prcPoison/100);
        //setting up the environment :
        /* Empty spot : 0
         * one cheese : 1
         * double cheese : 2
         * big pile of cheese : 3
         * poison : 4
         * mouse : 5 */
        //filling our board with zeros
        board = new int[this.boardRows][this.boardColumns];
        for (int i = 0; i < this.boardRows; i++) {
            for (int j = 0; j < this.boardColumns; j++) {
                board[i][j] = 0;
            }
        }

        //determinating the position of the mouse
        initMouseS = randState();
        mouseS = initMouseS;
        append(mouseS, 5);
        boolean cond = false;

        //set up the small cheese in the matrix
        for (int i = 0; i < cheese; i++) {
            do {
                int state = randState();
                if (    board[state/this.boardColumns][state% this.boardColumns] != 5 &&
                        board[state/this.boardColumns][state% this.boardColumns] != 1) {
                    append(state, 1);
                    cheeseS.add(state);
                    cond = true;
                }
            } while (!cond);
            cond = false;
        }

        //set up the double cheese in the matrix
        for (int i = 0; i < doubleCheese; i++) {
            do {
                int state = randState();
                if (    board[state/this.boardColumns][state% this.boardColumns] != 5 &&
                        board[state/this.boardColumns][state% this.boardColumns] != 1 &&
                        board[state/this.boardColumns][state% this.boardColumns] != 2) {
                    append(state, 2);
                    doubleCheeseS.add(state);
                    cond = true;
                }
            } while (!cond);
            cond = false;
        }

        //set up big pile of cheese in the matrix
        do {
            int state = randState();
            if (    board[state/this.boardColumns][state% this.boardColumns] != 5 &&
                    board[state/this.boardColumns][state% this.boardColumns] != 1 &&
                    board[state/this.boardColumns][state% this.boardColumns] != 2 ){
                append(state, 3);
                bigCheeseS = state;
                cond = true;
            }
        } while (!cond);
        cond = false;

        //set up the poison in the matrix
        for (int i = 0; i < poison; i++) {
            do {
                int state = randState();
                if (    board[state/this.boardColumns][state% this.boardColumns] != 5 &&
                        board[state/this.boardColumns][state% this.boardColumns] != 1 &&
                        board[state/this.boardColumns][state% this.boardColumns] != 2 &&
                        board[state/this.boardColumns][state% this.boardColumns] != 3 &&
                        board[state/this.boardColumns][state% this.boardColumns] != 4){
                    append(state, 4);
                    poisonS.add(state);
                    cond = true;
                }
            } while (!cond);
            cond = false;
        }
    }

    //initializing the game with specific positions (for tests)
    public Environnement(int mouseS, ArrayList<Integer> cheeseS, ArrayList<Integer> doubleCheeseS, int bigCheeseS, ArrayList<Integer> poisonS, int boardRows, int boardColumns){
        this.boardRows = boardRows;
        this.boardColumns = boardColumns;
        cheese = cheeseS.size();
        doubleCheese = doubleCheeseS.size();
        poison = poisonS.size();
        //filling our board with zeros
        board = new int[this.boardRows][this.boardColumns];
        for (int i = 0; i < this.boardRows; i++) {
            for (int j = 0; j < this.boardColumns; j++) {
                board[i][j] = 0;
            }
        }

        //determinating the position of the mouse
        initMouseS = mouseS;
        mouseS = initMouseS;
        append(mouseS, 5);

        //set up the small cheese in the matrix
        this.cheeseS.addAll(cheeseS);
        for (int i = 0; i < this.cheese; i++) {
            append(this.cheeseS.get(i), 1);
        }

        //set up the double cheese in the matrix
        this.doubleCheeseS.addAll(doubleCheeseS);
        for (int i = 0; i < this.doubleCheese; i++) {
            append(this.doubleCheeseS.get(i), 2);
        }

        //set up big pile of cheese in the matrix
        this.bigCheeseS = bigCheeseS;
        append(this.bigCheeseS, 3);

        //set up the poison in the matrix
        this.poisonS.addAll(poisonS);
        for (int i = 0; i < this.poison; i++) {
            append(this.poisonS.get(i), 4);
        }
    }

    //calculates the possibilities
    public ArrayList<Integer> possible(int state){
        ArrayList<Integer> possible = new ArrayList<>();
        //left and right
        if(state%boardColumns == 0){
            possible.add(2);
        }else if(state%boardColumns==(boardColumns-1)){
            possible.add(0);
        }else{
            possible.add(0);
            possible.add(2);
        }
        //up and down
        if(state/boardColumns == 0){
            possible.add(3);
        }else if (state/boardColumns == (boardRows-1)){
            possible.add(1);
        }else{
            possible.add(1);
            possible.add(3);
        }

        return possible;
    }

    //determinate reward
    double reward(int action){
        /*the actions are 0 for left 1 for up
        * 2 for right and 3 for down*/

        switch(action){
            case 0://left
                switch (board[mouseS/boardColumns][(mouseS%boardColumns)-1]){
                    case 0:return voidReward;
                    case 1:return cheeseReward;
                    case 2:return doubleCheeseReward;
                    case 3:return bigCheeseReward;
                    case 4:return poisonReward;
                }
                break;
            case 1://up
                switch (board[(mouseS/boardColumns)-1][mouseS%boardColumns]){
                    case 0:return voidReward;
                    case 1:return cheeseReward;
                    case 2:return doubleCheeseReward;
                    case 3:return bigCheeseReward;
                    case 4:return poisonReward;
                }
                break;
            case 2://right
                switch (board[mouseS/boardColumns][(mouseS%boardColumns+1)]){
                    case 0:return voidReward;
                    case 1:return cheeseReward;
                    case 2:return doubleCheeseReward;
                    case 3:return bigCheeseReward;
                    case 4:return poisonReward;
                }
                break;
            case 3://down
                switch (board[(mouseS/boardColumns)+1][mouseS%boardColumns]){
                    case 0:return voidReward;
                    case 1:return cheeseReward;
                    case 2:return doubleCheeseReward;
                    case 3:return bigCheeseReward;
                    case 4:return poisonReward;
                }
                break;
        }
        return 0;
    }

    //update according to the action
    public void step(int action){
        append(mouseS, 0);
        switch (action){
            case 0://left
                mouseS--;
                append(mouseS, 5);
                break;
            case 1://up
                mouseS-=boardColumns;
                append(mouseS, 5);
                break;
            case 2://right
                mouseS++;
                append(mouseS, 5);
                break;
            case 3://down
                mouseS+=boardColumns;
                append(mouseS, 5);
                break;
        }
    }

    //new state (returns the new state after taking an action)
    public int state(int action){
        switch (action){
            case 0://left
                return mouseS-1;
            case 1://up
                return mouseS-boardColumns;
            case 2://right
                return mouseS+1;
            default://down
                return mouseS+boardColumns;
        }
    }
    //initializes the state with the starting state.
    public void initState(){
        mouseS = initMouseS;
        for (int i = 0; i < boardRows; i++) {
            for (int j = 0; j < boardColumns; j++) {
                board[i][j] = 0;
            }
        }
        append(mouseS, 5);

        for (int i = 0; i < this.cheese; i++) {
            append(this.cheeseS.get(i), 1);
        }
        for (int i = 0; i < this.doubleCheese; i++) {
            append(this.doubleCheeseS.get(i), 2);
        }
        append(this.bigCheeseS, 3);
        for (int i = 0; i < this.poison; i++) {
            append(this.poisonS.get(i), 4);
        }
    }
    //get a random state (used in constructor)
    private int randState(){
        return (int) (Math.random() * (boardRows*boardColumns));
    }
    //append value according to state (used to change the value of a state (change the environment))
    public void append(int state, int value){
        board[state/boardColumns][state%boardColumns] = value;
    }

    public char letter(int state){
        switch (state){
            case 1 : return '1';
            case 2 : return '2';
            case 3 : return 'B';
            case 4 : return 'X';
            case 5 : return 'M';
        }
        return ' ';
    }
    public void draw(){
        //write - (base line of the top)
        for (int i = 0; i < (boardColumns*2+1); i++) {
            System.out.print("-");
        }
        System.out.println("");
        for (int i = 0; i < boardRows; i++) {
            for (int j = 0; j < boardColumns; j++) {
                System.out.print("|"+letter(board[i][j]));
            }
            System.out.println("|");
            // write - (base line of the rows)
            for (int j = 0; j < (boardColumns*2+1); j++) {
                System.out.print("-");
            }
            System.out.println("");
        }
    }
}
