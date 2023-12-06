package onediary.onediary;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.Keys;
import onediary.onediary.domain.member.entity.Member;
import onediary.onediary.domain.member.repository.MemberQuerydslRepository;
import onediary.onediary.mock.WithCustomMockUser;
import onediary.onediary.oauth.clientProxy.KakaoClient;
import onediary.onediary.oauth.service.KakaoAuthService;
import onediary.onediary.oauth.token.AuthRequestDto;
import onediary.onediary.oauth.token.AuthToken;
import onediary.onediary.oauth.token.AuthTokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.UnsupportedEncodingException;
import java.security.Key;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application-test.yml")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private KakaoClient kakaoClient;

    @Mock
    private MemberQuerydslRepository memberQuerydslRepository;

    @Mock
    private AuthTokenProvider authTokenProvider;

    @InjectMocks
    private KakaoAuthService kakaoAuthService;

    @Value("${jwt.secret}")
    private String testTokenSecret;


    public AuthControllerTest() throws UnsupportedEncodingException {
    }

    @Test
    @WithCustomMockUser
    public void testKakaoAuthRequest() throws Exception {
        // Given
        AuthRequestDto authRequest = new AuthRequestDto("test-access-token");
        Member kakaoMember = Member.builder()
                .socialId("test-social-id")
                .build();
        Key key = Keys.hmacShaKeyFor(testTokenSecret.getBytes("UTF-8"));


        when(kakaoClient.getUserData("test-access-token")).thenReturn(kakaoMember);
        when(memberQuerydslRepository.findBySocialId("test-social-id")).thenReturn(null);
        when(authTokenProvider.createMemberJwtToken("test-social-id")).thenReturn(new AuthToken("test-jwt-token", key));

        // When and Then
        mockMvc.perform(post("/api/v1/login/kakao")
                        .content(asJsonString(authRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.jwtToken").value("test-jwt-token"));
    }
    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
