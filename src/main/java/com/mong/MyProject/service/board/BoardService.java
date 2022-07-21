package com.mong.MyProject.service.board;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.image.Image;
import com.mong.MyProject.domain.image.ImageType;
import com.mong.MyProject.domain.member.Member;
import com.mong.MyProject.repository.board.BoardRepository;
import com.mong.MyProject.repository.image.ImageRepository;
import com.mong.MyProject.repository.member.MemberRepository;
import com.mong.MyProject.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
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
    public Board addBoard(Long member_id, Board board){
        Member member = memberRepository.findById(member_id)
                .orElseThrow(()-> {
                    throw new NoSuchElementException("없는 회원의 아이디 입니다.");
                });
        return boardRepository.save(member, board);
    }

    /**
     * member 소유의 board 조회
     * */
    public List<Board> getBoardsByMemberId(Long member_id) {
        Member member = memberRepository.findById(member_id)
                .orElseThrow(()-> {
                    throw new NoSuchElementException("없는 회원의 아이디 입니다.");
                });

        return member.getBoards();
    }

    /**
     * board id 를 통해 board 조회
     * */
    public Board getBoardById(Long board_id) {
        return boardRepository.findById(board_id).orElseThrow(() -> {
            throw new NoSuchElementException("없는 게시물의 아이디 입니다.");
        });
    }

    /**
     * board 내용(title, content) 수정
     * */
    public Board changeBoard(Long board_id, String title, String content) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> {
            throw new NoSuchElementException("없는 게시물의 아이디 입니다.");
        });
        board.setTitle(title);
        board.setContent(content);

        return boardRepository.save(board);
    }

    /**
     * board 삭제
     * */
    public void deleteBoard(Long board_id) {
        boardRepository.deleteBoardById(board_id);
    }

    /**
     * board 이미지 추가
     * */
    public void addImage(Long board_id, List<MultipartFile> images) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> {
            throw new NoSuchElementException("없는 게시물의 아이디 입니다.");
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
    public void deleteImages(Long board_id, List<Long> image_ids) {
        Board board = boardRepository.findById(board_id).orElseThrow(() -> {
            throw new NoSuchElementException("없는 게시물의 아이디 입니다.");
        });
        List<Image> images = imageRepository.findAllById(image_ids);
        List<Image> board_images = board.getImages();
        log.info("삭제할 이미지 = {}", board_images);
        images.forEach(image -> {
            fileService.removeFileByPath(image.getUrl());
            board_images.remove(image);
            log.info("삭제된 이미지 = {}", image);
        });

        boardRepository.save(board);
    }
}
