package andrej.cuscak.dms.advice;

public class ParentFolderNotFoundException extends RuntimeException{
    public ParentFolderNotFoundException(String message) {
        super(message);
    }
}
