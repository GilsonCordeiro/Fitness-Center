package Acadmy.start.project.exceptions;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message, Long id) {
        super(String.format(message, id));
    }

    public BusinessException(String message, Object id) {
        super(String.format(message, id));
    }

    public BusinessException(String message, String entityName, Object id) {
        super(String.format(message, entityName, id));
    }
}