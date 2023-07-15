package com.mong.project.repository.image;

import com.mong.project.domain.image.Image;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class JPQLImageRepositoryImpl implements JPQLImageRepository {

    private final EntityManager em;

    @Override
    public List<Image> findAllById(List<Long> ids) {
        List<Image> images = new ArrayList<>();
        ids.forEach(id -> {
            Image image = em.find(Image.class, id);
            images.add(image);
        });
        return images;
    }
}
