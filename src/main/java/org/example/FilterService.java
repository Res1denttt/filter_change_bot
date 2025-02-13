package org.example;

import java.time.LocalDate;
import java.util.List;

public class FilterService {

    private final TimetableRepository repository;

    public FilterService(TimetableRepository timetableRepository) {
        this.repository = timetableRepository;
    }

    /**
     * Обрабатывает сообщение
     */
    public String handleMessage(String userName, String message, long userId) {
        if (message.trim().contains("Фильтры заменены")) {
            repository.saveDateOfChange(userName, parseDate(message), userId);
            return "Данные обновлены";

        }

//        if (!repository.isKnownUser(userName)) {
//            return "Когда вы меняли фильтры? Введите дату в формате: \"Фильтры заменены ГГГГ-ММ-ДД\"";
//        }

        if (message.trim().contains("Когда менялись фильтры")) {
            LocalDate localDate = repository.loadDateOfChange(userName);
            return "Фильтры менялись: " + localDate;
        }

//        return "Если Вы поменяли фильтры, введите дату замены в формате: \"Фильтры заменены ГГГГ-ММ-ДД\"\nЕсли Вы хотите узнать дату последней замены, введите: \"Когда менялись фильтры?\"";
        return  "Привет! Чем могу помочь?";
    }

    /**
     * Обрабатывает комманду из меню
     */
    public String handleCommand(String userName, String command){
        if (command.contains("date_of_change")){
            if (!repository.isKnownUser(userName)) {
                return "Хм, кажется мы еще не знакомы. Напиши дату замены фильтров в формате: \"Фильтры заменены ГГГГ-ММ-ДД\"";
            }
            LocalDate localDate = repository.loadDateOfChange(userName);
            return "Фильтры менялись: " + localDate;
        }

        if (command.contains("filters_changed")){
            return "Ок, напиши дату замены фильтров в формате: \"Фильтры заменены ГГГГ-ММ-ДД\"";
        }
        return "Хм, кажется, что-то не так. Попробуй еще раз чуть позже";
    }

    /**
     * Получает список пользователей для отправки уведомления
     */
    public List<Long> checkUsersToNotify(){
        return repository.getUsersToNotify();
    }

    private LocalDate parseDate(String message){
        String date = message.trim().substring(message.length() - 10);
        return LocalDate.parse(date);
    }
}