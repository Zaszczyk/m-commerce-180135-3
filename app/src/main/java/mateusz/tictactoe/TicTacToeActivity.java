package mateusz.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class TicTacToeActivity extends Activity {

    // this array holds the status of each button whether
    //it is an X (=1), an O(=0), or blank (=2)
    int board[][];

    // this is the map of buttons. Think of it as buttons[ROW][COLUMN]
    // (same for board)
    Button buttons[][];

    // variables used throughout the app (some declared here for scope,
    // others for convenience)
    int i, j, k = 0;

    // our dialogue where we will provide status updates
    TextView textView;

    // The AI we will play against
    AI ai;


    int status = 0;

    /*
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

        // get the objects defined in main.xml
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

        clearBoard();

        textView.setText("Kliknij, żeby zaczął komputer.");
        textView.setOnClickListener(new TextViewListener());
    }

    private void clearBoard() {
        for (i = 1; i <= 3; i++) {
            for (j = 1; j <= 3; j++) {
                board[i][j] = 2;

                if (!buttons[i][j].hasOnClickListeners())
                    buttons[i][j].setOnClickListener(new ButtonListener(i, j));

                if (!buttons[i][j].isEnabled()) {
                    buttons[i][j].setText(" ");
                    buttons[i][j].setEnabled(true);
                }
            }
        }
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

    // check the board to see if there is a winner
    private boolean checkBoard() {
        boolean gameOver = false;

        if (checkIfYouWon()) {
            textView.setText("Wygrałeś!. Kliknij, zaby zagrać ponownie");
            status = 0;
            gameOver = true;

        } else if (checkIfYouLost()) {
            textView.setText("Przegrałeś! Kliknij, zaby zagrać ponownie");
            status = 0;
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
                textView.setText("Remis. Kliknij, zaby zagrać ponownie");
                status = 0;
            }
        }

        return gameOver;
    }

    // Mark the selected square
    private void markSquare(int x, int y, String symbol) {
        buttons[x][y].setEnabled(false);
        buttons[x][y].setText(symbol);
        if (symbol.equals("X"))
            board[x][y] = 1;
        else
            board[x][y] = 0;
    }

    // The AI inner class
    private class AI {
        // the computer checks the board and takes its turn
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

    class ButtonListener implements View.OnClickListener {
        int x;
        int y;

        public ButtonListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // handle the click event
        public void onClick(View view) {
            if (status == 0)
                status = 1;

            // check to see if the button is enabled
            if (buttons[x][y].isEnabled()) {
                markSquare(x, y, "O");

                if (!checkBoard()) {
                    ai.takeTurn();
                }
            } else
                Toast.makeText(getApplicationContext(), "To pole jest już zaznaczone.", Toast.LENGTH_SHORT).show();
        }
    }

    class TextViewListener implements View.OnClickListener {
        public void onClick(View view) {
            if (status == 0) {
                ai.takeTurn();
            } else if (status == 1) {

            }
        }
    }
}