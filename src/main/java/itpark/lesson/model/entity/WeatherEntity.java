package itpark.lesson.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author 1ommy
 * @version 20.12.2023
 */

@Builder
@Getter
@ToString
public class WeatherEntity {
    String city;
    String baseDescription;
    String temperature;
    String temperateFeelsLike;
    String tempMax;
    String tempMin;

}
