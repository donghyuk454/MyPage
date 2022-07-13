package com.mong.MyProject.domain.image;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "image")
public abstract class Image {

    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_url", nullable = false, unique = true)
    private String url;
}
