package clarkson.ee408.tictactoev4.client;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import clarkson.ee408.tictactoev4.socket.Request;

public class SocketClient {
    private static SocketClient instance;

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Gson gson;

    private SocketClient() {
        String HOSTNAME = "128.153.170.137";
        int PORT = 5000;

        gson = new GsonBuilder().serializeNulls().create();

        try {
            socket = new Socket(InetAddress.getByName(HOSTNAME), PORT);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) {
            Log.e(TAG, "Could not Resolve Host", e);
        } catch (IOException e) {
            Log.e(TAG, "Client IOStreams Failed", e);
        } catch (Exception e) {
            Log.e(TAG, "Unknown Exception Occurred", e);
        }
    }

    public synchronized static SocketClient getInstance() {
        if (instance == null) {
            Log.e(TAG, "Creating socket instance singleton");
            instance = new SocketClient();
            Log.e(TAG, "Socket instance created");
        }
        return instance;
    }

    public void close() {
        try {
            if(socket != null) socket.close();
            if(inputStream != null) inputStream.close();
            if(outputStream != null) outputStream.close();
            instance = null;
        } catch (IOException e) {
            Log.e(TAG, "Client IOStreams Failed", e);
        }
    }

    public <T> T sendRequest(Request request, Class<T> responseClass) {
        try {
            String serializedRequest = gson.toJson(request);
            outputStream.writeUTF(serializedRequest);
            outputStream.flush();

            String serializedResponse = inputStream.readUTF();
            return gson.fromJson(serializedResponse, responseClass);
        } catch (IOException e) {
            close();
            Log.e(TAG, "Client IOStreams Failed", e);
        } catch (Exception e) {
            Log.e(TAG, "Unknown Exception Occurred", e);
        }
        return null;
    }
}
