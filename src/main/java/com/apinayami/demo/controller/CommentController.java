package com.apinayami.demo.controller;

import com.apinayami.demo.dto.request.CommentDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.service.Impl.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Validated
public class CommentController {
    private final CommentServiceImpl commentService;

    @GetMapping
    public ResponseData<?> getAllComments() {
        return new ResponseData<>(HttpStatus.OK.value(), "Success", commentService.getAllComments());
    }

    @GetMapping("/{id}")
    public ResponseData<?> getCommentByProductId(@PathVariable long id) {
        return new ResponseData<>(HttpStatus.OK.value(), "Success", commentService.getCommentByProductId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseData<?> addComment(@Validated @RequestBody CommentDTO commentDTO) {
        try {
            commentService.create(commentDTO);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Success", "Thêm thành công " + commentDTO.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Save failed");
        }
    }

    @PostMapping("/active")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseData<?> cancelComment(@RequestBody Map<String, String> body) {
        try {
            String result = commentService.updateStatus(Long.parseLong(body.get("id")));
            return new ResponseData<>(HttpStatus.OK.value(), "Success", result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update active failed");
        }
    }

}
