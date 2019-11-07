package com.github.geirolz.exercises.tris.ex;


import java.io.PrintStream;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private final PrintStream out = System.out;
    private final PrintStream error = System.err;
    private final Scanner in = new Scanner(System.in);

    private String player1;
    private String player2;
    private String currentPlayer;
    private String currentPlayerSymbol;
    private String[][] map = new String[3][3];
    private boolean active = true;

    public void start(){
        initGame();
        printHelp();
        chooseFirst();
        gameLoop();
        printExit();
    }

    private void gameLoop(){
        while(active){
            out.print("\n" + currentPlayer + "[" + currentPlayerSymbol + "]: ");
            executeCmd(in.nextLine());
        }
    }

    private void initGame(){
        out.println("Welcome on Tris game!");

        out.print("\nPlease insert player1 name: ");
        this.player1 = in.nextLine();

        out.print("\nPlease insert player2 name: ");
        this.player2 = in.nextLine();
    }



    //PRINTS
    private void printHelp(){
        out.println("Use: ");
        out.println("-put [1-9] to insert value");
        out.println("-help to print help page");
        out.println("-quit to quit game");
    }

    private void printExit(){
        out.println("Goodbye " + player1 + " and " + player2 + "!");
        out.println("It was nice play with you!");
    }

    private void printWinner() {
        out.println("\nYEAA!! " + currentPlayer + " you WIN!");
    }

    private void printGrid(){

        for(int rowIndex = 0; rowIndex < 3; rowIndex++) {
            for (int colIndex = 0; colIndex < 3; colIndex++) {
                String cellValue = this.map[rowIndex][colIndex];
                out.print(" ");
                out.print((cellValue == null ? " " : cellValue));
                out.print(" ");

                if(colIndex != 2)
                    out.print("|");
            }

            if(rowIndex != 2)
                out.println("\n---|---|---");
        }
    }

    private void printFinishedMoves() {
        out.println("\nNo more moves available!");
    }


    //FUNCTIONS
    private void chooseFirst() {
        int result = new Random().nextInt(2) + 1;
        setCurrentPlayer(result == 1 ? player1 : player2);
    }

    private void nextPlayer(){
        if(currentPlayer.equals(player1)) {
            setCurrentPlayer(player2);
        } else if(currentPlayer.equals(player2)) {
            setCurrentPlayer(player1);
        } else
            throw new RuntimeException("Illegal players name");
    }

    private void setCurrentPlayer(String playerName){

        currentPlayer = playerName;

        if(Objects.equals(playerName, player1))
            currentPlayerSymbol = "X";
        if(Objects.equals(playerName, player2))
            currentPlayerSymbol = "O";
    }

    private void executeCmd(String cmd){

        String[] cmds = cmd.split(" ");

        if(cmds.length == 0) {
            error.println("Illegal command: " + cmd);
            return;
        }

        String cmd1 = cmds[0];

        switch (cmd1){
            case "put":
                put(cmds[1]);
                break;
            case "help":
                printHelp();
                break;
            case "quit":
                active = false;
                break;
            default:
                error.println("Illegal command: " + cmd);
        }
    }

    private void put(String cmd) {
        try{
            insertSymbolInPosition(Integer.parseInt(cmd));
            printGrid();
            if(checkForWin()){
                printWinner();
                active = false;
            }

            if(checkForFinishedMoves()){
                printFinishedMoves();
                active = false;
            }

            nextPlayer();

        }catch (Throwable ex){
            error.println(ex.getMessage());
        }
    }

    private void insertSymbolInPosition(int position){

        if(position <= 0 || position > 9)
            throw new RuntimeException("Invalid position! Position must be a value between 1 and 9");

        int col = position - 1;
        int row = 0;
        while(col >= 3){
            col -= 3;
            row++;
        }

        if(map[row][col] != null)
            throw new RuntimeException("Position busy!");

        map[row][col] = currentPlayerSymbol;
    }


    //CHECKS
    private boolean checkForWin() {

        //Check horizontal
        if(checkValues(map[0][0], map[0][1], map[0][2])) return true;
        if(checkValues(map[1][0], map[1][1], map[1][2])) return true;
        if(checkValues(map[2][0], map[2][1], map[2][2])) return true;

        //Check vertical
        if(checkValues(map[0][0], map[1][0], map[2][0])) return true;
        if(checkValues(map[0][1], map[1][1], map[2][1])) return true;
        if(checkValues(map[0][2], map[1][2], map[2][2])) return true;

        //Check oblique
        if(checkValues(map[0][0], map[1][1], map[2][2])) return true;
        if(checkValues(map[0][2], map[1][2], map[2][0])) return true;

        return false;
    }

    private boolean checkValues(String v1, String v2, String v3){
        return Objects.equals(v1, v2)
                && Objects.equals(v2, v3)
                && Objects.equals(v3, currentPlayerSymbol);
    }

    private boolean checkForFinishedMoves() {
        for (String[] row : map) {
            for (String cell : row) {
                if(cell == null)
                    return false;
            }
        }

        return true;
    }
}
