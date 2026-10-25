public class DuplicateIdException extends RuntimeException {
    private final String duplicateId;

    public DuplicateIdException(String duplicateId) {
        super("Trung ma: " + duplicateId);
        this.duplicateId = duplicateId;
    }

    public String getDuplicateId() {
        return duplicateId;
    }
}
