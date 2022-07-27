package com.mong.MyProject.repository.image;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.image.Image;
import com.mong.MyProject.domain.image.ImageType;
import com.mong.MyProject.repository.board.BoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@TestPropertySource("classpath:test.properties")
class ImageRepositoryImplTest {

    @Autowired private ImageRepository imageRepository;
    @Autowired private BoardRepository boardRepository;

    @Value("${spring.image.directory}") String imageDirectory;

    @Test
    @DisplayName("board 의 이미지를 id 를 통해 검색합니다.")
    void findAllById() {
        //given
        Board board = Board.builder().content("content").title("title").build();
        for (int i = 0; i < 5; i++) {
            Image image = Image.builder()
                    .url(imageDirectory+"/"+i+".PNG")
                    .type(ImageType.BOARD)
                    .build();
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
        assertThat(result.size()).isEqualTo(3);
        result.forEach(image->
            assertThat(images.contains(image)).isTrue()
        );
    }
}