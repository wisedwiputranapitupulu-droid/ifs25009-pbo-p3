package domain.entity;

import java.util.Comparator;

/**
 * Kriteria pengurutan transaksi.
 * Setiap opsi membawa comparator-nya sendiri sehingga logika pengurutan
 * terpusat di domain, bukan tersebar sebagai angka ajaib di view/use case.
 */
public enum SortOption {
    /** Jumlah terbesar lebih dahulu. */
    AMOUNT_DESC(Comparator.comparingLong(Transaction::getAmount).reversed()),

    /** Jumlah terkecil lebih dahulu. */
    AMOUNT_ASC(Comparator.comparingLong(Transaction::getAmount)),

    /** Pemasukan ditampilkan lebih dahulu (urutan enum: INCOME sebelum EXPENSE). */
    INCOME_FIRST(Comparator.comparing(Transaction::getType)),

    /** Pengeluaran ditampilkan lebih dahulu. */
    EXPENSE_FIRST(Comparator.comparing(Transaction::getType).reversed());

    private final Comparator<Transaction> comparator;

    SortOption(Comparator<Transaction> comparator) {
        this.comparator = comparator;
    }

    /** Mengembalikan comparator yang sesuai dengan opsi ini. */
    public Comparator<Transaction> comparator() {
        return comparator;
    }
}
