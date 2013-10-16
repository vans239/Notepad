package notepad.controller.event;

import notepad.controller.ControllerEvent;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class ExceptionEvent implements ControllerEvent {
    private Throwable exception;
    private String cause;

    public ExceptionEvent(String cause, Throwable e) {
        this.cause = cause;
        this.exception = e;

    }

    public String getCause() {
        return cause;
    }

    public Throwable getException() {
        return exception;
    }
}
