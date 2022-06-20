package com.mong.MyProject.domain.image;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "image", uniqueConstraints = {
        @UniqueConstraint(name = "image_unique_constraints", columnNames = {"image_url", "image_key"})
})
public abstract class Image {

    @Column(name = "image_url", nullable = false, unique = true)
    private String url;

    @Column(name = "image_key", nullable = false, unique = true)
    private String key;

}
