package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.Service.MessageSender;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    private final NotificationTaskRepository notificationTaskRepository;

    @Autowired
    private MessageSender messageSender;

    private static final String START_MESSAGE = "Привет, я готов к работе!";
    private static final Pattern NOTIFICATION_PATTERN = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");

    public TelegramBotUpdatesListener(NotificationTaskRepository notificationTaskRepository, TelegramBot telegramBot, MessageSender messageSender) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBot = telegramBot;
        this.messageSender = messageSender;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            Long chatId = update.message().chat().id();
            String message = update.message().text();

            if(message.equals("/start")){
                messageSender.send(chatId, START_MESSAGE);
            }
            Matcher matcher = NOTIFICATION_PATTERN.matcher(message);

             if (matcher.matches()){
                 String date = matcher.group(1);
                 String notification_message = matcher.group(3);
                 LocalDateTime notification_date = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                 notificationTaskRepository.save(new NotificationTask(chatId,notification_message,notification_date));
                 messageSender.send(chatId,"Notification succesfully saved to DB");
             }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
