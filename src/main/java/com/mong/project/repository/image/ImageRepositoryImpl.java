package com.mong.project.repository.image;

import com.mong.project.domain.image.Image;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ImageRepositoryImpl implements ImageRepository{

    private EntityManager em;

    public ImageRepositoryImpl(EntityManager em){
        this.em = em;
    }

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
