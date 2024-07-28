package org.example;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;

public class TimetableRepository {
    private final JdbcTemplate jdbcTemplate;

    public TimetableRepository() {
        this.jdbcTemplate = createJdbcTemplate();
    }

    private JdbcTemplate createJdbcTemplate(){
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setServerNames(new String[] {"localhost"});
        ds.setDatabaseName("aqua_filter");
        ds.setUser("postgres");
        ds.setPassword("123456");

        return new JdbcTemplate(ds);
    }

    public LocalDate loadDateOfChange(String userName) {
        return jdbcTemplate.queryForObject(
                "SELECT date_of_change FROM timetable WHERE user_name = ?",
                LocalDate.class,
                userName
        );
    } // Получает из БД дату последней замены фильтра

    public boolean isKnownUser(String userName){
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT (user_name) FROM timetable WHERE user_name = ?",
                Integer.class,
                userName
        );
        return count != null && count > 0;
    }// Идентификация

    /**
     * Обновляет дату замены фильтра в БД
     */
    public int saveDateOfChange(String userName, LocalDate date) {
        if (isKnownUser(userName)) {
            return jdbcTemplate.update("UPDATE timetable SET date_of_change=? WHERE user_name=?",
                    date,
                    userName
            );
        } else {
            return jdbcTemplate.update("INSERT INTO timetable (user_name, date_of_change) VALUES (?, ?)",
                    userName,
                    date
            );
        }

    }
}