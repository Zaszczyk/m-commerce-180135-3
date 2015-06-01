package mateusz.tictactoe;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class TicTacToeActivity extends Activity {

    /*
        1 - X
        0 - O
        2 - blank
    */
    int board[][];

    Button buttons[][];

    int i, j;

    TextView textView;

    AI ai;


    int status = 0;

    /*
        -1 - w trakcie
        0 - nierozpoczęta
        1 - skończona
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add("New Game");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        init();
        return true;
    }

    private void init() {
        ai = new AI();
        buttons = new Button[4][4];
        board = new int[4][4];

        textView = (TextView) findViewById(R.id.dialogue);

        buttons[1][3] = (Button) findViewById(R.id.one);
        buttons[1][2] = (Button) findViewById(R.id.two);
        buttons[1][1] = (Button) findViewById(R.id.three);
        buttons[2][3] = (Button) findViewById(R.id.four);
        buttons[2][2] = (Button) findViewById(R.id.five);
        buttons[2][1] = (Button) findViewById(R.id.six);
        buttons[3][3] = (Button) findViewById(R.id.seven);
        buttons[3][2] = (Button) findViewById(R.id.eight);
        buttons[3][1] = (Button) findViewById(R.id.nine);

        textView.setOnClickListener(new TextViewListener());

        clearBoard();
    }

    private void clearBoard() {
        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++) {
                board[i][j] = 2;
                buttons[i][j].getBackground().setColorFilter(0xFFFFFFFF, PorterDuff.Mode.MULTIPLY);
                if (!buttons[i][j].hasOnClickListeners())
                    buttons[i][j].setOnClickListener(new ButtonListener(i, j));

                if (!buttons[i][j].isEnabled()) {
                    buttons[i][j].setText(" ");
                    buttons[i][j].setEnabled(true);
                }
            }
        }
        textView.setText("Kliknij, żeby zaczął komputer.");
    }

    private boolean checkIfYouWon() {
        return (board[1][1] == 0 && board[2][2] == 0 && board[3][3] == 0)
                || (board[1][3] == 0 && board[2][2] == 0 && board[3][1] == 0)
                || (board[1][2] == 0 && board[2][2] == 0 && board[3][2] == 0)
                || (board[1][3] == 0 && board[2][3] == 0 && board[3][3] == 0)
                || (board[1][1] == 0 && board[1][2] == 0 && board[1][3] == 0)
                || (board[2][1] == 0 && board[2][2] == 0 && board[2][3] == 0)
                || (board[3][1] == 0 && board[3][2] == 0 && board[3][3] == 0)
                || (board[1][1] == 0 && board[2][1] == 0 && board[3][1] == 0);
    }

    private boolean checkIfYouLost() {
        return (board[1][1] == 1 && board[2][2] == 1 && board[3][3] == 1)
                || (board[1][3] == 1 && board[2][2] == 1 && board[3][1] == 1)
                || (board[1][2] == 1 && board[2][2] == 1 && board[3][2] == 1)
                || (board[1][3] == 1 && board[2][3] == 1 && board[3][3] == 1)
                || (board[1][1] == 1 && board[1][2] == 1 && board[1][3] == 1)
                || (board[2][1] == 1 && board[2][2] == 1 && board[2][3] == 1)
                || (board[3][1] == 1 && board[3][2] == 1 && board[3][3] == 1)
                || (board[1][1] == 1 && board[2][1] == 1 && board[3][1] == 1);
    }

    private boolean checkBoard() {
        boolean gameOver = false;

        if (checkIfYouWon()) {
            //Toast.makeText(getApplicationContext(), "Wygrałeś!", Toast.LENGTH_SHORT).show();
            textView.setText("Wygrałeś. Kliknij, żeby zagrać ponownie.");
            status = 1;
            gameOver = true;

        } else if (checkIfYouLost()) {
            //Toast.makeText(getApplicationContext(), "Przegrałeś!", Toast.LENGTH_SHORT).show();
            textView.setText("Przegrałeś. Kliknij, żeby zagrać ponownie.");
            status = 1;
            gameOver = true;
        } else {
            boolean isEmpty = true;
            for (i = 1; i <= 3; i++) {
                for (j = 1; j <= 3; j++) {
                    if (board[i][j] == 2) {
                        isEmpty = false;
                        break;
                    }
                }
            }

            if (isEmpty) {
                gameOver = true;
                //Toast.makeText(getApplicationContext(), "Remis.", Toast.LENGTH_SHORT).show();
                textView.setText("Remis. Kliknij, zaby zagrać ponownie");
                status = 1;
            }
        }

        return gameOver;
    }

    private void markSquare(int x, int y, String symbol) {
        buttons[x][y].setEnabled(false);
        buttons[x][y].setText(symbol);
        if (symbol.equals("X")){
            buttons[x][y].getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
            board[x][y] = 1;
        }
        else{
            board[x][y] = 0;
            buttons[x][y].getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        }


    }

    private void gameStarted() {
        status = -1;
        textView.setText("Gra w trakcie.");
    }

    class ButtonListener implements View.OnClickListener {
        int x;
        int y;

        public ButtonListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void onClick(View view) {
            if (status == 0) {
                gameStarted();
            }

            markSquare(x, y, "O");

            if (!checkBoard()) {
                ai.takeTurn();
            }
        }
    }

    class TextViewListener implements View.OnClickListener {
        public void onClick(View view) {
            if (status == 0) {
                gameStarted();
                ai.takeTurn();
            } else if (status == 1) {
                clearBoard();
                status = 0;
            }
        }
    }

    private class AI {

        public void takeTurn() {
            if (board[1][1] == 2 &&
                    ((board[1][2] == 0 && board[1][3] == 0) ||
                            (board[2][2] == 0 && board[3][3] == 0) ||
                            (board[2][1] == 0 && board[3][1] == 0))) {
                markSquare(1, 1, "X");
            } else if (board[1][2] == 2 &&
                    ((board[2][2] == 0 && board[3][2] == 0) ||
                            (board[1][1] == 0 && board[1][3] == 0))) {
                markSquare(1, 2, "X");
            } else if (board[1][3] == 2 &&
                    ((board[1][1] == 0 && board[1][2] == 0) ||
                            (board[3][1] == 0 && board[2][2] == 0) ||
                            (board[2][3] == 0 && board[3][3] == 0))) {
                markSquare(1, 3, "X");
            } else if (board[2][1] == 2 &&
                    ((board[2][2] == 0 && board[2][3] == 0) ||
                            (board[1][1] == 0 && board[3][1] == 0))) {
                markSquare(2, 1, "X");
            } else if (board[2][2] == 2 &&
                    ((board[1][1] == 0 && board[3][3] == 0) ||
                            (board[1][2] == 0 && board[3][2] == 0) ||
                            (board[3][1] == 0 && board[1][3] == 0) ||
                            (board[2][1] == 0 && board[2][3] == 0))) {
                markSquare(2, 2, "X");
            } else if (board[2][3] == 2 &&
                    ((board[2][1] == 0 && board[2][2] == 0) ||
                            (board[1][3] == 0 && board[3][3] == 0))) {
                markSquare(2, 3, "X");
            } else if (board[3][1] == 2 &&
                    ((board[1][1] == 0 && board[2][1] == 0) ||
                            (board[3][2] == 0 && board[3][3] == 0) ||
                            (board[2][2] == 0 && board[1][3] == 0))) {
                markSquare(3, 1, "X");
            } else if (board[3][2] == 2 &&
                    ((board[1][2] == 0 && board[2][2] == 0) ||
                            (board[3][1] == 0 && board[3][3] == 0))) {
                markSquare(3, 2, "X");
            } else if (board[3][3] == 2 &&
                    ((board[1][1] == 0 && board[2][2] == 0) ||
                            (board[1][3] == 0 && board[2][3] == 0) ||
                            (board[3][1] == 0 && board[3][2] == 0))) {
                markSquare(3, 3, "X");
            } else {
                Random rand = new Random();
                int a = rand.nextInt(4);
                int b = rand.nextInt(4);
                while (a == 0 || b == 0 || board[a][b] != 2) {
                    a = rand.nextInt(4);
                    b = rand.nextInt(4);
                }
                markSquare(a, b, "X");
            }

            checkBoard();
        }
    }
}