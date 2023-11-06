package clarkson.ee408.tictactoev4.socket;

/**
 * Response Class: Server Response to a client request
 */
public class Response {

    /**
     * An enumeration type for the status of the response
     */
    public enum ResponseStatus { SUCCESS, FAILURE};

    //Attributes
    protected ResponseStatus status;
    protected String message;


//Constructors
    /**
     * A default constructor that sets the contents of the response to null
     */
    public Response() {
        status = null;
        message = null;
    }
    /**
     * A constructor that take parameters for a response
     * @param status is an enum that shows if the responses was a SUCCESS or FAILURE
     * @param message is a string explaining the response status
     */
    public Response(ResponseStatus status, String message) {
        this.status = status;
        this.message = message;
    }



//Get and Set
    /**
     * Get method to retrieve a response's status
     * @return returns the response's status
     */
    public ResponseStatus getStatus() {
        return status;
    }
    /**
     * Sets a type for a status
     * @param status the status of the request
     */
    public void setStatus(ResponseStatus status) {
        this.status = status;
    }
    /**
     * Get method to retrieve a response's message
     * @return returns the response's message
     */
    public String getMessage() {
        return message;
    }
    /**
     * Sets a type for a status
     * @param message the sentence clarifying the response status
     */
    public void setMessage(String message) {
        this.message = message;
    }

} //end