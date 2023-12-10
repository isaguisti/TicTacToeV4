package clarkson.ee408.tictactoev4.socket;

import clarkson.ee408.tictactoev4.socket.Response;
import clarkson.ee408.tictactoev4.model.User;
import clarkson.ee408.tictactoev4.model.Event;

import java.util.List;

/**
 * Server response to a UPDATE_PAIRING request
 */
public class PairingResponse extends Response {
    /**
     * Represents players that are available to receive game invitations
     */
    private List<User> availableUsers;

    /**
     * Represents an event for a game invitation from another user
     */
    private Event invitation;

    /**
     * Represents an event for a response to a game invitation earlier sent by the current user
     */
    private Event invitationResponse;

    /**
     * Default constructor
     */
    public PairingResponse() {
        super();
    }

    /**
     * @param status Status to indicate success or failure of the request
     * @param message Explanation of the success or failure of the request
     * @param availableUsers List of available users that can receive game invitation
     * @param invitation Game invitation sent to the current user
     * @param invitationResponse Response to invitation earlier sent by the current user
     */
    public PairingResponse(ResponseStatus status, String message, List<User> availableUsers, Event invitation, Event invitationResponse) {
        super(status, message);
        this.availableUsers = availableUsers;
        this.invitation = invitation;
        this.invitationResponse = invitationResponse;
    }

    /**
     * Setter function for {@link #availableUsers} attribute
     * @param availableUsers players that are available to receive game invitations
     */
    public void setAvailableUsers(List<User> availableUsers) {
        this.availableUsers = availableUsers;
    }

    /**
     * Setter function for {@link #invitation} attribute
     * @param invitation an event for a game invitation from another user
     */
    public void setInvitation(Event invitation) {
        this.invitation = invitation;
    }

    /**
     * Setter function for {@link #invitationResponse} attribute
     * @param invitationResponse an event for a response to a game invitation earlier sent by the current user
     */
    public void setInvitationResponse(Event invitationResponse) {
        this.invitationResponse = invitationResponse;
    }

    /**
     * Getter function for {@link #availableUsers} attribute
     * @return availableUsers
     */
    public List<User> getAvailableUsers() {
        return availableUsers;
    }

    /**
     * Getter function for {@link #invitation} attribute
     * @return invitation
     */
    public Event getInvitation() {
        return invitation;
    }

    /**
     * Getter function for {@link #invitationResponse} attribute
     * @return invitationResponse
     */
    public Event getInvitationResponse() {
        return invitationResponse;
    }
}
