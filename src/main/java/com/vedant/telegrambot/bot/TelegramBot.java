package com.vedant.telegrambot.bot;

import com.vedant.telegrambot.config.BotConfig;
import com.vedant.telegrambot.entity.WorkEntryEntity;
import com.vedant.telegrambot.repository.WorkEntryRepository;
import com.vedant.telegrambot.service.ExcelService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final WorkEntryRepository workEntryRepository;
    private final ExcelService excelService;

    public TelegramBot(
            BotConfig botConfig,
            WorkEntryRepository workEntryRepository,
            ExcelService excelService
    ) {
        this.botConfig = botConfig;
        this.workEntryRepository = workEntryRepository;
        this.excelService = excelService;

        System.out.println("TelegramBot Bean Created");
    }

    @Override
    public String getBotUsername() {
        return botConfig.getUsername();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        try {

            if (update.hasMessage()
                    && update.getMessage().hasText()) {

                String userMessage =
                        update.getMessage().getText().trim();

                Long chatId =
                        update.getMessage().getChatId();

                System.out.println(
                        "User Message : "
                                + userMessage
                );

                // ==========================
                // EXCEL COMMAND
                // ==========================

                if (userMessage.equalsIgnoreCase("/excel")) {

                    String fileName =
                            excelService.generateExcel();

                    sendExcel(
                            chatId,
                            fileName
                    );

                    return;
                }

                // ==========================
                // TOTAL COMMAND
                // ==========================

                if (userMessage.equalsIgnoreCase("/total")) {

                    long total =
                            workEntryRepository.findAll()
                                    .stream()
                                    .mapToLong(
                                            WorkEntryEntity::getAmount
                                    )
                                    .sum();

                    sendMessage(
                            chatId,
                            """
                            📊 Total Summary

                            Total Works: %d

                            Total Amount:
                            ₹%,d
                            """.formatted(
                                    workEntryRepository.count(),
                                    total
                            )
                    );

                    return;
                }

                // ==========================
                // LIST COMMAND
                // ==========================

                if (userMessage.equalsIgnoreCase("/list")) {

                    if (workEntryRepository.count() == 0) {

                        sendMessage(
                                chatId,
                                "No works added yet."
                        );

                        return;
                    }

                    StringBuilder builder =
                            new StringBuilder();

                    builder.append(
                            "📋 Work List\n\n"
                    );

                    int srNo = 1;

                    for (WorkEntryEntity work :
                            workEntryRepository.findAll()) {

                        builder.append(srNo++)
                                .append(". ")
                                .append(work.getWorkName())
                                .append(" - ₹")
                                .append(
                                        String.format(
                                                "%,d",
                                                work.getAmount()
                                        )
                                )
                                .append("\n");
                    }

                    sendMessage(
                            chatId,
                            builder.toString()
                    );

                    return;
                }

                // ==========================
                // SAVE WORK
                // ==========================

                String[] parts =
                        userMessage.split("\\s+");

                if (parts.length < 2) {

                    sendMessage(
                            chatId,
                            """
                            ❌ Invalid Format

                            Example:

                            Nandvi baudhawadi pump basvine 151000
                            """
                    );

                    return;
                }

                String amount =
                        parts[
                                parts.length - 1
                                ];

                String workName =
                        userMessage.substring(
                                0,
                                userMessage.lastIndexOf(
                                        amount
                                )
                        ).trim();

                Long amountValue =
                        Long.parseLong(
                                amount
                        );

                WorkEntryEntity entity =
                        new WorkEntryEntity(
                                workName,
                                amountValue
                        );

                workEntryRepository.save(
                        entity
                );

                System.out.println(
                        "Saved In DB : "
                                + entity.getId()
                );

                String response =
                        """
                        ✅ Work Saved

                        Work:
                        %s

                        Amount:
                        ₹%,d
                        """.formatted(
                                workName,
                                amountValue
                        );

                sendMessage(
                        chatId,
                        response
                );

                System.out.println(
                        "Work Saved Successfully"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            try {

                sendMessage(
                        update.getMessage()
                                .getChatId(),
                        "❌ Something went wrong"
                );

            } catch (Exception ignored) {
            }
        }
    }

    private void sendMessage(
            Long chatId,
            String text
    ) throws Exception {

        SendMessage message =
                new SendMessage();

        message.setChatId(
                chatId.toString()
        );

        message.setText(text);

        execute(message);
    }

    private void sendExcel(
            Long chatId,
            String fileName
    ) throws Exception {

        SendDocument document =
                new SendDocument();

        document.setChatId(
                chatId.toString()
        );

        document.setDocument(
                new InputFile(
                        new File(fileName)
                )
        );

        execute(document);
    }
}