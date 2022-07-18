package com.mong.MyProject.domain.image;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "image")
@Table(name = "image")
@Getter
@NoArgsConstructor
public class Image {

    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_url", nullable = false, unique = true)
    protected String url;
}
