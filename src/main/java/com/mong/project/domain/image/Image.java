package com.mong.project.domain.image;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "images")
@Table(name = "images", uniqueConstraints = {
        @UniqueConstraint(name = "image_unique_constraint", columnNames = {"image_url", "image_key"})
})
@Getter
@NoArgsConstructor
public class Image {

    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_url", nullable = false, unique = true)
    private String url;

    @Column(name = "image_key", nullable = false, unique = true)
    private String key;

    @Column(name = "image_type")
    @Enumerated(value = EnumType.STRING)
    private ImageType type;

    @Builder
    public Image(Long id, String url, ImageType type) {
        this.id = id;
        this.url = url;
        this.type = type;

        this.key = extractKey();
    }

    private String extractKey(){
        return url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
    }
}
