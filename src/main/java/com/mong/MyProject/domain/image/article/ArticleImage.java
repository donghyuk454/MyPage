package com.mong.MyProject.domain.image.article;

import com.mong.MyProject.domain.article.Article;
import com.mong.MyProject.domain.image.Image;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "article_image")
@Getter
@Setter
public class ArticleImage extends Image {
    @ManyToOne
    @JoinColumn(name = "article_id",nullable = false)
    private Article article;
}
