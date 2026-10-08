package domain.repository;

import domain.entity.Activity;
import domain.entity.Day;
import java.time.LocalTime;

/**
 * Port spesifik Activity yang memperluas generic {@link IRepository}.
 * Menambahkan method pembuatan Activity baru dengan penomoran ID otomatis.
 */
public interface IActivityRepository extends IRepository<Activity, Integer> {
    /**
     * Menyimpan kegiatan baru.
     *
     * @return kegiatan yang tersimpan (lengkap dengan ID)
     */
    Activity save(String title, Day day, LocalTime time);
}
