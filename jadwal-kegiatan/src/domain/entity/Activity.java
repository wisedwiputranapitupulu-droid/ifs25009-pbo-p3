package domain.entity;

import java.time.LocalTime;

/**
 * Entity inti yang merepresentasikan satu kegiatan terjadwal.
 * Berada di layer domain — bebas dari urusan tampilan maupun penyimpanan.
 */
public class Activity {
    /** ID unik kegiatan, tidak boleh diubah setelah dibuat. */
    private final int id;

    private String title;
    private Day day;
    private LocalTime time;

    public Activity(int id, String title, Day day, LocalTime time) {
        this.id = id;
        this.title = title;
        this.day = day;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Day getDay() {
        return day;
    }

    public LocalTime getTime() {
        return time;
    }

    /** Mengubah judul kegiatan. */
    public void changeTitle(String title) {
        this.title = title;
    }

    /** Mengubah hari kegiatan. */
    public void changeDay(Day day) {
        this.day = day;
    }

    /** Mengubah waktu kegiatan. */
    public void changeTime(LocalTime time) {
        this.time = time;
    }
}
