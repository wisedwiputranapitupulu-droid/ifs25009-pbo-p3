package adapter.presenter;

import domain.entity.Item;
import java.util.List;

/**
 * Presenter yang memformat data dari use case menjadi output layar.
 * Format tampilan sengaja dipisah dari entity, use case, dan view.
 */
public class ItemPresenter {
    /** Memformat satu barang menjadi baris teks. */
    private String format(Item item) {
        return String.format("[%d] %s | Stok: %d | Kategori: %s",
                item.getId(), item.getName(), item.getQuantity(), item.getCategory());
    }

    /** Helper umum untuk menampilkan daftar barang. */
    private void printList(List<Item> items, String header, String emptyMessage) {
        System.out.println(header);
        if (items.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        for (Item item : items) {
            System.out.println(format(item));
        }
    }

    public void showItems(List<Item> items) {
        printList(items, "Daftar Barang:", "- Data barang belum tersedia!");
    }

    public void showSearchResults(List<Item> items, String keyword) {
        printList(items, "Hasil Pencarian: \"" + keyword + "\"", "- Barang tidak ditemukan!");
    }

    public void showSortedItems(List<Item> items) {
        printList(items, "Daftar Barang (Terurut):", "- Data barang belum tersedia!");
    }

    public void showAddSuccess(Item item) {
        System.out.printf("Berhasil menambah barang: %s%n", format(item));
    }

    public void showUpdateSuccess() {
        System.out.println("Berhasil mengubah stok barang.");
    }

    public void showUpdateFailed(int id) {
        System.out.printf("[!] Gagal mengubah stok barang dengan ID: %d.%n", id);
    }

    public void showRemoveSuccess() {
        System.out.println("Berhasil menghapus barang.");
    }

    public void showRemoveFailed(int id) {
        System.out.printf("[!] Gagal menghapus barang dengan ID: %d.%n", id);
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