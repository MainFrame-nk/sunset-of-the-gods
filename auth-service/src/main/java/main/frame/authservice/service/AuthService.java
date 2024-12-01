package main.frame.authservice.service;

import main.frame.authservice.dto.request.RegisterRequest;
import main.frame.shared.dto.UserDTO;

public interface AuthService {
    String registerUser(RegisterRequest registerRequest);
    String loginUser(UserDTO userDTO);
}
