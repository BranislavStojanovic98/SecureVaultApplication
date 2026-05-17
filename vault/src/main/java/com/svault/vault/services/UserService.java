package com.svault.vault.services;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.svault.vault.audit.AuditedAction;
import com.svault.vault.auth.AuthRequest;
import com.svault.vault.exceptions.NotFoundException;
import com.svault.vault.models.dto.UserDTO;
import com.svault.vault.models.entity.TotpAuthEntity;
import com.svault.vault.models.entity.UserEntity;
import com.svault.vault.models.mappers.UserMapper;
import com.svault.vault.repositories.TotpAuthRepository;
import com.svault.vault.repositories.UserRepository;
import com.svault.vault.util.EncryptionService;
import com.svault.vault.util.QrCodeGenerator;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TotpAuthRepository totpAuthRepository;

    private final EncryptionService encryptionService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException());
    }

    public UserDTO getUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException());
        return UserMapper.toDto(user);
    }
    
    @AuditedAction("CREATE_USER")
    public UserDTO createUser(UserDTO request) {

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());

        user.setPasswordHash(request.getPasswordHash());
        user.setEmail(request.getEmail());
        user.setIsActive(true);
        user.setIsFrozen(false);
        user.setRole(request.getRole());
        user.setPublicKey(request.getPublicKey());

        byte[] saltBytes = Base64.getDecoder().decode(request.getSalt());

        user.setSalt(saltBytes);
        user.setUpdatedAt(request.getUpdatedAt());

        UserEntity saved = userRepository.save(user);


        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();

        String secret = key.getKey();
        String encrypted = encryptionService.encrypt(secret);

        TotpAuthEntity totp = new TotpAuthEntity();
        totp.setUser(saved);
        totp.setSecret(encrypted);
        totpAuthRepository.save(totp);

        String issuer = "SecureVault";
        String account = saved.getEmail();

        String otpAuthUrl = String.format(
            "otpauth://totp/%s:%s?secret=%s&issuer=%s", 
            issuer,
            account,
            secret,
            issuer
        );

        String qrCode;

        try {
            qrCode = QrCodeGenerator.generateQRCode(otpAuthUrl, 250, 250);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }

        UserDTO dto = UserMapper.toDto(saved);
        dto.setTotpQrCode(qrCode);

        return dto;
    }

    @AuditedAction("EDIT_USER")
    public UserDTO editUser(Integer id, UserDTO request) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist"));
        
        if(!request.getUsername().isBlank() && !user.getUsername().equals(request.getUsername())) {
            user.setUsername(request.getUsername());
        }

        if(!request.getRole().equals(null) && !user.getRole().equals(request.getRole())) {
            user.setRole(request.getRole());
        }

        user.setId(id);
        UserEntity saved = userRepository.save(user);
        return UserMapper.toDto(saved);
    }
    
    @AuditedAction("ACTIVATE_USER")
    public void activateUser(Integer id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist"));

        user.setId(id);
        user.setIsActive(true);
        userRepository.save(user);
    }

    @AuditedAction("DEACTIVATE_USER")
    public void deactivateUser(Integer id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist"));

        user.setId(id);
        user.setIsActive(false);
        userRepository.save(user);
    }

    @AuditedAction("FREEZE_USER")
    public void freezeUser(Integer id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist"));

        user.setId(id);
        user.setIsFrozen(true);
        userRepository.save(user);
    }

    @AuditedAction("UNFREEZE_USER")
    public void unfreezeUser(Integer id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User does not exist"));

        user.setId(id);
        user.setIsFrozen(false);
        userRepository.save(user);
    }

    public byte[] getSalt(String username) {
        UserEntity entity = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("No user"));
        byte[] salt = entity.getSalt();
        return salt;
    }

    public String getPublicKey(Integer id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("No user"));
        return user.getPublicKey();
    }

    public boolean verifyMasterPassword(AuthRequest request) {
        UserEntity user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new NotFoundException("No user"));
        boolean valid = user.getPasswordHash().equals(request.getPassword());

        return valid;
    }

    public List<UserDTO> getAllUsers(Integer userId) {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().filter(user -> !user.getId().equals(userId)).map(UserMapper::toDto).collect(Collectors.toList());
        

    }
}
