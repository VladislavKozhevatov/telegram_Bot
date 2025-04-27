package pro.sky.telegrambot.Service;

import org.springframework.stereotype.Service;

@Service
public interface MessageSender {
    void send(Long chatId, String messageText);
}
