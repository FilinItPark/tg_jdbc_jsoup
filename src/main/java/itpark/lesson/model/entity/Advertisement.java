package itpark.lesson.model.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author 1ommy
 * @version 17.12.2023
 */

@Data
@Builder
public record Advertisement(
        String title,
        String description,
        String metroStation,
        String uri,
        String region,
        String price
) {
}
