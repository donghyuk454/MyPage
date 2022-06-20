package com.mong.MyProject.domain.image.article;

import com.mong.MyProject.domain.article.Article;
import com.mong.MyProject.domain.image.Image;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "article_image", uniqueConstraints = {
        @UniqueConstraint(name = "article_image_unique_constraints", columnNames = {"image_url", "image_key"})
})
public class ArticleImage extends Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id",nullable = false)
    private Article article;
}
