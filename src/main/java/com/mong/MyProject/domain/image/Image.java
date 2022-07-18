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
    private String url;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private ImageType type;

    @Builder
    public Image(Long id, String url, ImageType type, Board board, Member member) {
        this.id = id;
        this.url = url;
        this.type = type;
        if (type.equals(ImageType.BOARD)) {
            board.addImage(this);
        } else if (type.equals(ImageType.MEMBER)) {
            member.setImage(this);
        }

        this.key = extractKey();
    }

    private String extractKey(){
        return "";
    }
}
