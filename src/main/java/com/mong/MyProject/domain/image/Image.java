package com.mong.MyProject.domain.image;

import lombok.Getter;

import javax.persistence.*;

@Getter
@MappedSuperclass
public abstract class Image {

    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_url", nullable = false, unique = true)
    private String url;
}
