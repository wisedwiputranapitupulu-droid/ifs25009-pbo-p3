package framework.view;

import adapter.presenter.ActivityPresenter;
import domain.entity.SortOption;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import framework.util.InputUtil;
import usecase.ActivityUseCase;

/**
 * Tampilan konsol aplikasi jadwal kegiatan.
 * Menerima input user, memanggil use case, dan menangani exception domain.
 * Layer ini tidak mengandung logika bisnis, hanya interaksi dengan user.
 */
public class ActivityView {
    private final ActivityUseCase activityUseCase;
    private final ActivityPresenter presenter;

    public ActivityView(ActivityUseCase activityUseCase, ActivityPresenter presenter) {
        this.activityUseCase = activityUseCase;
        this.presenter = presenter;
    }

    /** Menampilkan menu utama dan loop interaksi user. */
    public void show() {
        boolean running = true;
        while (running) {
            presenter.showActivities(activityUseCase.getAllActivities());
            printMenu();
            String input = InputUtil.input("Pilih");
            System.out.println();

            switch (input) {
                case "1" -> addActivity();
                case "2" -> updateActivity();
                case "3" -> searchActivity();
                case "4" -> sortActivity();
                case "5" -> removeActivity();
                case "x" -> running = false;
                default -> presenter.showInvalidChoice();
            }

            if (running) {
                System.out.println();
            }
        }
    }

    private void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. Tambah");
        System.out.println("2. Ubah");
        System.out.println("3. Cari");
        System.out.println("4. Urutkan");
        System.out.println("5. Hapus");
        System.out.println("x. Keluar");
    }

    /** Form tambah kegiatan baru. */
    private void addActivity() {
        System.out.println("[Menambah Kegiatan]");

        String title = InputUtil.input("Judul (x Jika Batal)");
        if (title.equals("x")) {
            return;
        }

        String day = InputUtil.input("Hari (x Jika Batal)");
        if (day.equals("x")) {
            return;
        }

        String time = InputUtil.input("Waktu (x Jika Batal)");
        if (time.equals("x")) {
            return;
        }

        try {
            presenter.showAddSuccess(
                activityUseCase.addActivity(title, day, time)
            );
        } catch (ValidationException e) {
            presenter.showValidationError(e.getMessage());
        }
    }

    /** Form ubah kegiatan (parsial): field kosong berarti tidak diubah. */
    private void updateActivity() {
        System.out.println("[Mengubah Kegiatan]");

        String strId = InputUtil.input("ID Kegiatan yang diubah (x Jika Batal)");
        if (strId.equals("x")) {
            return;
        }

        int id;

        try {
            id = parseId(strId);
        } catch (ValidationException e) {
            presenter.showValidationError(e.getMessage());
            return;
        }

        String title = InputUtil.input("Judul Baru (Kosongkan jika tidak ingin mengubah)");
        String day = InputUtil.input("Hari Baru (Kosongkan jika tidak ingin mengubah)");
        String time = InputUtil.input("Waktu Baru (Kosongkan jika tidak ingin mengubah)");

        try {
            activityUseCase.updateActivity(
                id,
                emptyToNull(title),
                emptyToNull(day),
                emptyToNull(time)
            );

            presenter.showUpdateSuccess();

        } catch (ValidationException e) {
            presenter.showValidationError(e.getMessage());

        } catch (EntityNotFoundException e) {
            presenter.showUpdateFailed(e.getId());
        }
    }

    /** Form cari kegiatan berdasarkan judul. */
    private void searchActivity() {
        System.out.println("[Mencari Kegiatan]");

        String keyword = InputUtil.input("Kata Kunci (x Jika Batal)");
        if (keyword.equals("x")) {
            return;
        }

        try {
            presenter.showSearchResults(
                activityUseCase.searchActivities(keyword),
                keyword
            );
        } catch (ValidationException e) {
            presenter.showValidationError(e.getMessage());
        }
    }

    /** Form urutkan kegiatan berdasarkan pilihan user. */
    private void sortActivity() {
        System.out.println("[Mengurutkan Kegiatan]");

        System.out.println("Pilihan Pengurutan:");
        System.out.println("1. Hari (Senin -> Minggu)");
        System.out.println("2. Waktu (Awal -> Akhir)");
        System.out.println("3. Judul (A-Z)");
        System.out.println("4. Judul (Z-A)");
        System.out.println("x. Batal");

        String input = InputUtil.input("Pilih");
        System.out.println();

        if (input.equals("x")) {
            return;
        }

        SortOption option = mapSortOption(input);

        if (option == null) {
            presenter.showInvalidSortOption();
            return;
        }

        presenter.showSortedActivities(
            activityUseCase.sortActivities(option)
        );
    }

    /** Form hapus kegiatan berdasarkan ID. */
    private void removeActivity() {
        System.out.println("[Menghapus Kegiatan]");

        String strId = InputUtil.input("[ID Kegiatan] yang dihapus (x Jika Batal)");
        if (strId.equals("x")) {
            return;
        }

        try {
            activityUseCase.removeActivity(parseId(strId));
            presenter.showRemoveSuccess();

        } catch (ValidationException e) {
            presenter.showValidationError(e.getMessage());

        } catch (EntityNotFoundException e) {
            presenter.showRemoveFailed(e.getId());
        }
    }

    private int parseId(String value) throws ValidationException {
        try {
            return Integer.parseInt(value.trim());

        } catch (NumberFormatException e) {
            throw new ValidationException("ID tidak valid");
        }
    }

    /** Mengubah input kosong menjadi null (artinya field tidak diubah). */
    private String emptyToNull(String value) {
        return value.trim().isEmpty() ? null : value;
    }

    /** Memetakan pilihan menu (1-4) ke {@link SortOption} domain. */
    private SortOption mapSortOption(String input) {
        return switch (input) {
            case "1" -> SortOption.DAY;
            case "2" -> SortOption.TIME;
            case "3" -> SortOption.TITLE_ASC;
            case "4" -> SortOption.TITLE_DESC;
            default -> null;
        };
    }
}