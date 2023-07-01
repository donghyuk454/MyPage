package com.mong.project.service.board;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.image.Image;
import com.mong.project.domain.image.ImageType;
import com.mong.project.domain.member.Member;
import com.mong.project.repository.board.BoardRepository;
import com.mong.project.repository.image.ImageRepository;
import com.mong.project.repository.member.MemberRepository;
import com.mong.project.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

import static com.mong.project.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final FileService fileService;

    /**
     * 새로운 board 생성
     * */
    public Board addBoard(Long memberId, Board board){
        Member member = getMemberByMemberId(memberId);
        Board board1 = boardRepository.save(board);
        member.addBoard(board);

        return board1;
    }

    /**
     * member 소유의 board 조회
     * */
    public List<Board> getBoardsByMemberId(Long memberId) {
        return getMemberByMemberId(memberId)
                .getBoards();
    }

    private Member getMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(()-> new NoSuchElementException(NOT_EXIST_MEMBER));
    }

    /**
     * board id 를 통해 board 조회
     * */
    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException(NOT_EXIST_BOARD));
    }

    /**
     * board 내용(title, content) 수정
     * */
    public Board changeBoard(Long boardId, String title, String content) {
        Board board = getBoardById(boardId);

        board.setTitle(title);
        board.setContent(content);

        return boardRepository.save(board);
    }

    /**
     * board 삭제
     * */
    public void deleteBoard(Long boardId) {
        boardRepository.deleteBoardById(boardId);
    }

    /**
     * board 이미지 추가
     * */
    public void addImage(Long boardId, List<MultipartFile> images) {
        Board board = getBoardById(boardId);

        images.forEach(img ->{
            File imageFile = fileService.convertToFile(img);

            Image image = Image.builder()
                    .type(ImageType.BOARD)
                    .url(imageFile.getAbsolutePath())
                    .build();

            board.addImage(image);
        });

        boardRepository.save(board);
    }

    /**
     * board 이미지 삭제
     * */
    public void deleteImages(Long boardId, List<Long> imageIds) {
        Board board = getBoardById(boardId);

        List<Image> images = imageRepository.findAllById(imageIds);
        List<Image> boardImages = board.getImages();
        log.info("삭제할 이미지 = {}", boardImages);
        images.stream()
                .filter(image -> fileService.removeFileByPath(image.getUrl()))
                .forEach(boardImages::remove);

        boardRepository.save(board);
    }
}
