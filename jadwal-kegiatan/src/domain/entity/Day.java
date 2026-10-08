package domain.entity;

import domain.exception.ValidationException;

/**
 * Hari dalam seminggu (Senin sampai Minggu).
 * Urutan deklarasi enum menentukan urutan pengurutan hari.
 */
public enum Day {
    SENIN("Senin"),
    SELASA("Selasa"),
    RABU("Rabu"),
    KAMIS("Kamis"),
    JUMAT("Jumat"),
    SABTU("Sabtu"),
    MINGGU("Minggu");

    private final String label;

    Day(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Mengubah teks (case-insensitive) menjadi {@link Day}.
     *
     * @throws ValidationException jika teks bukan nama hari yang valid
     */
    public static Day fromLabel(String text) throws ValidationException {
        if (text != null) {
            for (Day day : values()) {
                if (day.label.equalsIgnoreCase(text.trim())) {
                    return day;
                }
            }
        }
        throw new ValidationException("Hari tidak valid (Senin - Minggu)");
    }
}
