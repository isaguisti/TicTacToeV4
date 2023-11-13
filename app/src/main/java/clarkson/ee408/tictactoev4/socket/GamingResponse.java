package clarkson.ee408.tictactoev4.socket;

/**
 * Server response to a REQUEST_MOVE request. It is a subclass of the Response class
 */
public class GamingResponse extends Response {

    //Attributes
    private int move;
    private boolean active;

//Methods
    /**
     * A default constructor that calls the default constructor located in the Response Class
     * which sets all values to null
     */
    public GamingResponse (){
        super();
    }
    /**
     * A parameterized constructor that calls the parameterized constructor located in the Response Class
     * which sets all values to the given parameters
     * @param status is an enum that shows if the responses was a SUCCESS or FAILURE
     * @param message is a string explaining the response status
     */
    public GamingResponse(ResponseStatus status, String message) {
        super(status, message);
        this.move = move;
        this.active = active;
    }

// Get and Set
    /**
     * Get method to retrieve a GamingResponse's move
     * @return returns the GamingResponse's move
     */
    public int getMove() {
        return move;
    }
    /**
     * Sets a type for a players move
     * @param move the players move of choice
     */
    public void setMove(int move) {
        this.move = move;
    }

    /**
     * Get method to retrieve a GamingResponse's active player
     * @return returns the GamingResponse's active player
     */
    public boolean getActive(){
        return active;
    }
    /**
     * Sets a type for which player is currently active
     * @param active shows which player is active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

}//end

