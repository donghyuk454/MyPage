package com.mong.project.repository.image;

import com.mong.project.domain.image.Image;

import java.util.List;

public interface JPQLImageRepository {
    List<Image> findAllById(List<Long> ids);


}
