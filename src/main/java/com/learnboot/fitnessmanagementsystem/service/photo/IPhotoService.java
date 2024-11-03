package com.learnboot.fitnessmanagementsystem.service.photo;

import com.learnboot.fitnessmanagementsystem.dto.PhotoDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

public interface IPhotoService {
    PhotoDto addPhoto(MultipartFile file, long userId) throws IOException, SQLException;

    PhotoDto getPhotoById(long id);

    @Transactional
    void deletePhotoById(long id, long userId);

    PhotoDto updatePhoto(long id, MultipartFile file) throws IOException, SQLException;

    byte[] getPhotoBytes(long id) throws SQLException;
}
