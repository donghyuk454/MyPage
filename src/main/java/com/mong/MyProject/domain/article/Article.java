package com.mong.MyProject.domain.article;

import com.mong.MyProject.domain.BaseEntity;
import com.mong.MyProject.domain.image.Image;
import com.mong.MyProject.domain.image.article.ArticleImage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity(name = "article")
@Getter
@Setter
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "article_id", updatable = false)
    private Long id;

    private String title;
    private String content;

    private boolean deleted;

    @OneToMany//(mappedBy = "image_id")
    private List<ArticleImage> images = new ArrayList<>();

}
