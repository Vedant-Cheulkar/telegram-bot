package com.vedant.telegrambot.config;

import com.vedant.telegrambot.bot.TelegramBot;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBotInitializer {

    private final TelegramBot telegramBot;

    public TelegramBotInitializer(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {

        try {

            TelegramBotsApi botsApi =
                    new TelegramBotsApi(DefaultBotSession.class);

            botsApi.registerBot(telegramBot);

            System.out.println("BOT REGISTERED SUCCESSFULLY");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}