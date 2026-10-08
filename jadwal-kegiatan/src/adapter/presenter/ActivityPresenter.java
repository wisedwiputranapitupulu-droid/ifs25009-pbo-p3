package adapter.presenter;

import domain.entity.Activity;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Presenter yang memformat data dari use case menjadi output layar.
 * Format tampilan sengaja dipisah dari entity, use case, dan view.
 */
public class ActivityPresenter {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    /** Memformat satu kegiatan menjadi baris teks. */
    private String format(Activity activity) {
        return String.format("[%d] %s | Hari: %s | Pukul: %s",
                activity.getId(), activity.getTitle(),
                activity.getDay().getLabel(), activity.getTime().format(TIME_FORMAT));
    }

    /** Helper umum untuk menampilkan daftar kegiatan. */
    private void printList(List<Activity> activities, String header, String emptyMessage) {
        System.out.println(header);
        if (activities.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        for (Activity activity : activities) {
            System.out.println(format(activity));
        }
    }

    public void showActivities(List<Activity> activities) {
        printList(activities, "Daftar Kegiatan:", "- Data kegiatan belum tersedia!");
    }

    public void showSearchResults(List<Activity> activities, String keyword) {
        printList(activities, "Hasil Pencarian: \"" + keyword + "\"", "- Kegiatan tidak ditemukan!");
    }

    public void showSortedActivities(List<Activity> activities) {
        printList(activities, "Daftar Kegiatan (Terurut):", "- Data kegiatan belum tersedia!");
    }

    public void showAddSuccess(Activity activity) {
        System.out.printf("Berhasil menambah kegiatan: %s%n", format(activity));
    }

    public void showUpdateSuccess() {
        System.out.println("Berhasil mengubah kegiatan.");
    }

    public void showUpdateFailed(int id) {
        System.out.printf("[!] Gagal mengubah kegiatan dengan ID: %d.%n", id);
    }

    public void showRemoveSuccess() {
        System.out.println("Berhasil menghapus kegiatan.");
    }

    public void showRemoveFailed(int id) {
        System.out.printf("[!] Gagal menghapus kegiatan dengan ID: %d.%n", id);
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
