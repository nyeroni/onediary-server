//package onediary.onediary.oauth.info.impl;
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
//        return attributes.get("id").toString();
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
//        return (String) attributes.get("email");
//    }
//}
