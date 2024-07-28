package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {

    private final FilterService filterService;

    public Bot(FilterService filterService) {
        this.filterService = filterService;

    }


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
            String userName = update.getMessage().getFrom().getUserName();
            Long userId = update.getMessage().getFrom().getId();
            String message = update.getMessage().getText();

            String response = filterService.handleMessage(userName, message);
            sendText(userId, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    } // Реагирует на новые сообщения боту

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what)
                .build();
        try {
            execute(sm);

        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    } // Отправляет сообщение в телеграмме
}