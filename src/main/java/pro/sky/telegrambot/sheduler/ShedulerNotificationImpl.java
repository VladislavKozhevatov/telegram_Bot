package pro.sky.telegrambot.sheduler;

import com.pengrad.telegrambot.TelegramBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.Service.MessageSender;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ShedulerNotificationImpl implements ShedulerNotification {

    private final MessageSender messageSender;

    private final NotificationTaskRepository repository;

    public ShedulerNotificationImpl(MessageSender messageSender, NotificationTaskRepository repository) {
        this.messageSender = messageSender;
        this.repository = repository;
    }

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Override
    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotifications() {
        logger.info("scheduler started");
     List<NotificationTask> tasks = repository.findNowNotification(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
       //List<NotificationTask> tasks = repository.findAll();
        for (NotificationTask task : tasks){
          messageSender.send(task.getChatId(), task.getMessage());
      }
    }
}
