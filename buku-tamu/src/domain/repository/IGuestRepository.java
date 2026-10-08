package domain.repository;

import domain.entity.Guest;

/**
 * Port spesifik Guest yang memperluas generic {@link IRepository}.
 * Menambahkan method pembuatan Guest baru dengan penomoran ID otomatis.
 */
public interface IGuestRepository extends IRepository<Guest, Integer> {
    /**
     * Menyimpan tamu baru.
     *
     * @param name    nama tamu
     * @param purpose tujuan kunjungan
     * @return tamu yang tersimpan (lengkap dengan ID)
     */
    Guest save(String name, String purpose);
}
