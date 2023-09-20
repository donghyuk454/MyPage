package com.mong.project.controller.board;

import com.mong.project.domain.board.Board;
import com.mong.project.domain.member.annotation.Login;
import com.mong.project.controller.board.dto.request.ChangeBoardRequest;
import com.mong.project.controller.board.dto.request.CreateBoardRequest;
import com.mong.project.controller.board.dto.request.RemoveBoardImageRequest;
import com.mong.project.controller.board.dto.response.GetBoardResponse;
import com.mong.project.service.board.BoardService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
@RequestMapping("/api/v2/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/{boardId}")
    public ResponseEntity<GetBoardResponse> getBoard(@Valid @PathVariable(name = "boardId") Long boardId) {
        Board board = boardService.getBoardById(boardId);

        return ResponseEntity.ok().body(new GetBoardResponse(board));
    }

    @PostMapping
    public ResponseEntity<Void> createBoard(@Login final Long memberId,
                                            @Nullable @RequestPart(name = "image") List<MultipartFile> images,
                                            @Valid @RequestBody CreateBoardRequest createBoardRequest) {
        Board board = boardService.addBoard(memberId,
                createBoardRequest.toBoard());
        if (images != null && images.isEmpty())
            boardService.addImage(board.getId(), images);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> changeBoard(@Valid @RequestBody ChangeBoardRequest changeBoardRequest) {
        boardService.changeBoard(changeBoardRequest.getBoardId(), changeBoardRequest.getTitle(), changeBoardRequest.getContent());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBoard(@NotNull @RequestParam(name = "boardId") Long boardId) {
        boardService.deleteBoard(boardId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/image")
    public ResponseEntity<Void> createBoardImage(@NotNull @RequestParam(name = "boardId") Long boardId,
                                                 @RequestPart List<MultipartFile> imageFiles) {
        boardService.addImage(boardId, imageFiles);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/image")
    public ResponseEntity<Void> removeBoardImage(@Valid @RequestBody RemoveBoardImageRequest removeBoardImageRequest) {
        boardService.deleteImages(removeBoardImageRequest.getBoardId(), removeBoardImageRequest.getImageIds());

        return ResponseEntity.ok().build();
    }
}
