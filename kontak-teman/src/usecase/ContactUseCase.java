package usecase;

import domain.entity.Contact;
import domain.entity.SortOption;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import domain.repository.IContactRepository;
import java.util.List;
import java.util.Optional;

/**
 * Use case yang menangani logika bisnis aplikasi kontak teman.
 * Tidak melakukan I/O, hanya memproses data dan melempar checked exception
 * domain jika operasi tidak valid atau data tidak ditemukan.
 */
public class ContactUseCase {
    /** Port repository yang di-inject dari luar (Dependency Inversion). */
    private final IContactRepository contactRepository;

    public ContactUseCase(IContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /** Mengambil semua kontak yang tersedia. */
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    /** Menambahkan kontak baru setelah validasi nama. */
    public Contact addContact(String name, String phone, String email) throws ValidationException {
        validateName(name);
        return contactRepository.save(name.trim(), phone, email);
    }

    /** Menghapus kontak berdasarkan ID; melempar exception jika tidak ditemukan. */
    public void removeContact(int id) throws EntityNotFoundException {
        boolean deleted = contactRepository.deleteById(id);
        if (!deleted) {
            throw new EntityNotFoundException(id);
        }
    }

    /**
     * Mengubah nama, telepon, dan/atau email kontak.
     * Parameter {@code null} berarti field tersebut tidak diubah (update parsial).
     * Email diterima apa adanya, tanpa validasi format (sesuai TC-14).
     */
    public void updateContact(int id, String name, String phone, String email)
            throws EntityNotFoundException, ValidationException {
        Optional<Contact> found = contactRepository.findById(id);
        if (found.isEmpty()) {
            throw new EntityNotFoundException(id);
        }

        if (name != null) {
            validateName(name);
        }

        Contact contact = found.get();
        if (name != null) {
            contact.changeName(name.trim());
        }
        if (phone != null) {
            contact.changePhone(phone);
        }
        if (email != null) {
            contact.changeEmail(email);
        }
        contactRepository.update(contact);
    }

    /** Mencari kontak yang namanya mengandung kata kunci (case-insensitive). */
    public List<Contact> searchContacts(String keyword) throws ValidationException {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new ValidationException("Kata kunci tidak boleh kosong");
        }
        String lowerKeyword = keyword.trim().toLowerCase();
        return contactRepository.findBy(contact -> contact.getName().toLowerCase().contains(lowerKeyword));
    }

    /** Mengurutkan kontak sesuai kriteria {@link SortOption} yang dipilih. */
    public List<Contact> sortContacts(SortOption option) {
        return contactRepository.findAll().stream()
                .sorted(option.comparator())
                .toList();
    }

    private void validateName(String name) throws ValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Nama tidak boleh kosong");
        }
    }
}
