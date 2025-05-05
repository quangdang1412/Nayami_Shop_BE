package com.apinayami.demo.controller;

import com.apinayami.demo.dto.response.ResponseCommentDTO;
import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.service.Impl.ResponseCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/responses")
@Validated
public class ResponseCommentController {
    private final ResponseCommentService responseCommentService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STAFF')")
    public ResponseData<?> createResponseComment(@RequestBody Map<String, String> body) {
        try {
            String result = responseCommentService.createResponseComment(body.get("reply"), body.get("staff"), Long.parseLong(body.get("id")));
            return new ResponseData<>(HttpStatus.CREATED.value(), result);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Tạo phản hồi thất bại");
        }
    }

    @GetMapping("/{id}")
    public ResponseData<?> getResponseCommentsByProductId(@PathVariable long id) {
        try {
            List<ResponseCommentDTO> responseCommentDTOList = responseCommentService.getResponseCommentsByProductId(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Get responses successfully", responseCommentDTOList);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lấy phản hồi thất bại");
        }
    }

}
