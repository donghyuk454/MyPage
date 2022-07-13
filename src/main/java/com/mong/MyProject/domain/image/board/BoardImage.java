package com.mong.MyProject.domain.image.board;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.image.Image;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "board_image")
@Getter
@Setter
public class BoardImage extends Image {
    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false, updatable = false)
    private Board board;
}
