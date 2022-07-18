package com.mong.MyProject.repository.image;

import com.mong.MyProject.domain.image.Image;

import java.util.List;

public interface ImageRepository {
    List<Image> findAllById(List<Long> ids);


}
