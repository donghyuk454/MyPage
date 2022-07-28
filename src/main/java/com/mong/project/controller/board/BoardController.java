package com.mong.project.controller.board;

import com.mong.project.domain.board.Board;
import com.mong.project.dto.request.board.ChangeBoardRequest;
import com.mong.project.dto.request.board.CreateBoardRequest;
import com.mong.project.dto.request.board.RemoveBoardImageRequest;
import com.mong.project.dto.response.board.GetBoardResponse;
import com.mong.project.service.board.BoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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
    public ResponseEntity<GetBoardResponse> getBoard(@PathVariable(name = "board_id") Long boardId){
        Board board = boardService.getBoardById(boardId);
        GetBoardResponse response = new GetBoardResponse(board);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/member/board")
    public ResponseEntity<List<GetBoardResponse>> getMemberBoards(@RequestParam(name = "member_id") Long memberId) {
        List<Board> boards = boardService.getBoardsByMemberId(memberId);
        List<GetBoardResponse> responses = new ArrayList<>();
        boards.forEach(board -> {
            GetBoardResponse response = new GetBoardResponse(board);
            responses.add(response);
        });
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping("/board")
    public ResponseEntity<Void> createBoard(@RequestParam(name = "member_id") Long memberId, @Nullable @RequestParam(name = "image") List<MultipartFile> images, @RequestBody CreateBoardRequest createBoardRequest) {
        Board board = boardService.addBoard(memberId, createBoardRequest.toBoard());
        if (images != null && images.size() > 0)
            boardService.addImage(board.getId(), images);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/board")
    public ResponseEntity<Void> changeBoard(@RequestBody ChangeBoardRequest changeBoardRequest) {
        boardService.changeBoard(changeBoardRequest.getBoardId(), changeBoardRequest.getTitle(), changeBoardRequest.getContent());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/board")
    public ResponseEntity<Void> deleteBoard(@RequestParam(name = "board_id") Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/board/image")
    public ResponseEntity<Void> createBoardImage(@RequestParam(name = "board_id") Long boardId, @RequestPart List<MultipartFile> imageFiles) {
        boardService.addImage(boardId, imageFiles);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/board/image")
    public ResponseEntity<Void> removeBoardImage(@RequestBody RemoveBoardImageRequest removeBoardImageRequest) {
        boardService.deleteImages(removeBoardImageRequest.getBoardId(), removeBoardImageRequest.getImageIds());
        return ResponseEntity.ok().build();
    }
}
