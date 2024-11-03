package com.learnboot.fitnessmanagementsystem.controller;

import com.learnboot.fitnessmanagementsystem.dto.PhotoDto;
import com.learnboot.fitnessmanagementsystem.response.ApiResponse;
import com.learnboot.fitnessmanagementsystem.service.photo.IPhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
@Slf4j
public class PhotoController {

    private final IPhotoService photoService;

    @PostMapping("/upload-photo")
    public ResponseEntity<ApiResponse> uploadPhoto(@RequestParam("file") MultipartFile file,
                                                   @RequestParam long userId) throws SQLException, IOException {
        log.info("Uploading photo for User {}", userId);
        PhotoDto photoDto = photoService.addPhoto(file,userId);
        return ResponseEntity.ok(new ApiResponse("Photo Uploaded Successfully", photoDto));
    }

    @GetMapping("/get-photo/{photoId}")
    public ResponseEntity<ApiResponse> getPhotoById(@PathVariable long photoId) throws SQLException, IOException {
        PhotoDto photoDto = photoService.getPhotoById(photoId);
        return ResponseEntity.ok(new ApiResponse("Photo Found Successfully", photoDto));
    }
    @PutMapping("/update-photo/{photoId}")
    public ResponseEntity<ApiResponse> updatePhoto(@RequestParam("file") MultipartFile file,
                                                   @PathVariable long photoId) throws SQLException, IOException {
        PhotoDto photoDto = photoService.updatePhoto(photoId,file);
        return ResponseEntity.ok(new ApiResponse("Photo Updated Successfully", photoDto));
    }

    @DeleteMapping("/delete-photo/{photoId}/user/{userId}")
    public ResponseEntity<ApiResponse> deletePhoto(@PathVariable long photoId,@PathVariable long userId) throws SQLException, IOException {
        photoService.deletePhotoById(photoId,userId);
        return ResponseEntity.ok(new ApiResponse("Photo Deleted Successfully", null));
    }
    @GetMapping("/get-photo-bytes/{photoId}")
    public ResponseEntity<ApiResponse> getPhotoBytes(@PathVariable long photoId) throws SQLException, IOException {
        byte[] photoBytes = photoService.getPhotoBytes(photoId);
        return ResponseEntity.ok(new ApiResponse("Photo Found Successfully", photoBytes));
    }
}
