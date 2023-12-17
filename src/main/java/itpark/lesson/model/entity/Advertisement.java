package itpark.lesson.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 1ommy
 * @version 17.12.2023
 */

@AllArgsConstructor
@Builder
public record Advertisement(
        Long id,
        String title,
        String description,
        String metroStation,
        String uri,
        String region,
        String price
) {

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s, %s",  title, description, metroStation, uri, region, price);
    }
}
