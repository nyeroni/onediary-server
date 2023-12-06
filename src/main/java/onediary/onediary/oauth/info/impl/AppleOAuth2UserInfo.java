//package onediary.onediary.oauth.info.impl;
//
//
//import onediary.onediary.oauth.info.OAuth2UserInfo;
//
//import java.util.Map;
//
//public class AppleOAuth2UserInfo extends OAuth2UserInfo {
//
//
//    public AppleOAuth2UserInfo(Map<String , Object> attributes){
//        super(attributes);
//    }
//    @Override
//    public String getId() {
//        return attributes.get("sub").toString();
//    }
//
//    @Override
//    public String getName() {
//        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
//
//        if (properties == null) {
//            return null;
//        }
//
//        return (String) properties.get("name");
//    }
//    @Override
//    public String getEmail()
//    {
//        // 이메일 정보를 가져올 수 있는지 확인
//        if (attributes.containsKey("email")) {
//            return attributes.get("email").toString();
//        } else {
//            throw new RuntimeException("이메일 정보를 가져올 수 없습니다. Apple 로그인 시 이메일 제공 동의를 받아주세요.");
//        }    }
//}