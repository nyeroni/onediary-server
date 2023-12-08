//package onediary.onediary.oauth.info.impl;
//
//import onediary.onediary.oauth.info.OAuth2UserInfo;
//
//import java.util.Map;
//
//public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
//
//    public KakaoOAuth2UserInfo(Map<String , Object> attributes){
//        super(attributes);
//    }
//    @Override
//    public String getId() {
//        return attributes.get("id").toString();
//    }
//
//    @Override
//    public String getName() {
//        // Kakao에서 전달되는 사용자의 닉네임을 반환합니다.
//        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
//
//        if (properties == null) {
//            return null;
//        }
//
//        return (String) properties.get("nickname");
//    }
//    @Override
//    public String getEmail()
//    {
//        // Kakao에서 전달되는 사용자의 이메일을 반환합니다.
//        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
//        return (String) kakaoAccount.get("email");
//    }
//}