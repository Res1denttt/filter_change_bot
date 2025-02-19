package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private final FilterService filterService;

    public Bot(FilterService filterService) {
        this.filterService = filterService;
    }

    InlineKeyboardButton dateQueryButton = InlineKeyboardButton.builder()
            .text("Узнать дату прошлой замены фильтров").callbackData("date_of_change")
            .build();

    InlineKeyboardButton dateInsertButton = InlineKeyboardButton.builder()
            .text("Сообщить о замене фильтров").callbackData("filters_changed")
            .build();

    InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
            .keyboardRow(List.of(dateQueryButton))
            .keyboardRow(List.of(dateInsertButton))
            .build();




    @Override
    public String getBotUsername() {
        return "Change_Aqua_Filter_Bot";
    }

    @Override
    public String getBotToken() {
        return "7272879042:AAEgkpOb0X2t3775EHkm7a4UirZReW1_pvw";
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            String message = getMessage(update);
            String command = getCommand(update);
            String userName = getUserName(message, update);
            Long userId = getUserId(message, update);
            String response = getResponse(userName, message, command, userId);
            sendMenu(userId, response, keyboard);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getMessage(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getText();
        }

        return null;
    }

    private String getCommand(Update update) {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getData();
        }

        return null;
    }

    public String getResponse(String userName, String message, String command, long userId) {
        if (message != null){
            return filterService.handleMessage(userName, message, userId);
        }
        return filterService.handleCommand(userName, command);
    }

    private String getUserName(String message, Update update) {
        if (message != null){
            return update.getMessage().getFrom().getUserName();
        }
        return  update.getCallbackQuery().getFrom().getUserName();
    }

    private Long getUserId(String message, Update update) {
        if (message != null){
            return update.getMessage().getFrom().getId();
        }
        return update.getCallbackQuery().getFrom().getId();
    }

    /**
     * Отправляет сообщение в телеграмме
     */
    private void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what)
                .build();
        try {
            execute(sm);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMenu(Long who, String txt, InlineKeyboardMarkup kb) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .parseMode("HTML").text(txt)
                .replyMarkup(kb)
                .build();

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNotification() {
        List<Long> who = filterService.checkUsersToNotify();
        if(!who.isEmpty()){
            for(long user : who){
                sendText(user, "Настало время заменить фильтры");
            }
        }
    }
}