package com.apinayami.demo.controller;

import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.service.Impl.ResponseCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/response")
@Validated
public class ResponseCommentController {
    private final ResponseCommentService responseCommentService;

    @PostMapping
    public ResponseData<?> createResponseComment(@RequestBody Map<String, String> body) {
        try{
            String result = responseCommentService.createResponseComment(body.get("reply"), body.get("staff"), Long.parseLong(body.get("id")));
            return new ResponseData<>(HttpStatus.CREATED.value(), result);
        }
        catch (Exception e){
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Tạo phản hồi thất bại");
        }
    }

}
