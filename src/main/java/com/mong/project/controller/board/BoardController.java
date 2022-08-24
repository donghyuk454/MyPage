package com.mong.project.controller.board;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.member.annotation.Login;
import com.mong.project.dto.request.board.ChangeBoardRequest;
import com.mong.project.dto.request.board.CreateBoardRequest;
import com.mong.project.dto.request.board.RemoveBoardImageRequest;
import com.mong.project.dto.response.board.GetBoardResponse;
import com.mong.project.service.board.BoardService;

import com.mong.project.util.transformer.BoardTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<GetBoardResponse> getBoard(@PathVariable(name = "boardId") Long boardId) {
        Board board = boardService.getBoardById(boardId);

        return ResponseEntity.ok().body(new GetBoardResponse(board));
    }

    @PostMapping
    public ResponseEntity<Void> createBoard(@Login final Long memberId,
                                            @Nullable @RequestPart(name = "image") List<MultipartFile> images,
                                            @RequestBody CreateBoardRequest createBoardRequest) {
        Board board = boardService.addBoard(memberId,
                BoardTransformer.createBoardRequestToBoard(createBoardRequest));
        if (images != null && images.isEmpty())
            boardService.addImage(board.getId(), images);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> changeBoard(@Login final Long memberId,
                                            @RequestBody ChangeBoardRequest changeBoardRequest) {
        boardService.changeBoard(changeBoardRequest.getBoardId(), changeBoardRequest.getTitle(), changeBoardRequest.getContent());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBoard(@Login final Long memberId,
                                            @RequestParam(name = "boardId") Long boardId) {
        boardService.deleteBoard(boardId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/image")
    public ResponseEntity<Void> createBoardImage(@Login final Long memberId,
                                                 @RequestParam(name = "boardId") Long boardId,
                                                 @RequestPart List<MultipartFile> imageFiles) {
        boardService.addImage(boardId, imageFiles);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/image")
    public ResponseEntity<Void> removeBoardImage(@Login final Long memberId,
                                                 @RequestBody RemoveBoardImageRequest removeBoardImageRequest) {
        boardService.deleteImages(removeBoardImageRequest.getBoardId(), removeBoardImageRequest.getImageIds());

        return ResponseEntity.ok().build();
    }
}
