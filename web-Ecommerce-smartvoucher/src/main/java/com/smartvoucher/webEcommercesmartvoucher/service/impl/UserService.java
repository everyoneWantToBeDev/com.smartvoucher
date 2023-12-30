package com.smartvoucher.webEcommercesmartvoucher.service.impl;

import com.google.api.services.drive.model.File;
import com.smartvoucher.webEcommercesmartvoucher.converter.UserConverter;
import com.smartvoucher.webEcommercesmartvoucher.dto.BlockUserDTO;
import com.smartvoucher.webEcommercesmartvoucher.dto.ChangePasswordDTO;
import com.smartvoucher.webEcommercesmartvoucher.dto.UserDTO;
import com.smartvoucher.webEcommercesmartvoucher.dto.UserDetailDTO;
import com.smartvoucher.webEcommercesmartvoucher.entity.UserEntity;
import com.smartvoucher.webEcommercesmartvoucher.entity.enums.Provider;
import com.smartvoucher.webEcommercesmartvoucher.exception.ChangePasswordException;
import com.smartvoucher.webEcommercesmartvoucher.exception.ObjectNotFoundException;
import com.smartvoucher.webEcommercesmartvoucher.exception.UserNotFoundException;
import com.smartvoucher.webEcommercesmartvoucher.payload.ResponseOutput;
import com.smartvoucher.webEcommercesmartvoucher.repository.UserRepository;
import com.smartvoucher.webEcommercesmartvoucher.service.IUserService;
import com.smartvoucher.webEcommercesmartvoucher.service.oauth2.security.OAuth2UserDetailCustom;
import com.smartvoucher.webEcommercesmartvoucher.util.UploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final UploadUtil uploadUtil;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final UserConverter userConverter,
                       final UploadUtil uploadUtil,
                       final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.uploadUtil = uploadUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadUserImages(MultipartFile fileName, Principal connectedUser) {
        String folderId = "1jNZ5_XieGUGKYIhNq3P2tflcKYHCwdb5";
        String email = connectedUser.getName();
        UserEntity userEntity = userRepository.findByEmailAndProvider(email, Provider.local.name());
        if (userEntity != null){
            File file = uploadUtil.uploadImages(fileName, folderId);
            userEntity.setAvatarUrl("https://drive.google.com/uc?export=view&id="+file.getId());
            this.userRepository.save(userEntity);
        }else {
            log.info("User not found data");
            throw new UserNotFoundException(404, "User not found data");
        }
        log.info("Your avatar upload successfully !");
        return "Your avatar upload successfully !";
    }



    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUser() {
        if (userRepository.findAll().isEmpty()) {
            log.info("List user is empty !");
            throw new ObjectNotFoundException(404, "List user is empty !");
        }
        log.info("Get all user completed !");
        return userConverter.toUserDTOList(userRepository.findAll());
    }

    @Override
    public ResponseOutput getAllUser(int page, int limit, String sortBy, String sortField) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.fromString(sortBy), sortField));
        List<UserDTO> userDTOList = userConverter.toUserDTOList(
                userRepository.findAll(pageable).getContent()
        );
        if (userDTOList.isEmpty()){
            log.info("List user is empty !");
            throw new ObjectNotFoundException(404, "List user is empty !");
        }
        int totalItem = (int) userRepository.count();
        int totalPage = (int) Math.ceil((double) totalItem / limit);
        log.info("Get all user completed !");
        return new ResponseOutput(
                page,
                totalItem,
                totalPage,
                userDTOList
        );
    }

    @Override
    public List<UserDTO> searchUserByEmail(String email) {
        return userConverter.toUserDTOList(
                userRepository.searchAllByEmailContainingIgnoreCase(email)
        );
    }


    @Override
    @Transactional(readOnly = true)
    public UserDTO getEmail(UserDTO userDTO) {
        if (userRepository.findOneByEmail(userDTO.getEmail()) == null) {
            log.info("User not found or not exist !");
            throw new UserNotFoundException(404, "User not found or not exist !");
        }
        log.info("Get email of user " + userDTO.getUserName() + " is completed !");
        return userConverter.toUserDTO(userRepository.findOneByEmail(userDTO.getEmail()));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailDTO getUserById(Long id) {
        if (userRepository.findOneByIdAndProvider(id, Provider.local.name()) == null) {
            log.info("User not found or not exist !");
            throw new UserNotFoundException(404, "User not found or not exist !");
        }
        log.info("Get user by id " + id + " is completed !");
        return userConverter.toUserDetailDTO(userRepository.findOneByIdAndProvider(id, Provider.local.name()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String changePassword(ChangePasswordDTO changePasswordDTO, Principal connectedUser) {
        String email =  connectedUser.getName();
        UserEntity user = userRepository.findByEmailAndProvider(email, Provider.local.name());
        //check MK user trong database
        if (!(passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPwd()))){
            log.info("Wrong password !");
            throw new ChangePasswordException(501, "Wrong password !");
        }
        if (!(changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword()))){
            log.info("Password won't match !");
            throw new ChangePasswordException(501, "Password won't match !");
        }
        //update new password
        user.setPwd(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        this.userRepository.save(user);
        log.info("Change password successfully !");
        return "Change password successfully !";
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailDTO getInformationLoginUser(Principal connectedUser) {
        UserEntity user;
        String email = connectedUser.getName();
        if (this.userRepository.findByEmailAndProvider(email, Provider.local.name()) != null) {
            user = userRepository.findByEmailAndProvider(email, Provider.local.name());
            log.info("Get information login user completed !");
            return userConverter.toUserDetailDTO(user);
        }else {
            log.info("Get information login user failed, User not found data");
            throw new UserNotFoundException(404, "User not found data");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailDTO getInformationOauth2User(OAuth2UserDetailCustom oAuth2UserDetail) {
        UserEntity user;
        String email = oAuth2UserDetail.getUsername();
        if (this.userRepository.findByEmailAndProvider(email, Provider.google.name()) != null) {
            user = userRepository.findByEmailAndProvider(email, Provider.google.name());
            log.info("Get information oAuth2User completed !");
            return userConverter.toUserDetailDTO(user);
        }else {
            log.info("Get information oAuth2User failed, User not found data");
            throw new UserNotFoundException(404, "User not found data");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String editUserProfile(UserDetailDTO userDetailDTO,
                                  Principal connectedUser) {
        String email = connectedUser.getName();
        UserEntity user = userRepository.findByEmailAndProvider(email, Provider.local.name());
        if (user != null){
            UserEntity newUser = userConverter.toUserDetailEntity(userDetailDTO, user);
            this.userRepository.save(newUser);
        }else {
            log.info("Edit user profile failed, User not found data");
            throw new UserNotFoundException(404, "User not found data");
        }
        log.info("Update your profile is successfully !");
        return "Update your profile is successfully !";
    }

    @Override
    public Boolean blockUser(BlockUserDTO blockUserDTO) {
        UserEntity user = userRepository.findOneById(blockUserDTO.getId());
        boolean isBlockUser;
        if (user != null){
            isBlockUser = blockUserDTO.isEnable();
            user.setEnable(isBlockUser);
            user.setStatus(blockUserDTO.getStatus());
            this.userRepository.save(user);
        }else {
            throw new UserNotFoundException(404, "User not found data !");
        }
        return isBlockUser;
    }
}
