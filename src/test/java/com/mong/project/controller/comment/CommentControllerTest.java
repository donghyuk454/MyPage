package com.mong.project.controller.comment;

import com.mong.project.controller.AbstractControllerTest;
import com.mong.project.domain.comment.Comment;
import com.mong.project.dto.request.comment.AddCommentRequest;
import com.mong.project.dto.request.comment.ChangeCommentRequest;
import com.mong.project.exception.ErrorCode;
import com.mong.project.service.comment.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends AbstractControllerTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    @Override
    protected Object setController() {
        return commentController;
    }

    @Test
    @DisplayName("댓글을 작성합니다. 성공 시 200 을 응답합니다.")
    void addComment() throws Exception {
        AddCommentRequest addCommentRequest
                = new AddCommentRequest(1L, 1L, "댓글입니다.");

        MockHttpServletRequestBuilder builder = post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(addCommentRequest));

        doNothing().when(commentService)
                .addComment(1L,1L,"댓글입니다.");

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글의 내용을 수정합니다. 성공 시 200 을 응답합니다.")
    void changeComment() throws Exception {
        ChangeCommentRequest changeCommentRequest
                = new ChangeCommentRequest(1L, "수정할 내용입니다.");

        MockHttpServletRequestBuilder builder = put("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(changeCommentRequest));

        when(commentService.changeComment(1L, "수정할 내용입니다."))
                .thenReturn(Comment.builder().id(1L).content("수정할 내용입니다.").build());

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 댓글의 내용을 수정합니다. 업는 댓글이므로 NoSuchElementException 이 발생하고 400 을 응답합니다.")
    void changeNotExistComment() throws Exception {
        ChangeCommentRequest changeCommentRequest
                = new ChangeCommentRequest(1L, "수정할 내용입니다.");

        MockHttpServletRequestBuilder builder = put("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(changeCommentRequest));

        when(commentService.changeComment(any(Long.class), anyString()))
                .thenThrow(new NoSuchElementException(ErrorCode.NOT_EXIST_COMMENT));

        mockMvc.perform(builder)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("댓글을 삭제합니다. 성공 시 200 을 응답합니다.")
    void deleteComment() throws Exception {
        MockHttpServletRequestBuilder builder = delete("/comments")
                .param("comment_id", "1");

        doNothing().when(commentService)
                .deleteComment(1L);

        mockMvc.perform(builder)
                .andExpect(status().isOk());
    }
}