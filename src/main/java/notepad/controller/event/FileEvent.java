package notepad.controller.event;

import notepad.controller.ControllerEvent;

/**
 * Evgeny Vanslov
 * vans239@gmail.com
 */
public class FileEvent implements ControllerEvent {
    private String path;
    private FileStatus fileStatus;

    public FileEvent(FileStatus fileStatus, String path) {
        this.fileStatus = fileStatus;
        this.path = path;
    }

    public String getFilePath() {
        return path;
    }

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public static enum FileStatus {
        SAVE, OPEN
    }
}
