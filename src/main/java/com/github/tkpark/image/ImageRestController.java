package com.github.tkpark.image;

import com.github.tkpark.errors.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("images")
public class ImageRestController {

    private final ImageService imageService;

    @Value("${images.path}")
    private String IMAGES_PATH;

    public ImageRestController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(path = "{code}")
    public ResponseEntity<Resource> findById(@PathVariable String code) {
        log.info("========= images/{} =========", code);

        RoadMaster road = imageService.findByRoad(code)
                .orElseThrow(() -> new NotFoundException("Could not found road for " + code));

        String imgFileName = road.getImageFileName();
        FileSystemResource imgFile = new FileSystemResource(IMAGES_PATH + imgFileName);

        if (imgFile.exists()) {
            HttpHeaders headers = new HttpHeaders();

            if (imgFileName.endsWith(".jpg")) {
                headers.setContentType(MediaType.IMAGE_JPEG);
            } else if (imgFileName.endsWith(".png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            }

            headers.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS));
            return new ResponseEntity<>(imgFile, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}