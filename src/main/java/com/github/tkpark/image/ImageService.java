package com.github.tkpark.image;

import com.github.tkpark.wind.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class ImageService {

    @Autowired
    CommonService commonService;

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Transactional(readOnly = true)
    public Optional<RoadMaster> findByRoad(String road) {
        return imageRepository.findByRoad(road);
    }

}
