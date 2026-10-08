package usecase;

import domain.entity.Activity;
import domain.entity.Day;
import domain.entity.SortOption;
import domain.exception.EntityNotFoundException;
import domain.exception.ValidationException;
import domain.repository.IActivityRepository;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

/**
 * Use case yang menangani logika bisnis jadwal kegiatan.
 * Tidak melakukan I/O — hanya memproses data dan melempar checked exception
 * domain jika operasi tidak valid atau data tidak ditemukan.
 */
public class ActivityUseCase {
    /** Port repository yang di-inject dari luar (Dependency Inversion). */
    private final IActivityRepository activityRepository;

    public ActivityUseCase(IActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /** Mengambil semua kegiatan. */
    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    /** Menambahkan kegiatan baru setelah validasi judul, hari, dan waktu. */
    public Activity addActivity(String title, String dayText, String timeText) throws ValidationException {
        validateTitle(title);
        Day day = Day.fromLabel(dayText);
        LocalTime time = parseTime(timeText);
        return activityRepository.save(title.trim(), day, time);
    }

    /**
     * Mengubah kegiatan secara parsial. Parameter {@code null} berarti field tidak diubah.
     * Seluruh input divalidasi terlebih dahulu sebelum ada perubahan pada kegiatan.
     */
    public void updateActivity(int id, String title, String dayText, String timeText)
            throws EntityNotFoundException, ValidationException {
        Optional<Activity> found = activityRepository.findById(id);
        if (found.isEmpty()) {
            throw new EntityNotFoundException("Kegiatan", id);
        }

        if (title != null) {
            validateTitle(title);
        }
        Day day = dayText != null ? Day.fromLabel(dayText) : null;
        LocalTime time = timeText != null ? parseTime(timeText) : null;

        Activity activity = found.get();
        if (title != null) {
            activity.changeTitle(title.trim());
        }
        if (day != null) {
            activity.changeDay(day);
        }
        if (time != null) {
            activity.changeTime(time);
        }
        activityRepository.update(activity);
    }

    /** Menghapus kegiatan berdasarkan ID; melempar exception jika tidak ditemukan. */
    public void removeActivity(int id) throws EntityNotFoundException {
        if (!activityRepository.deleteById(id)) {
            throw new EntityNotFoundException("Kegiatan", id);
        }
    }

    /** Mencari kegiatan yang judulnya mengandung kata kunci (case-insensitive). */
    public List<Activity> searchActivities(String keyword) throws ValidationException {
        if (keyword == null || keyword.isBlank()) {
            throw new ValidationException("Kata kunci tidak boleh kosong");
        }
        String lowerKeyword = keyword.trim().toLowerCase();
        return activityRepository.findBy(a -> a.getTitle().toLowerCase().contains(lowerKeyword));
    }

    /** Mengurutkan kegiatan sesuai kriteria {@link SortOption}. */
    public List<Activity> sortActivities(SortOption option) {
        return activityRepository.findAll().stream()
                .sorted(option.comparator())
                .toList();
    }

    private void validateTitle(String title) throws ValidationException {
        if (title == null || title.isBlank()) {
            throw new ValidationException("Judul kegiatan tidak boleh kosong");
        }
    }

    /** Mengubah teks berformat HH:mm menjadi {@link LocalTime}. */
    private LocalTime parseTime(String text) throws ValidationException {
        try {
            return LocalTime.parse(text == null ? "" : text.trim());
        } catch (DateTimeParseException e) {
            throw new ValidationException("Waktu tidak valid (format HH:mm)", e);
        }
    }
}
