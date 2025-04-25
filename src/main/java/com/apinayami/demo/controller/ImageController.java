package com.apinayami.demo.controller;

import com.apinayami.demo.dto.response.ResponseData;
import com.apinayami.demo.dto.response.ResponseError;
import com.apinayami.demo.service.IImageService;
import com.google.api.Http;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
@Validated
@PreAuthorize("hasAuthority('ADMIN')")
public class ImageController {
    private final IImageService imageService;

    @PostMapping
    public ResponseData<?> deleteImage(@RequestBody Map<String, String> body) {
        try{
            imageService.cloudinaryDelete(body.get("url"));
            return new ResponseData<>(HttpStatus.OK.value(), "Xóa ảnh thành công");
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
