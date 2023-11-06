package clarkson.ee408.tictactoev4.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.google.gson.Gson;

import clarkson.ee408.tictactoev4.socket.Request;

public class SocketClient {
    private static SocketClient instance;

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Gson gson;

    private SocketClient() {
        String HOSTNAME = "";
        int PORT = 5000;
    }

    public static synchronized SocketClient getInstance() {
        if (instance == null) {
            instance = new SocketClient();
        }
        return instance;
    }

    public void close() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> T sendRequest(Request request, Class<T> responseClass) {
        T response = null;
        try {
            String jsonRequest = gson.toJson(request);

            outputStream.writeUTF(jsonRequest);
            outputStream.flush();

            String jsonResponse = inputStream.readUTF();

            response = gson.fromJson(jsonResponse, responseClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
