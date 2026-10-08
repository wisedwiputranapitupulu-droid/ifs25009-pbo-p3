package domain.entity;

/**
 * Entity inti yang merepresentasikan satu transaksi keuangan.
 * Berada di layer domain — bebas dari urusan tampilan maupun penyimpanan.
 */
public class Transaction {
    /** ID unik transaksi, tidak boleh diubah setelah dibuat. */
    private final int id;

    /** Jenis transaksi tidak berubah setelah dicatat. */
    private final TransactionType type;

    /** Keterangan transaksi. */
    private String description;

    /** Jumlah uang (dalam Rupiah), selalu bernilai positif. */
    private long amount;

    public Transaction(int id, String description, long amount, TransactionType type) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public long getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    /** Mengubah keterangan transaksi. */
    public void changeDescription(String description) {
        this.description = description;
    }

    /** Mengubah jumlah transaksi. */
    public void changeAmount(long amount) {
        this.amount = amount;
    }
}
