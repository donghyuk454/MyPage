package com.mong.project.repository.image;

import com.mong.project.domain.image.Image;

import java.util.List;

public interface ImageRepository {
    List<Image> findAllById(List<Long> ids);


}
