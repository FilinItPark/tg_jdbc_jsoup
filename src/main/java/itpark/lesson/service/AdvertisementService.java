package itpark.lesson.service;

import itpark.lesson.model.DBManager;
import itpark.lesson.model.entity.Advertisement;
import itpark.lesson.model.repository.AdvertisementRepository;

import java.util.List;

/**
 * @author 1ommy
 * @version 17.12.2023
 */
public class AdvertisementService {
    private final AdvertisementRepository advertisementRepository;

    public AdvertisementService() {
        this.advertisementRepository = DBManager.getInstance().getAdvertisementRepository();
    }

    public void saveAll(List<Advertisement> ads) {
        advertisementRepository.saveAll(ads);
    }

    public List<Advertisement> getAll() {
        return advertisementRepository.getAll();
    }
}
