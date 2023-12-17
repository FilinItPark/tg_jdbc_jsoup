package itpark.lesson.handlers;

import itpark.lesson.model.entity.Advertisement;
import itpark.lesson.parsers.CianParser;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

/**
 * @author 1ommy
 * @version 17.12.2023
 */
public class TelegramRequestHandler extends TelegramLongPollingBot {
    private final CianParser cianParser = new CianParser();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            var message = update.getMessage();

            if (message.hasText()) {
                String text = message.getText();
                String chatId = message.getChatId().toString();

                // cmd: /parse <PAGE> <COUNT_ADVERTISEMENETS>
                if (text.startsWith("/parse")) {
                    String[] args = text.split(" ");
                    Integer page = Integer.parseInt(args[0]);
                    Integer countAdvertisements = Integer.valueOf(args[1]);

                   List<Advertisement> advertisements = cianParser.parse(page, countAdvertisements);
                }
            }
        }
    }

    private void sendMessage(String text, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("чет пошло не так при отправке сообщения");
        }
    }

    @Override
    public String getBotUsername() {
        return "jsoupjdbcbot";
    }

    @Override
    public String getBotToken() {
        return "6264596503:AAERj0RR-sOyaWxOETnR970Qs7o7Wdv-geQ";
    }
}
