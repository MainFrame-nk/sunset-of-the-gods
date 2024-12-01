package main.frame.authservice.service;

import main.frame.authservice.controller.AuthController;
//import main.frame.game.dto.UserDTO;
import main.frame.authservice.model.User;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(AuthController.class)
public class AuthControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void testRegisterUser() throws Exception {
//        mockMvc.perform(post("/auth/register")
//                        .contentType("application/json")
//                        .content("{\"email\": \"test@example.com\", \"password\": \"password\"}"))
//                .andExpect(status().isOk());
//    }
}