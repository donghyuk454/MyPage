package com.mong.project.service.board;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.image.Image;
import com.mong.project.domain.image.ImageType;
import com.mong.project.domain.member.Member;
import com.mong.project.exception.ErrorCode;
import com.mong.project.repository.board.BoardRepository;
import com.mong.project.repository.image.ImageRepository;
import com.mong.project.repository.member.MemberRepository;
import com.mong.project.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final FileService fileService;

    @Autowired
    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository, ImageRepository imageRepository, FileService fileService) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.imageRepository = imageRepository;
        this.fileService = fileService;
    }

    /**
     * 새로운 board 생성
     * */
    public Board addBoard(Long memberId, Board board){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> {
                    throw new NoSuchElementException(ErrorCode.NOT_EXIST_MEMBER);
                });
        return boardRepository.save(member, board);
    }

    /**
     * member 소유의 board 조회
     * */
    public List<Board> getBoardsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> {
                    throw new NoSuchElementException(ErrorCode.NOT_EXIST_MEMBER);
                });

        return member.getBoards();
    }

    /**
     * board id 를 통해 board 조회
     * */
    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> {
            throw new NoSuchElementException(ErrorCode.NOT_EXIST_BOARD);
        });
    }

    /**
     * board 내용(title, content) 수정
     * */
    public Board changeBoard(Long boardId, String title, String content) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            throw new NoSuchElementException(ErrorCode.NOT_EXIST_BOARD);
        });
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
        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            throw new NoSuchElementException(ErrorCode.NOT_EXIST_BOARD);
        });
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
        Board board = boardRepository.findById(boardId).orElseThrow(() -> {
            throw new NoSuchElementException(ErrorCode.NOT_EXIST_BOARD);
        });

        List<Image> images = imageRepository.findAllById(imageIds);
        List<Image> boardImages = board.getImages();
        log.info("삭제할 이미지 = {}", boardImages);
        images.forEach(image -> {
            if (fileService.removeFileByPath(image.getUrl())) {
                boardImages.remove(image);
                log.info("삭제된 이미지 = {}", image);
            } else
                throw new NoSuchElementException(ErrorCode.FAIL_TO_REMOVE_FILE);
        });

        boardRepository.save(board);
    }
}
