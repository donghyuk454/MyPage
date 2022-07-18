package com.mong.MyProject.service.board;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.domain.image.Image;
import com.mong.MyProject.domain.image.ImageType;
import com.mong.MyProject.domain.member.Member;
import com.mong.MyProject.repository.board.BoardRepository;
import com.mong.MyProject.repository.image.ImageRepository;
import com.mong.MyProject.repository.member.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class BoardService {

    private BoardRepository boardRepository;
    private MemberRepository memberRepository;
    private ImageRepository imageRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository, ImageRepository imageRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.imageRepository = imageRepository;
    }

    /**
     * 새로운 board 생성
     * */
    public Board addBoard(Long member_id, Board board){
        Member member = memberRepository.findById(member_id).get();
        return boardRepository.save(member, board);
    }

    /**
     * board 내용(title, content) 수정
     * */
    public Board changeBoard(Long board_id, String title, String content) {
        Board board = boardRepository.findById(board_id).get();
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
        Board board = boardRepository.findById(board_id).get();
        // TODO: url 생성, image 업로드 생성
        images.forEach(img ->{
            String url = "";
            Image image = Image.builder()
                    .type(ImageType.BOARD)
                    .url(url)
                    .build();

            board.addImage(image);
        });

        boardRepository.save(board);
    }

    /**
     * board 이미지 삭제
     * */
    public void deleteImages(Long board_id, List<Long> image_ids) {
        Board board = boardRepository.findById(board_id).get();
        List<Image> images = imageRepository.findAllById(image_ids);
        List<Image> board_images = board.getImages();
        images.forEach(board_images::remove);

        boardRepository.save(board);
    }
}
