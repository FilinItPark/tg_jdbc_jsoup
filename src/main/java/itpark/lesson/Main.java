package itpark.lesson;

import itpark.lesson.handlers.TelegramRequestHandler;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * @author 1ommy
 * @version 17.12.2023
 */
public class Main {

    /*
        он вводит колво страниц и колво объявлений,которые он хочет найти
     */
    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramRequestHandler bot = new TelegramRequestHandler();
            telegramBotsApi.registerBot(bot);
            bot.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}