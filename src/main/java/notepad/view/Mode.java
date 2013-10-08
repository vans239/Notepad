package notepad.view;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public enum Mode {
    REPLACE {
        @Override
        public Mode swap() {
            return INSERT;
        }
    }, INSERT {
        @Override
        public Mode swap() {
            return REPLACE;
        }
    };

    public abstract Mode swap();
}
