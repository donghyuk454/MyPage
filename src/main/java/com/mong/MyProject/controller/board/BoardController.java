package com.mong.MyProject.controller.board;

import com.mong.MyProject.domain.board.Board;
import com.mong.MyProject.dto.request.board.ChangeBoardRequest;
import com.mong.MyProject.dto.request.board.CreateBoardRequest;
import com.mong.MyProject.dto.request.board.RemoveBoardImageRequest;
import com.mong.MyProject.dto.response.board.GetBoardResponse;
import com.mong.MyProject.service.board.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/board/{board_id}")
    public ResponseEntity<GetBoardResponse> getBoard(@PathVariable(name = "board_id") Long board_id){
        Board board = boardService.getBoardById(board_id);
        GetBoardResponse response = new GetBoardResponse(board);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/member/board")
    public ResponseEntity<List<GetBoardResponse>> getMemberBoards(@RequestParam(name = "member_id") Long member_id) {
        List<Board> boards = boardService.getBoardsByMemberId(member_id);
        List<GetBoardResponse> responses = new ArrayList<>();
        boards.forEach(board -> {
            GetBoardResponse response = new GetBoardResponse(board);
            responses.add(response);
        });

        return ResponseEntity.ok().body(responses);
    }

    @PostMapping("/board")
    public ResponseEntity<Void> createBoard(@RequestParam(name = "member_id") Long member_id, @RequestParam(name = "image") List<MultipartFile> images, @RequestBody CreateBoardRequest createBoardRequest) {
        Board board = boardService.addBoard(member_id, createBoardRequest.toBoard());
        boardService.addImage(board.getId(), images);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/board")
    public ResponseEntity<Void> changeBoard(@RequestBody ChangeBoardRequest changeBoardRequest) {
        boardService.changeBoard(changeBoardRequest.getBoard_id(), changeBoardRequest.getTitle(), changeBoardRequest.getContent());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/board")
    public ResponseEntity<Void> deleteBoard(@RequestParam(name = "board_id") Long board_id) {
        boardService.deleteBoard(board_id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/board/image")
    public ResponseEntity<Void> createBoardImage(@RequestParam(name = "board_id") Long board_id, @RequestPart List<MultipartFile> imageFiles) {
        boardService.addImage(board_id, imageFiles);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/board/image")
    public ResponseEntity<Void> removeBoardImage(@RequestBody RemoveBoardImageRequest removeBoardImageRequest) {
        boardService.deleteImages(removeBoardImageRequest.getBoard_id(), removeBoardImageRequest.getImage_ids());

        return ResponseEntity.ok().build();
    }
}
