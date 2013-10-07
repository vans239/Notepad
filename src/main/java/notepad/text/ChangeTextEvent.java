package notepad.text;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public interface ChangeTextEvent {
    void apply(final TextModel textModel);
    void revert(final TextModel textModel);
}
