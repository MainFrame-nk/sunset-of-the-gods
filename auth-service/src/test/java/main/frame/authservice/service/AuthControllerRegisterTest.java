package main.frame.authservice.service;

//import main.frame.game.dto.UserDTO;
import main.frame.authservice.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerRegisterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

//    @Test
//    public void testRegisterUser_Success() throws Exception {
//        UserDTO userDTO = new UserDTO("test@example.com", "password");
//
//        when(userService.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
//
//        mockMvc.perform(post("/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(userDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Поздравляем! Вы успешно зарегистрировались!"));
//
//        verify(authService, times(1)).registerUser(userDTO);
//    }
//
//    @Test
//    public void testRegisterUser_EmailAlreadyExists() throws Exception {
//        UserDTO userDTO = new UserDTO("test@example.com", "password");
//
//        when(userService.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(new User()));
//
//        mockMvc.perform(post("/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(userDTO)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Такой email уже используется!"));
//
//        verify(authService, times(0)).registerUser(userDTO);
//    }
}