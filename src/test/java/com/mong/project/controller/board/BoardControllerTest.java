package com.mong.project.controller.board;

import com.mong.project.controller.AbstractControllerTest;
import com.mong.project.domain.board.Board;
import com.mong.project.domain.member.Member;
import com.mong.project.dto.request.board.ChangeBoardRequest;
import com.mong.project.dto.request.board.CreateBoardRequest;
import com.mong.project.dto.request.board.RemoveBoardImageRequest;
import com.mong.project.exception.ErrorCode;
import com.mong.project.service.board.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardControllerTest extends AbstractControllerTest {

    @InjectMocks
    private BoardController boardController;

    @Mock
    private BoardService boardService;

    @Override
    protected Object setController() {
        return boardController;
    }

    @Test
    @DisplayName("board id 를 통해 board 를 조회합니다. 성공 시 200 을 응답합니다.")
    void getBoard() throws Exception{
        MockHttpServletRequestBuilder builder = get("/board/1");

        Member member = mock(Member.class);
        Board board = Board.builder()
                .id(1L).title("제목").content("내용").member(member).build();

        when(member.getId()).thenReturn(1L);
        when(member.getAlias()).thenReturn("테스트용");
        when(boardService.getBoardById(1L))
                .thenReturn(board);

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 board id 를 통해 board 를 조회합니다. NoSuchElementException 이 발생하고 400 을 응답합니다.")
    void getNotExistBoard() throws Exception{
        MockHttpServletRequestBuilder builder = get("/board/1");

        Member member = mock(Member.class);

        when(member.getId()).thenReturn(1L);
        when(member.getAlias()).thenReturn("테스트용");

        when(boardService.getBoardById(1L))
                .thenThrow(new NoSuchElementException(ErrorCode.NOT_EXIST_BOARD));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("member 가 작성한 boards 를 조회합니다. 성공 시 200 을 응답합니다.")
    void getMemberBoars() throws Exception {
        MockHttpServletRequestBuilder builder = get("/member/board")
                .param("member_id", "1");

        List<Board> boards = mock(List.class);
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(1L);
        when(member.getAlias()).thenReturn("테스트용");
        when(member.getBoards()).thenReturn(boards);
        when(boards.size()).thenReturn(2);

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("새로운 board 를 생성합니다. 사진이 있는 경우 image 까지 추가합니다. 성공 시 200 을 응답합니다.")
    void createBoard() throws Exception {
        MockMultipartFile file1
                = new MockMultipartFile("image", "test.PNG", MediaType.IMAGE_PNG_VALUE, "test".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2
                = new MockMultipartFile("image", "test.PNG", MediaType.IMAGE_PNG_VALUE, "test".getBytes(StandardCharsets.UTF_8));

        Board board = mock(Board.class);
        CreateBoardRequest createBoardRequest
                = new CreateBoardRequest("제목입니다.", "내용입니다.");

        when(boardService.addBoard(any(Long.class), any(Board.class)))
                .thenReturn(board);
        doNothing().when(boardService)
                .addImage(any(Long.class), any(List.class));

        MockHttpServletRequestBuilder builder = multipart("/board")
                .file(file1)
                .file(file2)
                .param("member_id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(createBoardRequest));

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("새로운 board 를 생성합니다. 사진이 없는 경우 image 를 추가하지 않습니다. 성공 시 200 을 응답합니다.")
    void createEmptyImageBoard() throws Exception {
        Board board = mock(Board.class);
        CreateBoardRequest createBoardRequest
                = new CreateBoardRequest("제목입니다.", "내용입니다.");

        when(boardService.addBoard(any(Long.class), any(Board.class)))
                .thenReturn(board);

        MockHttpServletRequestBuilder builder = post("/board")
                .param("member_id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(createBoardRequest));

        mockMvc.perform(builder)
                .andExpect(status().isOk());

        verify(boardService, times(0))
                .addImage(any(), anyList());
    }

    @Test
    @DisplayName("board 의 내용을 수정합니다. 성공 시 200 을 응답합니다.")
    void changeBoard() throws Exception {
        Board board = mock(Board.class);
        ChangeBoardRequest changeBoardRequest
                = new ChangeBoardRequest(1L, "제목입니다.", "내용입니다.");

        when(boardService.changeBoard(1L, "제목입니다.", "내용입니다,"))
                .thenReturn(board);

        MockHttpServletRequestBuilder builder = put("/board")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(changeBoardRequest));

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("board 를 삭제합니다. 성공 시 200 을 응답합니다.")
    void deleteBoard() throws Exception {
        doNothing().when(boardService).deleteBoard(anyLong());

        MockHttpServletRequestBuilder builder = delete("/board")
                .param("board_id", "1");

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("board 의 image 를 추가합니다. 성공 시 200 을 응답합니다.")
    void addBoardImage() throws Exception {
        MockMultipartFile file1
                = new MockMultipartFile("imageFiles", "test.PNG", MediaType.IMAGE_PNG_VALUE, "test".getBytes(StandardCharsets.UTF_8));
        MockMultipartFile file2
                = new MockMultipartFile("imageFiles", "test.PNG", MediaType.IMAGE_PNG_VALUE, "test".getBytes(StandardCharsets.UTF_8));

        doNothing().when(boardService)
                .addImage(any(Long.class), any(List.class));

        MockHttpServletRequestBuilder builder = multipart("/board/image")
                .file(file1)
                .file(file2)
                .param("board_id", "1");

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("board 의 image 를 삭제합니다. 성공 시 200 을 응답합니다.")
    void deleteBoardImage() throws Exception {
        RemoveBoardImageRequest removeBoardImageRequest
                = new RemoveBoardImageRequest(1L, List.of(1L, 2L, 3L));

        doNothing().when(boardService)
                .deleteImages(1L, List.of(1L, 2L, 3L));

        MockHttpServletRequestBuilder builder = delete("/board/image")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(removeBoardImageRequest));

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }
}