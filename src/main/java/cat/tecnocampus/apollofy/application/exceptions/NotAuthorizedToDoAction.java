package cat.tecnocampus.apollofy.application.exceptions;

public class NotAuthorizedToDoAction extends RuntimeException {
    public NotAuthorizedToDoAction() {
        super("You are not authorized to do this action");
    }
}
