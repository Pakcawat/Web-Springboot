package project.service;

import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import project.model.User;
import project.repository.UserDto;

public interface UserService {
    void saveUser(UserDto userDto , String siteURL)throws UnsupportedEncodingException, MessagingException ;

    User findUserByEmail(String email);

    public boolean verify(String verificationCode);

    public void defaultAdmin();


}
