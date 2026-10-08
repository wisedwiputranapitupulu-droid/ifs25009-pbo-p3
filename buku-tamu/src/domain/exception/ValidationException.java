package domain.exception;

/**
 * Exception yang dilempar saat validasi aturan bisnis atau input domain gagal.
 * Pesan berisi alasan kegagalan tanpa tanda baca akhir (presenter yang memformatnya).
 */
public class ValidationException extends DomainException {
    public ValidationException(String message) {
        super(message);
    }
}
