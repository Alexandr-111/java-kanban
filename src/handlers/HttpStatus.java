package handlers;

public enum HttpStatus {

    OK(200),
    CREATED(201),
    NOT_FOUND(404),
    NOT_ACCEPTABLE(406),
    INTERNAL_SERVER_ERROR(500);
    private final int value;

    HttpStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}