package framework.view;

import adapter.presenter.FinancePresenter;
import domain.entity.SortOption;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import framework.util.InputUtil;
import usecase.FinanceUseCase;

/**
 * Tampilan konsol aplikasi catatan keuangan.
 * Menerima input user, memanggil use case, dan menangani exception domain.
 * Layer ini tidak mengandung logika bisnis — hanya interaksi dengan user.
 */
public class FinanceView {
    private final FinanceUseCase financeUseCase;
    private final FinancePresenter presenter;

    public FinanceView(FinanceUseCase financeUseCase, FinancePresenter presenter) {
        this.financeUseCase = financeUseCase;
        this.presenter = presenter;
    }

    /** Menampilkan menu utama dan loop interaksi user. */
    public void show() {
        boolean running = true;
        while (running) {
            presenter.showTransactions(financeUseCase.getAllTransactions(), financeUseCase.getBalance());
            printMenu();
            String input = InputUtil.input("Pilih");
            System.out.println();

            switch (input) {
                case "1" -> addTransaction(true);
                case "2" -> addTransaction(false);
                case "3" -> searchTransaction();
                case "4" -> sortTransaction();
                case "5" -> presenter.showBalance(financeUseCase.getBalance());
                case "6" -> removeTransaction();
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
        System.out.println("1. Tambah Pemasukan");
        System.out.println("2. Tambah Pengeluaran");
        System.out.println("3. Cari");
        System.out.println("4. Urutkan");
        System.out.println("5. Lihat Saldo");
        System.out.println("6. Hapus");
        System.out.println("x. Keluar");
    }

    /** Form tambah transaksi (pemasukan jika {@code income} true, selain itu pengeluaran). */
    private void addTransaction(boolean income) {
        System.out.println(income ? "[Tambah Pemasukan]" : "[Tambah Pengeluaran]");
        String description = InputUtil.input("Keterangan (x Jika Batal)");
        if (description.equals("x")) {
            return;
        }
        String strAmount = InputUtil.input("Jumlah");
        try {
            long amount = parseAmount(strAmount);
            presenter.showAddSuccess(income
                    ? financeUseCase.addIncome(description, amount)
                    : financeUseCase.addExpense(description, amount));
        } catch (ValidationException e) {
            presenter.showValidationError(e.getMessage());
        }
    }

    /** Form cari transaksi berdasarkan keterangan. */
    private void searchTransaction() {
        System.out.println("[Cari Transaksi]");
        String keyword = InputUtil.input("Kata Kunci (x Jika Batal)");
        if (keyword.equals("x")) {
            return;
        }
        try {
            presenter.showSearchResults(financeUseCase.searchTransactions(keyword), keyword);
        } catch (ValidationException e) {
            presenter.showValidationError(e.getMessage());
        }
    }

    /** Form urutkan transaksi berdasarkan pilihan user. */
    private void sortTransaction() {
        System.out.println("[Urutkan Transaksi]");
        System.out.println("1. Jumlah (Terkecil)");
        System.out.println("2. Jumlah (Terbesar)");
        System.out.println("3. Pemasukan Dulu");
        System.out.println("4. Pengeluaran Dulu");
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
        presenter.showSortedTransactions(financeUseCase.sortTransactions(option));
    }

    /** Form hapus transaksi berdasarkan ID. */
    private void removeTransaction() {
        System.out.println("[Hapus Transaksi]");
        String strId = InputUtil.input("ID Transaksi (x Jika Batal)");
        if (strId.equals("x")) {
            return;
        }
        try {
            financeUseCase.removeTransaction(parseId(strId));
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

    private long parseAmount(String value) throws ValidationException {
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("Jumlah tidak valid");
        }
    }

    /** Memetakan pilihan menu (1-4) ke {@link SortOption} domain. */
    private SortOption mapSortOption(String input) {
        return switch (input) {
            case "1" -> SortOption.AMOUNT_ASC;
            case "2" -> SortOption.AMOUNT_DESC;
            case "3" -> SortOption.INCOME_FIRST;
            case "4" -> SortOption.EXPENSE_FIRST;
            default -> null;
        };
    }
}
