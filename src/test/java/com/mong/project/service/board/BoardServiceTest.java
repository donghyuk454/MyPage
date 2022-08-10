package com.mong.project.service.board;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.image.Image;
import com.mong.project.domain.member.Member;
import com.mong.project.repository.board.BoardRepository;
import com.mong.project.repository.image.ImageRepository;
import com.mong.project.repository.member.MemberRepository;
import com.mong.project.service.FileService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @InjectMocks
    private BoardService boardService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private FileService fileService;

    @Mock
    private Member member;
    @Mock
    private Board board;

    @Test
    @DisplayName("member 의 새로운 board 를 추가합니다.")
    void addBoard() {
        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));
        when(boardRepository.save(member, board))
                .thenReturn(board);

        boardService.addBoard(1L, board);

        verify(memberRepository, times(1))
                .findById(1L);
        verify(boardRepository, times(1))
                .save(member, board);
    }

    @Test
    @DisplayName("member 의 id 를 통해 member 의 모든 board 를 조회합니다.")
    void getBoardsByMemberId() {
        when(member.getBoards())
                .thenReturn(List.of(board));
        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));

        List<Board> result = boardService.getBoardsByMemberId(1L);

        verify(memberRepository, times(1))
                .findById(1L);
    }

    @Test
    @DisplayName("member 의 id 를 통해 member 의 모든 board 를 조회합니다.")
    void getBoardById() {
        when(boardRepository.findById(1L))
                .thenReturn(Optional.of(board));

        Board result = boardService.getBoardById(1L);

        verify(boardRepository, times(1))
                .findById(1L);
    }

    @Test
    @DisplayName("board 의 내용(title, content)를 수정합니다.")
    void changeBoard() {
        when(boardRepository.save(board))
                .thenReturn(board);
        when(boardRepository.findById(1L))
                .thenReturn(Optional.of(board));

        Board result = boardService.changeBoard(1L, "title", "content");

        verify(boardRepository, times(1))
                .save(board);
    }

    @Test
    @DisplayName("board 를 삭제합니다.")
    void deleteBoard() {
        doNothing().when(boardRepository).deleteBoardById(1L);

        boardService.deleteBoard(1L);

        verify(boardRepository, times(1))
                .deleteBoardById(1L);
    }

    @Test
    @DisplayName("board 에 image 를 추가합니다.")
    void addBoardImage(){
        when(boardRepository.save(board))
                .thenReturn(board);
        when(boardRepository.findById(1L))
                .thenReturn(Optional.of(board));
        List<MultipartFile> images = List.of(
                new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes()),
                new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "test2".getBytes())
        );
        File imageFile= mock(File.class);
        when(fileService.convertToFile(any()))
                .thenReturn(imageFile);
        when(imageFile.getAbsolutePath())
                .thenReturn("/this/is/absolute/path.PNG");

        boardService.addImage(1L, images);

        verify(board,times(2))
                .addImage(any());
    }

    @Test
    @DisplayName("board 에 image 를 삭제합니다.")
    void deleteBoardImage(){
        List<Image> images = mock(List.class);
        List<Long> image_ids = new ArrayList<>();
        List<Image> foundedImages = new ArrayList<>();

        for(long i = 1L; i < 5L; i++) {
            Image temp = mock(Image.class);
            if (i < 3){
                image_ids.add(i);
                foundedImages.add(temp);
                when(images.remove(temp))
                        .thenReturn(true);
            }
        }

        when(boardRepository.findById(1L))
                .thenReturn(Optional.of(board));
        when(board.getImages())
                .thenReturn(images);
        when(imageRepository.findAllById(image_ids))
                .thenReturn(foundedImages);
        when(boardRepository.save(board))
                .thenReturn(board);
        when(fileService.removeFileByPath(any()))
                .thenReturn(true);

        boardService.deleteImages(1L, image_ids);

        verify(images, times(2))
                .remove(any(Image.class));
    }
}