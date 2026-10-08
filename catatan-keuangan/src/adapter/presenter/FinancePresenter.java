package adapter.presenter;

import domain.entity.Transaction;
import domain.entity.TransactionType;
import java.util.List;

/**
 * Presenter yang memformat data dari use case menjadi output layar.
 * Format tampilan (termasuk label Pemasukan/Pengeluaran) sengaja dipisah dari entity.
 */
public class FinancePresenter {
    /** Memformat satu transaksi menjadi baris teks. */
    private String format(Transaction t) {
        String type = t.getType() == TransactionType.INCOME ? "Pemasukan" : "Pengeluaran";
        return String.format("[%d] %s | Rp %d | [%s]", t.getId(), t.getDescription(), t.getAmount(), type);
    }

    /** Helper umum untuk menampilkan daftar transaksi. */
    private void printList(List<Transaction> list, String header, String emptyMessage) {
        System.out.println(header);
        if (list.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        for (Transaction t : list) {
            System.out.println(format(t));
        }
    }

    /** Menampilkan daftar semua transaksi beserta saldo. */
    public void showTransactions(List<Transaction> list, long balance) {
        printList(list, "Daftar Transaksi:", "- Belum ada transaksi!");
        System.out.printf("Saldo: Rp %d%n", balance);
    }

    /** Menampilkan hasil pencarian berdasarkan keterangan. */
    public void showSearchResults(List<Transaction> list, String keyword) {
        printList(list, "Hasil Pencarian: \"" + keyword + "\"", "- Transaksi tidak ditemukan!");
    }

        /** Menampilkan daftar transaksi yang sudah diurutkan. */
    public void showSortedTransactions(List<Transaction> list) {
        printList(list, "Daftar Transaksi (Terurut):", "- Belum ada transaksi!");
    }

    /** Menampilkan saldo saat ini. */
    public void showBalance(long balance) {
        System.out.printf("Saldo saat ini: Rp %d%n", balance);
    }

    public void showAddSuccess(Transaction t) {
        System.out.printf("Berhasil menambah transaksi: %s%n", format(t));
    }

    public void showRemoveSuccess() {
        System.out.println("Berhasil menghapus transaksi.");
    }

    public void showRemoveFailed(int id) {
        System.out.printf("[!] Gagal menghapus transaksi dengan ID: %d.%n", id);
    }

    /** Menampilkan pesan kesalahan validasi dari domain. */
    public void showValidationError(String message) {
        System.out.println("[!] " + message + "!");
    }

    public void showInvalidChoice() {
        System.out.println("[!] Pilihan tidak dimengerti.");
    }

    public void showInvalidSortOption() {
        System.out.println("[!] Pilihan tidak valid!");
    }
}
