package usecase;

import domain.entity.Item;
import domain.entity.SortOption;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import domain.repository.IItemRepository;
import java.util.List;
import java.util.Optional;

/**
 * Use case yang menangani logika bisnis aplikasi inventaris barang.
 * Tidak melakukan I/O, hanya memproses data dan melempar checked exception
 * domain jika operasi tidak valid atau data tidak ditemukan.
 */
public class ItemUseCase {
    /** Port repository yang di-inject dari luar (Dependency Inversion). */
    private final IItemRepository itemRepository;

    public ItemUseCase(IItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /** Mengambil semua barang yang tersedia. */
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    /** Menambahkan barang baru setelah validasi nama, stok, dan kategori. */
    public Item addItem(String name, int quantity, String category) throws ValidationException {
        validateName(name);
        validateQuantity(quantity);
        validateCategory(category);
        return itemRepository.save(name.trim(), quantity, category.trim());
    }

    /** Menghapus barang berdasarkan ID; melempar exception jika tidak ditemukan. */
    public void removeItem(int id) throws EntityNotFoundException {
        boolean deleted = itemRepository.deleteById(id);
        if (!deleted) {
            throw new EntityNotFoundException(id);
        }
    }

    /**
     * Mengubah nama, stok, dan/atau kategori barang.
     * Parameter {@code null} berarti field tersebut tidak diubah (update parsial).
     */
    public void updateItem(int id, String name, Integer quantity, String category)
            throws EntityNotFoundException, ValidationException {
        Optional<Item> found = itemRepository.findById(id);
        if (found.isEmpty()) {
            throw new EntityNotFoundException(id);
        }

        // Validasi semua field dulu agar tidak ada perubahan setengah jalan
        if (name != null) {
            validateName(name);
        }
        if (quantity != null) {
            validateQuantity(quantity);
        }
        if (category != null) {
            validateCategory(category);
        }

        Item item = found.get();
        if (name != null) {
            item.changeName(name.trim());
        }
        if (quantity != null) {
            item.changeQuantity(quantity);
        }
        if (category != null) {
            item.changeCategory(category.trim());
        }
        itemRepository.update(item);
    }

    /** Mencari barang yang namanya mengandung kata kunci (case-insensitive). */
    public List<Item> searchItems(String keyword) throws ValidationException {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ValidationException("Kata kunci tidak boleh kosong");
        }
        String lowerKeyword = keyword.trim().toLowerCase();
        return itemRepository.findBy(item -> item.getName().toLowerCase().contains(lowerKeyword));
    }

    /** Mengurutkan barang sesuai kriteria {@link SortOption} yang dipilih. */
    public List<Item> sortItems(SortOption option) {
        return itemRepository.findAll().stream()
                .sorted(option.comparator())
                .toList();
    }

    private void validateName(String name) throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Nama barang tidak boleh kosong");
        }
    }

    private void validateQuantity(int quantity) throws ValidationException {
        if (quantity <= 0) {
            throw new ValidationException("Jumlah stok harus lebih dari 0");
        }
    }

    private void validateCategory(String category) throws ValidationException {
        if (category == null || category.trim().isEmpty()) {
            throw new ValidationException("Kategori tidak boleh kosong");
        }
    }
}
