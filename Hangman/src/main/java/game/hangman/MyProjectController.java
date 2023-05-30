package game.hangman;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class MyProjectController {

    final int SIZE = 26; // 26 ABC letters
    private int mistakeCounter = 0; // Number of mistake
    private int correctCounter = 0; // Number of right guess
    private int countWords = 0; //counter for number of words i get from the text
    private Button tempKey; // get input of button click
    private char charTemp;//save the input letter from the pressed button
    private GraphicsContext gc;
    private Button[] btns;// for a-z  letter button
    private Label[] lbl; // for the output for the buttons
    private List<String> stgWords, temp;// list for thr word from the txt file
    @FXML
    private GridPane LetterGrid;// grid for letters

    @FXML
    private GridPane ButtonGrid;//grid for buttons

    @FXML
    private Canvas canv;

    @FXML
    private Label LetterLbl;// output label for pressed letters

    @FXML
    void reset(ActionEvent event)//create a new game with new word
    {
        countWords++;// take the next word from the list
        aNewGame();// reset all inputs
    }


    public void initialize() {
        gc = canv.getGraphicsContext2D();
        List<String> temp = new ArrayList<String>();// list to get the words from the text
        stgWords = listWords(temp);// set all the words from the txt in the list
        drawBuilding();// building the hang man graphics
        aNewGame();// reset all inputs
        btns = new Button[SIZE];// creating 26 buttons for 26 a-z letters
        for (int i = 0; i < btns.length; i++) {
            btns[i] = new Button((char) (97 + i) + "");// convert code ascii to char for a-z letters and then convert them to string
            btns[i].setPrefSize(ButtonGrid.getPrefWidth() / (SIZE / 2), ButtonGrid.getPrefHeight() / (SIZE / 2));//all buttons same size on the grid
            ButtonGrid.add(btns[i], i % (SIZE / 2), i / (SIZE / 2));//2 rows for the letters
            btns[i].setOnAction(new EventHandler<ActionEvent>() {//on button press with activate the handle
                @Override
                public void handle(ActionEvent event) {
                    handleButton(event);
                }
            });
        }
    }

    private static List<String> listWords(List<String> stgWord) { //getting the words from the txt file and putting them in a list
        try {
            Scanner words = new Scanner(new File("hangman.txt"));// read the file
            String str;// new string to save the file
            while (words.hasNext()) {//if there is still words in the file
                str = words.next();
                stgWord.add(str);// add all the words to the list
            }
            return stgWord;// getting the list of words
        } catch (IOException e) {
            System.out.println("Error");
        }

        return stgWord;
    }

    private void drawBuilding() {// building the hang man graphics
        gc.strokeLine((canv.getWidth()) / 2, canv.getHeight() - 1, (canv.getWidth() - 1) / 2, 0);
        gc.strokeLine(0, canv.getHeight() - 1, canv.getWidth() - 1, canv.getHeight() - 1);
        gc.strokeLine((canv.getWidth() - 1) / 4, 0, (canv.getWidth() - 1) / 2, 0);
        gc.strokeLine((canv.getWidth() - 1) / 4, 0, (canv.getWidth() - 1) / 4, (canv.getWidth()) / 10);
    }

    private void handleButton(ActionEvent event) {
        if (tempKey == null)//first key pressed
            tempKey = (Button) event.getSource();//save the key
        checkLetterInWord(tempKey);//get the preesed button and check if the letter from the button is in the word we got from the text
    }


    private void checkLetterInWord(Button buttonLetter) {//get the preesed button and check if the letter from the button is in the word we got from the text

        charTemp = buttonLetter.getText().charAt(0); // get the letter from the button
        if (stgWords.get(countWords).indexOf(charTemp) >= 0) { // if the letter of the button the player pressed is in the word
            for (int i = 0; i < stgWords.get(countWords).length(); i++) {
                if (stgWords.get(countWords).charAt(i) == charTemp) {// then check where the letter is in the labels
                    correctCounter++;//count correct letter pressed so far
                    buttonLetter.setVisible(false);// make the pressed button disappeared
                    lbl[i].setText(charTemp + "");// set the label to show the letter
                    if (LetterLbl.getText().indexOf(charTemp) == -1)// if the word has more than 2 of the same letter dont wright it twice in the output label letters
                        LetterLbl.setText(LetterLbl.getText() + "  " + charTemp);// enter the presses letter to the label output
                }
                if (correctCounter == stgWords.get(countWords).length()) {// guessed all the letters from the word
                    JOptionPane.showConfirmDialog(null, "YOU WON!!", "Good Game", JOptionPane.CLOSED_OPTION);//the played won activate the game again with different word
                    countWords++;// next word form the txt file
                    resetButton();
                    LetterLbl.setText(" ");
                    aNewGame();
                }
            }
        } else {// the letter is not in the word
            buttonLetter.setVisible(false);// make the pressed button disappeared
            LetterLbl.setText(LetterLbl.getText() + "  " + charTemp);//  enter the presses letter to the label output
            switch (mistakeCounter) {// each mistake draw another body part of the hangman
                case 0:
                    gc.strokeOval((canv.getWidth() - 1) / 6, (canv.getWidth()) / 10, 50, 50);//head
                    mistakeCounter++;
                    break;
                case 1:
                    gc.strokeLine((canv.getWidth() - 1) / 4, ((canv.getWidth()) / 10) + 50, (canv.getWidth() - 1) / 4, ((canv.getWidth()) / 10) + 200); //body
                    mistakeCounter++;
                    break;
                case 2:
                    gc.strokeLine((canv.getWidth() - 1) / 4, ((canv.getWidth()) / 10) + 200, (canv.getWidth() - 1) / 3, ((canv.getWidth()) / 10) + 250);//legs
                    mistakeCounter++;
                    break;
                case 3:
                    gc.strokeLine((canv.getWidth() - 1) / 4, ((canv.getWidth()) / 10) + 200, (canv.getWidth() - 1) / 6, ((canv.getWidth()) / 10) + 250);//legs
                    mistakeCounter++;
                    break;
                case 4:
                    gc.strokeLine((canv.getWidth() - 1) / 4, ((canv.getWidth()) / 10) + 100, ((canv.getWidth() - 1) / 3) + 25, ((canv.getWidth()) / 10) + 50);//hands
                    mistakeCounter++;
                    break;
                case 5:
                    gc.strokeLine((canv.getWidth() - 1) / 4, ((canv.getWidth()) / 10) + 100, ((canv.getWidth() - 1) / 6) - 25, ((canv.getWidth()) / 10) + 50);//hands
                    JOptionPane.showConfirmDialog(null, "YOU are dead", "GameOver", JOptionPane.CLOSED_OPTION);//the played lose activate the game again with same word
                    resetButton();
                    LetterLbl.setText(" ");
                    aNewGame();
                    break;
                default:
            }


        }
        tempKey = null;// reset button so we can get the next click
    }


    private void aNewGame() {// create new game
        gc.clearRect(0, 0, canv.getWidth(), canv.getHeight());// clean the board
        drawBuilding();//build the building for the hangman
        correctCounter = 0;//reset correct guesses
        mistakeCounter = 0;//reset mistake
        if (countWords != stgWords.size())// Out of words in the txt file
        {
            lbl = new Label[stgWords.get(countWords).length()];// the amount of labels will be tha amount of letters in the word
            if (countWords == 0) {//first word from the list
                createlabels();//create the letter labels
            } else {
                resetButton();//reset the buttons
                LetterLbl.setText(" ");//reset the label text
                LetterGrid.setGridLinesVisible(false);// clear the grid than add the new labels for second word
                LetterGrid.getColumnConstraints().clear();// clear the grid than add the new labels for second word
                LetterGrid.getRowConstraints().clear();// clear the grid than add the new labels for second word
                LetterGrid.getChildren().clear();// get the first grid made by the screen builder
                LetterGrid.setGridLinesVisible(true);// clear the grid than add the new labels for second word
                LetterGrid.setAlignment(Pos.CENTER);//setting the alignment
                createlabels();//create the letter labels

            }
        } else {
            JOptionPane.showConfirmDialog(null, "Out Of Words", "Game finish", JOptionPane.CLOSED_OPTION);// Out of words in the txt file
            System.exit(0);//game ended
        }
    }


        private void createlabels () {//create the letter labels
            for (int i = 0; i < stgWords.get(countWords).length(); i++) {
                lbl[i] = new Label(stgWords.get(countWords).charAt(i) + "");//creating new label and naming them with the first letter of the word from the txt file
                lbl[i].setFont(new Font("Arial", 30));//sizing the letter to be bigger
                lbl[i].setText(" * ");// enter * to the text so the player couldnt see the letter of the word
                lbl[i].setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));//SETTING the label back ground to be blue
                lbl[i].setPrefSize(LetterGrid.getPrefWidth() / stgWords.get(countWords).length(), LetterGrid.getPrefHeight() / stgWords.get(countWords).length());//all labels same size on the grid
                LetterGrid.add(lbl[i], i % stgWords.get(countWords).length(), i / stgWords.get(countWords).length());//Adding the labels to the GRID
            }
        }

        private void resetButton () {// reset the buttons to visible
            for (int i = 0; i < btns.length; i++)
                btns[i].setVisible(true);
        }


    }