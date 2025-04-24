package pro.sky.telegrambot.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;

@Service
public class MessageSenderImpl implements MessageSender {
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot bot;

    public MessageSenderImpl(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void send(Long chatId, String messageText){
        SendMessage sendMessage = new SendMessage(chatId, messageText);
        SendResponse response = bot.execute(sendMessage);
    }


}
