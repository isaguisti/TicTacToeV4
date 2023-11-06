package clarkson.ee408.tictactoev4.model;

import java.util.Objects;

/**
 * Event class; models a game lifecycle, from initiating a request to being declined, completed,
 * or aborted after being accepted
 */
public class Event {

    /**
     * An enumeration type for different game status's
     */
    public enum EventStatus { PENDING, DECLINED, ACCEPTED, PLAYING, COMPLETED, ABORTED }

    private int eventId;
    private String sender;
    private String opponent;
    private EventStatus status;
    private String turn;
    private int move;

    // CONSTRUCTORS
    /**
     * Default constructor for an Event object; sets all variables to their default values
     */
    public Event() {
        this(0, null, null, null, null, 0);
    }

    /**
     * Parameterized constructor for an Event object
     * @param eventId A global unique integer to represent an Event. Autogenerated by a
     * central database; of type int
     * @param sender Represents the username of the user that sends the game invitation; of
     * type String
     * @param opponent Represents the username of the user that the game invitation was
     * sent to; of type String
     * @param status Represents the status of a game; of type EventStatus
     * @param turn The username of the player that made the last move; of type String
     * @param move Stores the last move of the game; of type int
     */
    public Event(int eventId, String sender, String opponent, EventStatus status,
                 String turn, int move) {

        this.eventId = eventId;
        this.sender = sender;
        this.opponent = opponent;
        this.status = status;
        this.turn = turn;
        this.move = move;
    }

    // GET AND SET METHODS

    /**
     * Get method to retrieve an Event's event id
     * @return Returns the Event's event id
     */
    public int getEventId() {
        return eventId;
    }

    /**
     * Get method to retrieve an Event's sender
     * @return Returns the Event's sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * Get method to retrieve an Event's opponent
     * @return Returns the Event's opponent
     */
    public String getOpponent() {
        return opponent;
    }

    /**
     * Get method to retrieve an Event's status
     * @return Returns the Event's status
     */
    public EventStatus getStatus() {
        return status;
    }

    /**
     * Get method to retrieve an Event's turn
     * @return Returns the Event's current turn
     */
    public String getTurn() {
        return turn;
    }

    /**
     * Get method to retrieve an Event's move
     * @return Returns the Event's current move
     */
    public int getMove() {
        return move;
    }


    /**
     * Sets an event id for an Event
     * @param eventId Global unique integer to represent an Event
     */
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    /**
     * Sets a sender for an Event
     * @param sender The username of the user that sends the game invitation
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Sets an opponent for an Event
     * @param opponent The username of the user that the game invitation was
     * sent to
     */
    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    /**
     * Sets a status for an Event
     * @param status The status of a game
     */
    public void setStatus(EventStatus status) {
        this.status = status;
    }

    /**
     * Sets a turn for an Event
     * @param turn The username of the player that made the last move
     */
    public void setTurn(String turn) {
        this.turn = turn;
    }

    /**
     * Sets a move for an Event
     * @param move An integer storing the last move of the game
     */
    public void setMove(int move) {
        this.move = move;
    }


    /**
     * Overrides the equals method; uses the event id as the unique attribute and
     * tests if 'o' is equal to the current object and if 'o' is null or not in the same class
     * @param o Has the type Object
     * @return Returns true if both Events are equal; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return eventId == event.eventId;
    }

    /**
     * Writes the hashcode of an object using event id as the unique attribute
     * @return Returns the objects hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
}