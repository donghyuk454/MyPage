package com.mong.project.repository.image;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.image.Image;
import com.mong.project.domain.image.ImageType;
import com.mong.project.domain.member.Member;
import com.mong.project.repository.board.BoardRepository;
import com.mong.project.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@ActiveProfiles("dev")
@TestPropertySource("classpath:test.properties")
class ImageRepositoryImplTest {

    @Autowired private ImageRepository imageRepository;
    @Autowired private BoardRepository boardRepository;
    @Autowired private MemberRepository memberRepository;

    @Value("${spring.image.directory}") String imageDirectory;

    @Test
    @DisplayName("board 의 이미지를 id 를 통해 검색합니다.")
    void findAllById_board() {
        //given
        Board board = createDefaultBoard();
        for (int i = 0; i < 5; i++) {
            Image image = createImage(i, ImageType.BOARD);
            board.addImage(image);
        }
        boardRepository.save(board);

        List<Long> image_ids = new ArrayList<>();
        List<Image> images = board.getImages();

        for (int i = 0; i < 5; i += 2){
            image_ids.add(images.get(i).getId());
        }

        //when
        List<Image> result = imageRepository.findAllById(image_ids);

        //then
        assertThat(result).hasSize(3);
        result.forEach(image->
            assertThat(images).contains(image)
        );
    }

    @Test
    @DisplayName("member 의 이미지를 id 를 통해 검색합니다.")
    void findAllById_member() {
        //given
        Member member = createDefaultMember();
        member.setImage(createImage(1, ImageType.MEMBER));
        memberRepository.save(member);

        Image image = member.getImage();

        //when
        Image result = imageRepository.findById(image.getId())
                .orElse(null);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getUrl()).isEqualTo(member.getImage().getUrl());
        assertThat(result.getType()).isEqualTo(ImageType.MEMBER);
    }

    private static Member createDefaultMember() {
        return Member.builder()
                .name("test")
                .email("test@test.com")
                .alias("test")
                .passwd("test")
                .build();
    }

    private static Board createDefaultBoard() {
        return Board.builder()
                .content("content")
                .title("title")
                .build();
    }

    private Image createImage(int index, ImageType type) {
        return Image.builder()
                .url(imageDirectory + "/" + index + ".PNG")
                .type(type)
                .build();
    }
}