package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws TelegramApiException {
        startBot();
    }

    private static void startBot() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        TimetableRepository timetableRepository = new TimetableRepository();
        FilterService filterService = new FilterService(timetableRepository);
        Bot bot = new Bot(filterService);
        botsApi.registerBot(bot);
        startNotifications(bot);
    }

    private static void startNotifications (Bot bot){
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
        service.scheduleAtFixedRate(() -> bot.sendNotification(), 0L, 1L, TimeUnit.DAYS);
    }
}

