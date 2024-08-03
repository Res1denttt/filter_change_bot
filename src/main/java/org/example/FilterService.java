package org.example;

import java.time.LocalDate;

public class FilterService {

    private final TimetableRepository repository;

    public FilterService(TimetableRepository timetableRepository) {
        this.repository = timetableRepository;
    }

    /**
     * Обрабатывает сообщение
     */
    public String handleMessage(String userName, String message) {
        if (message.trim().contains("Фильтры заменены")) {
            repository.saveDateOfChange(userName, parseDate(message));
            return "Данные обновлены";

        }

        if (!repository.isKnownUser(userName)) {
            return "Когда вы меняли фильтры? Введите дату в формате: \"Фильтры заменены ГГГГ-ММ-ДД\"";
        }

        if (message.trim().contains("Когда менялись фильтры")) {
            LocalDate localDate = repository.loadDateOfChange(userName);
            return "Фильтры менялись: " + localDate;
        }

        return "Если Вы поменяли фильтры, введите дату замены в формате: \"Фильтры заменены ГГГГ-ММ-ДД\"\nЕсли Вы хотите узнать дату последней замены, введите: \"Когда менялись фильтры?\"";

    }


    private LocalDate parseDate(String message){
        String date = message.trim().substring(message.length() - 10);
        return LocalDate.parse(date);
    }
}