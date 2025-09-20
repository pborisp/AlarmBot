package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Chats;
import pro.sky.telegrambot.model.Messages;
import pro.sky.telegrambot.repository.ChatsRepository;
import pro.sky.telegrambot.repository.MessagesRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private MessagesRepository messagesRepository;
    @Autowired
    private ChatsRepository chatsRepository;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            String str = update.message().text();

            if (str.equals("/start")) {
                SendMessage sendMessage = new SendMessage(update.message().chat().id(), "Привет " + update.message().chat().firstName() + "!");
                telegramBot.execute(sendMessage);
                SendMessage sendMessage2 = new SendMessage(update.message().chat().id(), "Напишите дату, время и текст напоминания в формате: \n 01.01.2022 20:00 Сделать домашнюю работу");
                telegramBot.execute(sendMessage2);
            } else {
                Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");
                Matcher matcher = pattern.matcher(str);

                Chats chats = new Chats();
                chats.setId(update.message().chat().id());
                chats.setUserName(update.message().chat().username());
                chats.setFirstName(update.message().chat().firstName());
                chatsRepository.save(chats);

                Messages messages = new Messages();
                if (matcher.matches()) {
                    LocalDateTime localDateTime = LocalDateTime.parse(matcher.group(1), DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                    String text = matcher.group(3);
                    messages.setDateTime(localDateTime);
                    messages.setText(text);
                    messages.setChats(chats);
                    messagesRepository.save(messages);
                    SendMessage sendMessage = new SendMessage(chats.getId(), "Напоминание установлено!");
                    telegramBot.execute(sendMessage);
                } else {
                    SendMessage sendMessage = new SendMessage(chats.getId(), "Не корректные данные, введите еще раз, солгасно формату: \\n 01.01.2022 20:00 Сделать домашнюю работу\"");
                    telegramBot.execute(sendMessage);
                }
            }
        });

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNowMessages() {
        List<Messages> nowMessages = new ArrayList<>();
        nowMessages = messagesRepository.findAll().stream()
                .filter(localDateTime -> localDateTime.getDateTime().equals(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)))
                .collect(Collectors.toList());
        if (!nowMessages.isEmpty() && nowMessages != null) {
            nowMessages.forEach(messages -> {
                SendMessage sendMessage = new SendMessage(messages.getChats().getId(), "Напоминание!\n" + messages.getText() + "   !!!");
                telegramBot.execute(sendMessage);
            });
        }
    }
}
