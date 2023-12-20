package itpark.lesson.handlers;

import itpark.lesson.model.entity.Advertisement;
import itpark.lesson.model.entity.WeatherEntity;
import itpark.lesson.parsers.CianParser;
import itpark.lesson.parsers.WeatherParser;
import itpark.lesson.service.AdvertisementService;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 1ommy
 * @version 17.12.2023
 */
public class TelegramRequestHandler extends TelegramLongPollingBot {
    private final CianParser cianParser = new CianParser();
    private final AdvertisementService advertisementService = new AdvertisementService();
    private Map<String, List<String>> historyOfMessages = new HashMap<>();

    List<BotCommand> LIST_OF_COMMAND = List.of(
            new BotCommand("/start", "запуск бота"),
            new BotCommand("/get", "Получить список объявлений"),
            new BotCommand("/history", "Получить историю сообщений")
    );
    public void init() throws TelegramApiException {
        this.execute(new SetMyCommands(LIST_OF_COMMAND, new BotCommandScopeDefault(), null));
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            var message = update.getMessage();

            if (message.hasText()) {
                String text = message.getText();
                String chatId = message.getChatId().toString();

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

                List<InlineKeyboardButton> rowInline1 = new ArrayList<>();

                InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
                inlineKeyboardButton1.setText("Получить список объявлений");
                inlineKeyboardButton1.setCallbackData("/get");

                InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
                inlineKeyboardButton2.setText("Получить историю сообщений");
                inlineKeyboardButton2.setCallbackData("/history");
                rowInline1.add(inlineKeyboardButton1);
                rowInline1.add(inlineKeyboardButton2);

                markupInline.setKeyboard(List.of(rowInline1));

                // cmd: /parse <PAGE> <COUNT_ADVERTISEMENETS>

                if (text.startsWith("/start")) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    sendMessage.setReplyMarkup(markupInline);
                    sendMessage.setText("Выберите действие");

                    execute(sendMessage);
                }

                if (text.startsWith("/parse")) {
                    historyOfMessages.get(chatId).add(text);
                    String[] args = text.split(" ");
                    Integer page = Integer.parseInt(args[1]);
                    Integer countAdvertisements = Integer.valueOf(args[2]);

                    List<Advertisement> advertisements = cianParser.parse(page, countAdvertisements);
                    advertisementService.saveAll(advertisements);
                } else {
                    List<String> list = historyOfMessages.get(chatId);

                    if (list == null) {
                        historyOfMessages.put(chatId, new ArrayList<>());
                        list = historyOfMessages.get(chatId);
                    }
                    list.add(text);
                    if (text.startsWith("/get")) {
                        List<Advertisement> all = advertisementService.getAll();

                        for (Advertisement advertisement : all) {
                            sendMessage(advertisement.getFormattedAdvertisementInfo(), chatId);
                        }
                    } else if (text.startsWith("/weather")) {

                        String[] args = text.split(" ");
                        String city = args[1];

                        WeatherEntity weatherEntity = new WeatherParser().getWeatherInTheCity(city);
                        sendMessage(weatherEntity.toString(), chatId);
                    } else if (text.startsWith("/history")) {
                        String history = String.join("\n", list);
                        sendMessage(history, chatId);
                    }
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
