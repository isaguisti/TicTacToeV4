package clarkson.ee408.tictactoev4.socket;

import java.io.Serializable;

/**
 * Request class; clients request that is sent to the server
 */
public class Request implements Serializable {

    /**
     * An enumeration types for different request types
     */
    public enum RequestType { LOGIN, REGISTER, UPDATE_PAIRING, SEND_INVITATION, ACCEPT_INVITATION,
        DECLINE_INVITATION, ACKNOWLEDGE_RESPONSE, REQUEST_MOVE, SEND_MOVE, ABORT_GAME, COMPLETE_GAME }

    private RequestType type;
    private Object data;

    // CONSTRUCTORS

    /**
     * Default constructor for a Request object; sets all variables to their default values
     */
    public Request() {
        this(null, null);
    }

    /**
     * Parameterized constructor for a Request object
     * @param type The type of client request; of type RequestType
     * @param data A String representation of serialized data sent by the client. It can
     * be a serialized object of a String, Integer, or User
     */
    public Request(RequestType type, Object data) {
        this.type = type;
        this.data = data;
    }

    // GET AND SET METHODS

    /**
     * Get method to retrieve a Request's type
     * @return Returns the Request's type
     */
    public RequestType getType() {
        return type;
    }

    /**
     * Get method to retrieve a Request's data
     * @return Returns the Request's data
     */
    public Object getData() {
        return data;
    }


    /**
     * Sets a type for a Request
     * @param type The type of client request
     */
    public void setType(RequestType type) {
        this.type = type;
    }

    /**
     * Sets a data for a Request
     * @param data A representation of serialized data sent by the client
     */
    public void setData(Object data) {
        this.data = data;
    }


    /**
     * Converts a Request object into a string
     * @return Returns the string with all Request variables
     */
    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", data='" + data + '\'' +
                '}';
    }
}