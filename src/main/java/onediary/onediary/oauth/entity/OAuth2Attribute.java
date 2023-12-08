//package onediary.onediary.oauth.entity;
//
//
//import lombok.Builder;
//import lombok.Data;
//import onediary.onediary.member.entity.SocialProvider;
//
//import java.util.Map;
//
//@Data
//@Builder
//public class OAuth2Attribute {
//
//    private SocialProvider socialProvider;
//    private Long memberId;
//    private String username;
//    private String email;
//
//    /**
//     * OAuth2 속성 객체를 생성합니다.
//     *
//     * @param socialProvider        소셜 프로바이더 (KAKAO, NAVER 등)
//     * @param usernameAttributeName 사용자 이름을 나타내는 속성 이름
//     * @param attributes            소셜 로그인에서 얻은 속성 값들
//     * @return 생성된 OAuth2Attribute 객체
//     */
//    public static OAuth2Attribute of(SocialProvider socialProvider, String usernameAttributeName, Map<String, Object> attributes){
//        switch (socialProvider){
//            case KAKAO:
//                return OAuth2Attribute.ofKakao(socialProvider, usernameAttributeName, attributes);
//            case NAVER:
//                return OAuth2Attribute.ofNaver(socialProvider, usernameAttributeName, attributes);
//            //            case SocialProvider.APPLE:
////                return OAuth2Attribute.ofApple(socialProvider, usernameAttributeName, attributes);
//            default:
//                throw new RuntimeException("소셜 로그인 접근 실패");
//        }
//    }
//    /**
//     * 카카오 소셜 프로바이더의 속성을 기반으로 OAuth2Attribute 객체를 생성합니다.
//     *
//     * @param socialProvider        소셜 프로바이더 (KAKAO)
//     * @param usernameAttributeName 사용자 이름을 나타내는 속성 이름
//     * @param attributes            소셜 로그인에서 얻은 속성 값들
//     * @return 생성된 OAuth2Attribute 객체
//     */
//
//    private static OAuth2Attribute ofKakao(SocialProvider socialProvider, String usernameAttributeName, Map<String, Object>attributes){
//        String username = String.valueOf(attributes.get(usernameAttributeName));
//        Long memberId;
//        try {
//            memberId = Long.parseLong(username.concat("kakao"));
//        } catch (NumberFormatException e) {
//            // 변환 실패 시, memberId를 기본값으로 설정하거나 다른 예외 처리 방법 선택
//            memberId = 0L;  // 기본값 설정 예시
//            e.printStackTrace();
//        }
//        return OAuth2Attribute.builder()
//                .socialProvider(socialProvider)
//                .username(String.valueOf(attributes.get("nickname")))
//                .email(String.valueOf(attributes.get("email")))
//                .memberId(memberId).build();
//    }
//    /**
//     * 네이버 소셜 프로바이더의 속성을 기반으로 OAuth2Attribute 객체를 생성합니다.
//     *
//     * @param socialProvider        소셜 프로바이더 (NAVER)
//     * @param usernameAttributeName 사용자 이름을 나타내는 속성 이름
//     * @param attributes            소셜 로그인에서 얻은 속성 값들
//     * @return 생성된 OAuth2Attribute 객체
//     */
//    private static OAuth2Attribute ofNaver(SocialProvider socialProvider, String usernameAttributeName, Map<String, Object>attributes){
//        String username = String.valueOf(attributes.get(usernameAttributeName));
//        Long memberId;
//        try {
//            memberId = Long.parseLong(username.concat("naver"));
//        } catch (NumberFormatException e) {
//            // 변환 실패 시, memberId를 기본값으로 설정하거나 다른 예외 처리 방법 선택
//            memberId = 0L;  // 기본값 설정 예시
//            e.printStackTrace();
//        }
//        return OAuth2Attribute.builder()
//                .socialProvider(socialProvider)
//                .username(String.valueOf(attributes.get("name")))
//                .email(String.valueOf(attributes.get("email")))
//                .memberId(memberId).build();
//    }
//}
