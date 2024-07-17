package www.project.config.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import www.project.config.oauth2.provider.GoogleUserInfo;
import www.project.config.oauth2.provider.KakaoUserInfo;
import www.project.config.oauth2.provider.NaverUserInfo;
import www.project.config.oauth2.provider.OAuth2UserInfo;
import www.project.domain.UserVO;
import www.project.repository.UserMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserMapper userMapper;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("getAttributes >>> {}",oAuth2User.getAttributes());
        OAuth2UserInfo oAuth2UserInfo = null;
        String provider = userRequest.getClientRegistration().getRegistrationId();
        if(provider.equalsIgnoreCase("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if(provider.equals("naver")){
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        } else if(provider.equals("kakao")){
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }

        provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getPfoviderId();
        String email = oAuth2UserInfo.getEmail();
        String nickName = oAuth2UserInfo.getName();
        UserVO originUser = userMapper.searchUser(providerId);
        if(originUser == null) {
            log.info("첫 로그인");
            UserVO newUser = new UserVO();
            newUser.setEmail("("+provider+")"+email);
            newUser.setNickname(nickName);
            newUser.setProvider(provider);
            newUser.setProviderId(providerId);
            userMapper.insertSocialUser(newUser);
            userMapper.insertAuth(newUser.getEmail());
            return new PrincipalDetails(newUser, oAuth2User.getAttributes());
        } else {
            log.info("이미 있는 유저");
            return new PrincipalDetails(originUser, oAuth2User.getAttributes());
        }
    }
































}
