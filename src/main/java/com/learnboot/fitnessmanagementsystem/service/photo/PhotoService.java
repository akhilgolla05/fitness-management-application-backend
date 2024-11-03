package com.learnboot.fitnessmanagementsystem.service.photo;

import com.learnboot.fitnessmanagementsystem.domains.Photo;
import com.learnboot.fitnessmanagementsystem.domains.User;
import com.learnboot.fitnessmanagementsystem.dto.PhotoDto;
import com.learnboot.fitnessmanagementsystem.exceptions.ResourceNotFoundException;
import com.learnboot.fitnessmanagementsystem.repository.PhotoRepository;
import com.learnboot.fitnessmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoService implements IPhotoService {

    private final PhotoRepository photoRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public PhotoDto addPhoto(MultipartFile file, long userId) throws IOException, SQLException {
        User dbUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Photo photo = new Photo();
        if(!file.isEmpty()){
            photo.setFilename(file.getOriginalFilename());
            photo.setFileType(file.getContentType());
            byte[] photoBytes  = file.getBytes();
            Blob blob = new SerialBlob(photoBytes);
            photo.setImage(blob);
        }
        Photo dbPhoto = photoRepository.save(photo);
        dbUser.setPhoto(dbPhoto);
        userRepository.save(dbUser);
        return mapPhotoToDto(dbPhoto);
    }

    @Override
    public PhotoDto getPhotoById(long id) {
        return photoRepository.findById(id)
                .map(photo-> {
                    try {
                        return mapPhotoToDto(photo);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found"));
    }

    @Transactional
    @Override
    public void deletePhotoById(long id, long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(user->{
                    user.setPhoto(null);
                    userRepository.save(user);
                },()->{
                    throw new ResourceNotFoundException("User not found");
                });

        photoRepository.findById(id)
                .ifPresentOrElse(photoRepository::delete,()->{
                    throw new ResourceNotFoundException("Photo not found");
                });
    }

    @Override
    public PhotoDto updatePhoto(long id, MultipartFile file) throws IOException, SQLException {

        Photo photo = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found"));
       if(!file.isEmpty()){
           photo.setFilename(file.getOriginalFilename());
           photo.setFileType(file.getContentType());
           byte[] photoBytes = file.getBytes();
           Blob blob = new SerialBlob(photoBytes);
           photo.setImage(blob);
       }
       Photo updatedPhoto = photoRepository.save(photo);
       return mapPhotoToDto(updatedPhoto);

    }

    @Override
    public byte[] getPhotoBytes(long id) throws SQLException {
        Photo dbPhoto = photoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found"));
        Blob blob = dbPhoto.getImage();
        int photoLength = (int)blob.length();
        return blob.getBytes(1, photoLength);
    }

    private PhotoDto mapPhotoToDto(Photo photo) throws SQLException {
        PhotoDto photoDto = modelMapper.map(photo, PhotoDto.class);
        Blob blob = photo.getImage();
        int length = (int) blob.length();
        byte[] bytes = blob.getBytes(1, length);
        photoDto.setImage(bytes);
        return photoDto;
    }


}
