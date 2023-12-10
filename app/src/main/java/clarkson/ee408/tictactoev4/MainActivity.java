package clarkson.ee408.tictactoev4;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import clarkson.ee408.tictactoev4.client.AppExecutors;
import clarkson.ee408.tictactoev4.client.SocketClient;
import clarkson.ee408.tictactoev4.socket.GamingResponse;
import clarkson.ee408.tictactoev4.socket.Request;
import clarkson.ee408.tictactoev4.socket.Response;

public class MainActivity extends AppCompatActivity {
    private TicTacToe tttGame;
    private Button [][] buttons;
    private TextView status;
    private Gson gson;
    private Handler handler;
    private Runnable refresh;
    private boolean shouldRequestMove;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        int player = getIntent().getIntExtra("PLAYER", 1);
        tttGame = new TicTacToe(player);
        buildGuiByCode( );

        gson = new GsonBuilder().serializeNulls().create();
        updateTurnStatus();

        shouldRequestMove = true;

        handler = new Handler();
        refresh = () -> {
            if(shouldRequestMove) requestMove();
            handler.postDelayed(refresh, 500);
        };
        handler.post(refresh);
    }

    public void buildGuiByCode( ) {
        // Get width of the screen
        Point size = new Point( );
        getWindowManager( ).getDefaultDisplay( ).getSize( size );
        int w = size.x / TicTacToe.SIDE;

        // Create the layout manager as a GridLayout
        GridLayout gridLayout = new GridLayout( this );
        gridLayout.setColumnCount( TicTacToe.SIDE );
        gridLayout.setRowCount( TicTacToe.SIDE + 2 );

        // Create the buttons and add them to gridLayout
        buttons = new Button[TicTacToe.SIDE][TicTacToe.SIDE];
        ButtonHandler bh = new ButtonHandler( );

//        GridLayout.LayoutParams bParams = new GridLayout.LayoutParams();
//        bParams.width = w - 10;
//        bParams.height = w -10;
//        bParams.bottomMargin = 15;
//        bParams.rightMargin = 15;

        gridLayout.setUseDefaultMargins(true);

        for( int row = 0; row < TicTacToe.SIDE; row++ ) {
            for( int col = 0; col < TicTacToe.SIDE; col++ ) {
                buttons[row][col] = new Button( this );
                buttons[row][col].setTextSize( ( int ) ( w * .2 ) );
                buttons[row][col].setOnClickListener( bh );
                GridLayout.LayoutParams bParams = new GridLayout.LayoutParams();
//                bParams.width = w - 10;
//                bParams.height = w -40;

                bParams.topMargin = 0;
                bParams.bottomMargin = 10;
                bParams.leftMargin = 0;
                bParams.rightMargin = 10;
                bParams.width=w-10;
                bParams.height=w-10;
                buttons[row][col].setLayoutParams(bParams);
                gridLayout.addView( buttons[row][col]);
//                gridLayout.addView( buttons[row][col], bParams );
            }
        }

        // set up layout parameters of 4th row of gridLayout
        status = new TextView( this );
        GridLayout.Spec rowSpec = GridLayout.spec( TicTacToe.SIDE, 2 );
        GridLayout.Spec columnSpec = GridLayout.spec( 0, TicTacToe.SIDE );
        GridLayout.LayoutParams lpStatus
                = new GridLayout.LayoutParams( rowSpec, columnSpec );
        status.setLayoutParams( lpStatus );

        // set up status' characteristics
        status.setWidth( TicTacToe.SIDE * w );
        status.setHeight( w );
        status.setGravity( Gravity.CENTER );
        status.setBackgroundColor( Color.GREEN );
        status.setTextSize( ( int ) ( w * .15 ) );
        status.setText( tttGame.result( ) );

        gridLayout.addView( status );

        // Set gridLayout as the View of this Activity
        setContentView( gridLayout );
    }

    public void update( int row, int col ) {
        int play = tttGame.play( row, col );
        if( play == 1 )
            buttons[row][col].setText( "X" );
        else if( play == 2 )
            buttons[row][col].setText( "O" );
        if( tttGame.isGameOver( ) ) {
            status.setBackgroundColor( Color.RED );
            enableButtons( false );
            status.setText( tttGame.result( ) );
            shouldRequestMove = false;
            showNewGameDialog( );	// offer to play again
        }
        else updateTurnStatus();
    }

    public void sendMove (int move) {
        Request request = new Request();
        request.setType(Request.RequestType.SEND_MOVE);
        request.setData(gson.toJson(move));

        Log.e(TAG, "Sending Move: " + move);
        AppExecutors.getInstance().networkIO().execute(()-> {
            Response response = SocketClient.getInstance().sendRequest(request, Response.class);
            AppExecutors.getInstance().mainThread().execute(()-> {
                if(response == null) {
                    Toast.makeText(this, "Couldn't send game move", Toast.LENGTH_SHORT).show();
                } else if(response.getStatus() == Response.ResponseStatus.FAILURE) {
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                }else{ //Success
                    Log.e(TAG, "Move sent");
                }
            });
        });
    }

    private void requestMove() {
        Request request = new Request();
        request.setType(Request.RequestType.REQUEST_MOVE);

        AppExecutors.getInstance().networkIO().execute(()-> {
            GamingResponse response = SocketClient.getInstance().sendRequest(request, GamingResponse.class);

            AppExecutors.getInstance().mainThread().execute(()-> {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                } else if (response.getStatus() == Response.ResponseStatus.FAILURE) {
                    Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                } else if(!response.getActive()) {
                    showNewGameDialog(); // not sure how to do task 10
                } else if(response.getMove() != -1) {
                    // Convert cell id to row and columns
                    int row = response.getMove() / 3;
                    int col = response.getMove() % 3;
                    update(row, col);
                }
            });
        });
    }

    public void updateTurnStatus( ) {
        if(tttGame.getPlayer() == tttGame.getTurn()){
            status.setText("Your Turn");
            enableButtons(true);
        }
        else {
            status.setText("Waiting for Opponent");
            enableButtons(false);
        }
    }

    public void enableButtons( boolean enabled ) {
        for( int row = 0; row < TicTacToe.SIDE; row++ )
            for( int col = 0; col < TicTacToe.SIDE; col++ )
                buttons[row][col].setEnabled( enabled );
    }

    public void resetButtons( ) {
        for( int row = 0; row < TicTacToe.SIDE; row++ )
            for( int col = 0; col < TicTacToe.SIDE; col++ )
                buttons[row][col].setText( "" );
    }

    public void showNewGameDialog( ) {
        AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setTitle( "Do you want to play again?" );
        alert.setMessage( "Play again?" );
        PlayDialog playAgain = new PlayDialog( );
        alert.setPositiveButton( "YES", playAgain );
        alert.setNegativeButton( "NO", playAgain );
        alert.show( );
    }

    private class ButtonHandler implements View.OnClickListener {
        public void onClick( View v ) {
            Log.d("button clicked", "button clicked");

            int column = 0;
            int row;
            for (row = 0; row < TicTacToe.SIDE; row++)
                for (column = 0; column < TicTacToe.SIDE; column++)
                    if (v == buttons[row][column]) {
                        sendMove((row * TicTacToe.SIDE) + column);
                        update(row, column);
                    }
        }
    }

    private class PlayDialog implements DialogInterface.OnClickListener {
        public void onClick( DialogInterface dialog, int id ) {
            if(id == DialogInterface.BUTTON_POSITIVE) /* YES button */ {
                tttGame.resetGame( );
                enableButtons( true );
                resetButtons( );
                status.setBackgroundColor( Color.GREEN );
                status.setText( tttGame.result( ) );
                tttGame.setPlayer(tttGame.getPlayer() == 1 ? 2:1);
                updateTurnStatus();
                shouldRequestMove = true;
            }
            else if(id == DialogInterface.BUTTON_NEGATIVE) /* NO button */
                MainActivity.this.finish( );
        }
    }

    private void completeGame() {
        Request request = new Request();
        request.setType(Request.RequestType.COMPLETE_GAME);

        AppExecutors.getInstance().networkIO().execute(() -> {
            Response response = SocketClient.getInstance().sendRequest(request, Response.class);

            AppExecutors.getInstance().mainThread().execute(() -> {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                } else if (response.getStatus() == Response.ResponseStatus.SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Game completed successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to complete game: " + response.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void abortGame() {
        Request request = new Request();
        request.setType(Request.RequestType.ABORT_GAME);

        AppExecutors.getInstance().networkIO().execute(() -> {
            Response response = SocketClient.getInstance().sendRequest(request, Response.class);

            AppExecutors.getInstance().mainThread().execute(() -> {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                } else if (response.getStatus() == Response.ResponseStatus.SUCCESS) {
                    Toast.makeText(getApplicationContext(), "Game aborted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to abort game: " + response.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacks(refresh);

        if (tttGame != null && tttGame.isGameOver()) {
            completeGame();
        } else {
            abortGame();
        }
    }
}