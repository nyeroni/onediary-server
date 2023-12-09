package onediary.onediary;

import com.fasterxml.jackson.databind.ObjectMapper;
import onediary.onediary.oauth.token.AuthResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@WithMockUser(username = "mockUser")
@Import(TestSecurityConfig.class) // JWT 인증과정을 무시하기 위해 사용
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    private static final String accessToken = "csdfnlLSDFasdf23adFKASDFdsansd";

    @Test
    public void 카카오_회원가입_및_로그인_성공() throws Exception{

        //given
        String object = objectMapper.writeValueAsString(AuthResponseDto.builder()
                .jwtToken(accessToken)
                .isNewMember(false)
                .build());

        //when
        ResultActions actions = mockMvc.perform(
                post("/auth/kakao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(object)
        );

        //then
        actions
                .andDo(print())
                .andExpect(status().isOk());
    }


}
