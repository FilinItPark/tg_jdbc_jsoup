package itpark.lesson.parsers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import itpark.lesson.model.entity.WeatherEntity;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author 1ommy
 * @version 20.12.2023
 */
public class WeatherParser {


    public WeatherEntity getWeatherInTheCity(String city) throws IOException, InterruptedException {
        final String completelyUri = "https://api.openweathermap.org/data/2" +
                ".5/weather?&appid=58cff0dc4fdf535e33bea4d8dcb1fce4&q=" + city + "&units=metric&lang=ru";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(completelyUri))
                .GET()
                .build();

        HttpResponse<String> sendedRequest = null;
            sendedRequest = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        String response = (String) sendedRequest.body();

        return parseJson(response);
    }

    private WeatherEntity parseJson(String response) {
        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

        WeatherEntity weatherEntity = WeatherEntity.builder()
                .baseDescription(
                        jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString()
                )
                .city(jsonObject.get("name").getAsString())
                .temperature(jsonObject.get("main").getAsJsonObject().get("temp").getAsString())
                .temperateFeelsLike(jsonObject.get("main").getAsJsonObject().get("feels_like").getAsString())
                .tempMax(jsonObject.get("main").getAsJsonObject().get("temp_max").getAsString())
                .tempMin(jsonObject.get("main").getAsJsonObject().get("temp_min").getAsString())
                .build();
        return weatherEntity;
    }
}
