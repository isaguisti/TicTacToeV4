package clarkson.ee408.tictactoev4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import clarkson.ee408.tictactoev4.client.AppExecutors;
import clarkson.ee408.tictactoev4.client.SocketClient;
import clarkson.ee408.tictactoev4.model.Event;
import clarkson.ee408.tictactoev4.model.User;
import clarkson.ee408.tictactoev4.socket.PairingResponse;
import clarkson.ee408.tictactoev4.socket.Request;
import clarkson.ee408.tictactoev4.socket.Response;

public class PairingActivity extends AppCompatActivity {

    private final String TAG = "PAIRING";

    private Gson gson;

    private TextView noAvailableUsersText;
    private RecyclerView recyclerView;
    private AvailableUsersAdapter adapter;

    private Handler handler;
    private Runnable refresh;

    private boolean shouldUpdatePairing = true;

    private SocketClient socketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pairing);

        Log.e(TAG, "App is now created");
        // TODO: DONE; setup Gson with null serialization option
        gson = new GsonBuilder().serializeNulls().create();

        //Setting the username text
        TextView usernameText = findViewById(R.id.text_username);
        // TODO: DONE; set the usernameText to the username passed from LoginActivity (i.e from Intent)
        String username = getIntent().getStringExtra("USERNAME");

        //Getting UI Elements
        noAvailableUsersText = findViewById(R.id.text_no_available_users);
        recyclerView = findViewById(R.id.recycler_view_available_users);

        //Setting up recycler view adapter
        adapter = new AvailableUsersAdapter(this, this::sendGameInvitation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        updateAvailableUsers(null);

        handler = new Handler();
        refresh = () -> {
            // TODO: call getPairingUpdate if shouldUpdatePairing is true
            handler.postDelayed(refresh, 1000);
        };
        handler.post(refresh);
    }

    /**
     * Send UPDATE_PAIRING request to the server
     */
    private void getPairingUpdate() {
        // TODO:  Send an UPDATE_PAIRING request to the server. If SUCCESS call handlePairingUpdate(). Else, Toast the error
        String apiUrl = "https://your-api-url/update_pairing"; // Replace with your actual API URL
        new Thread(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    PairingResponse response = new PairingResponse(Response.ResponseStatus.SUCCESS, "",
                            null, null, null);
                    runOnUiThread(() -> handlePairingUpdate(response));
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Pairing update failed", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    /**
     * Handle the PairingResponse received form the server
     * @param response PairingResponse from the server
     */
    private void handlePairingUpdate(PairingResponse response) {
        // TODO: DONE; handle availableUsers by calling updateAvailableUsers()
        List<User> availableUsers = response.getAvailableUsers();
        updateAvailableUsers(availableUsers);

        // TODO: DONE; handle invitationResponse. First by sending acknowledgement calling sendAcknowledgement()
        Event invitationResponse = response.getInvitationResponse();
        if (invitationResponse != null) {
            sendAcknowledgement(invitationResponse);
        // TODO: DONE; If the invitationResponse is ACCEPTED, Toast an accept message and call beginGame
            if (invitationResponse.getStatus() == Event.EventStatus.ACCEPTED) {
                Toast.makeText(this, "Invitation Accepted", Toast.LENGTH_SHORT).show();
                beginGame(invitationResponse, invitationResponse.getMove());
            }
        // TODO: DONE; If the invitationResponse is DECLINED, Toast a decline message
            else if (invitationResponse.getStatus() == Event.EventStatus.DECLINED) {
                Toast.makeText(this, "Invitation Declined", Toast.LENGTH_SHORT).show();
            }
        }
        // TODO: DONE; handle invitation by calling createRespondAlertDialog()
        Event invitation = response.getInvitation();
        if (invitation != null) {
            createRespondAlertDialog(invitation);
        }
    }

    /**
     * Updates the list of available users
     * @param availableUsers list of users that are available for pairing
     */
    public void updateAvailableUsers(List<User> availableUsers) {
        adapter.setUsers(availableUsers);
        if (adapter.getItemCount() <= 0) {
            // TODO: DONE; show noAvailableUsersText and hide recyclerView
            noAvailableUsersText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            // TODO: DONE; hide noAvailableUsersText and show recyclerView
            noAvailableUsersText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sends game invitation to an
     * @param userOpponent the User to send invitation to
     */
    private void sendGameInvitation(User userOpponent) {
        // TODO: DONE; Send an SEND_INVITATION request to the server. If SUCCESS Toast a success message. Else, Toast the error
        String apiUrl = "https://your-api-url/send_invitation"; // Replace with your actual API URL

        new Thread(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String jsonUser = gson.toJson(userOpponent);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonUser.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Invitation sent successfully", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Invitation sending failed", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    /**
     * Sends an ACKNOWLEDGE_RESPONSE request to the server
     * Tell server i have received accept or declined response from my opponent
     */
    private void sendAcknowledgement(Event invitationResponse) {
        // TODO: DONE; Send an ACKNOWLEDGE_RESPONSE request to the server.
        String apiUrl = "https://your-api-url/acknowledge_response"; // Replace with your actual API URL

        new Thread(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String jsonEvent = gson.toJson(invitationResponse);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonEvent.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Acknowledgement sent successfully", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Acknowledgement sending failed", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    /**
     * Create a dialog showing incoming invitation
     * @param invitation the Event of an invitation
     */
    private void createRespondAlertDialog(Event invitation) {
        // TODO: DONE; set shouldUpdatePairing to false
        shouldUpdatePairing = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Game Invitation");
        builder.setMessage(invitation.getSender() + " has Requested to Play with You");
        builder.setPositiveButton("Accept", (dialogInterface, i) -> acceptInvitation(invitation));
        builder.setNegativeButton("Decline", (dialogInterface, i) -> declineInvitation(invitation));
        builder.show();
    }

    /**
     * Sends an ACCEPT_INVITATION to the server
     * @param invitation the Event invitation to accept
     */
    private void acceptInvitation(Event invitation) {
        // TODO: DONE; Send an ACCEPT_INVITATION request to the server. If SUCCESS beginGame() as player 2. Else, Toast the error
        String apiUrl = "https://your-api-url/accept_invitation"; // Replace with your actual API URL

        new Thread(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String jsonEvent = gson.toJson(invitation);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonEvent.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() -> beginGame(invitation, 2));
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Invitation acceptance failed", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    /**
     * Sends an DECLINE_INVITATION to the server
     * @param invitation the Event invitation to decline
     */
    private void declineInvitation(Event invitation) {
        String apiUrl = "https://your-api-url/decline_invitation"; // Replace with your actual API URL

        new Thread(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                String jsonEvent = gson.toJson(invitation);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonEvent.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                // TODO: DONE; Send a DECLINE_INVITATION request to the server. If SUCCESS response, Toast a message, else, Toast the error
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Invitation declined", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Invitation decline failed", Toast.LENGTH_SHORT).show());
                }

                // TODO: DONE; set shouldUpdatePairing to true after DECLINE_INVITATION is sent.
                shouldUpdatePairing = true;

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    /**
     *
     * @param pairing the Event of pairing
     * @param player either 1 or 2
     */
    private void beginGame(Event pairing, int player) {
        // TODO: DONE; set shouldUpdatePairing to false
        shouldUpdatePairing = false;
        // TODO: DONE; start MainActivity and pass player as data
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("PLAYER", player);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: DONE; set shouldUpdatePairing to true
        shouldUpdatePairing = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);

        // TODO: DONE; set shouldUpdatePairing to false
        handler.removeCallbacksAndMessages(null);
        shouldUpdatePairing = false;
        // TODO: DONE; logout by calling close() function of SocketClient
        if (socketClient != null) {
            socketClient.close();
        }
    }

}