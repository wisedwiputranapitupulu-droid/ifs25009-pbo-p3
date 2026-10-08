package domain.entity;

import java.util.Comparator;

/**
 * Kriteria pengurutan kegiatan.
 * Setiap opsi membawa comparator-nya sendiri sehingga logika pengurutan terpusat di domain.
 */
public enum SortOption {
    /** Urut hari (Senin-Minggu), lalu waktu. */
    DAY(Comparator.comparing(Activity::getDay).thenComparing(Activity::getTime)),

    /** Urut waktu paling pagi lebih dahulu, lalu hari. */
    TIME(Comparator.comparing(Activity::getTime).thenComparing(Activity::getDay)),

    /** Judul A ke Z (case-insensitive). */
    TITLE_ASC(Comparator.comparing(Activity::getTitle, String.CASE_INSENSITIVE_ORDER)),

    /** Judul Z ke A (case-insensitive). */
    TITLE_DESC(Comparator.comparing(Activity::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());

    private final Comparator<Activity> comparator;

    SortOption(Comparator<Activity> comparator) {
        this.comparator = comparator;
    }

    /** Mengembalikan comparator yang sesuai dengan opsi ini. */
    public Comparator<Activity> comparator() {
        return comparator;
    }
}
