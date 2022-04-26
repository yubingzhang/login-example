package com.example.third.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.third.exception.BizException;
import com.example.third.protocol.RetCodeEnum;
import com.example.third.service.AbstractThirdPartAuthStrategy;
import com.example.third.util.ThreadLocalUtil;
import com.example.third.vo.request.ThirdLoginRequest;
import com.example.third.vo.response.LoginResponse;
import com.example.third.vo.response.ThirdUserInfoResponse;
import com.google.protobuf.StringValue;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Map;
import java.util.Objects;

import static com.example.third.enums.ThirdTypeEnum.THIRD_TYPE_APPLE;


/**
 * apple授权服务
 *
 * @author zhangubing
 */
@Slf4j
@Service("appleAuthStrategy")
public class AppleAuthStrategy extends AbstractThirdPartAuthStrategy {

    private static final String APPLE_USER_ID = "apple_user_id";

    /**
     * 获取Apple公钥URL
     */
    private static final String GET_APPLE_PUBLIC_KEY_URL = "https://appleid.apple.com/auth/keys";

    /**
     * Apple官网URL
     */
    private static final String APPLE_OFFICE_URL = "https://appleid.apple.com";

    /**
     * Apple登录
     *
     * @param req 第三方授权请求信息
     * @return 登录结果
     */
    @Override
    public LoginResponse login(ThirdLoginRequest req) {
        try {
            return super.login(req);
        } finally {
            ThreadLocalUtil.remove(APPLE_USER_ID);
        }
    }

    /**
     * 获取Apple用户唯一标识
     *
     * @param req 请求参数
     * @return openId
     */
    @Override
    public String getOpenid(ThirdLoginRequest req) {
        String userId = verify(req.getCode());
        ThreadLocalUtil.put(APPLE_USER_ID, userId);
        return userId;
    }

    /**
     * 获取第三方登录信息
     *
     * @param req 请求参数
     * @return 用户信息
     */
    @Override
    public ThirdUserInfoResponse getThirdInfo(ThirdLoginRequest req) {
        String userId = ThreadLocalUtil.getAndRemove(APPLE_USER_ID);
        if (Objects.isNull(userId)) {
            userId = verify(req.getCode());
        }
        return ThirdUserInfoResponse.builder().bindType(THIRD_TYPE_APPLE.getValue())
                .openId(userId)
                .build();
    }

    /**
     * 解密个人信息
     *
     * @param identityToken APP获取的identityToken
     * @return sub就是用户id
     */
    public static String verify(String identityToken) {
        try {
            String[] identityTokens = identityToken.split("\\.");
            Map<String, Object> data1 = JSONObject.parseObject(new String(Base64.decodeBase64(identityTokens[0]), "UTF-8"));
            Map<String, Object> data2 = JSONObject.parseObject(new String(Base64.decodeBase64(identityTokens[1]), "UTF-8"));
            String kid = (String) data1.get("kid");
            String aud = (String) data2.get("aud");
            String sub = (String) data2.get("sub");
            if (verify(identityToken, kid, aud, sub)) {
                return sub;
            }
        } catch (Exception e) {
            log.error(">>>>> 验证Apple Token信息失败 ", e);
        }
        throw new BizException(RetCodeEnum.RET_INTERNAL_SERVER_ERROR);
    }

    /**
     * 验证Apple用户信息
     *
     * @param identityToken APP获取的identityToken
     * @param aud           Apple Developer帐户中的client_id
     * @param sub           用户的唯一标识符对应APP获取到的：user
     * @return true/false
     */
    private static boolean verify(String identityToken, String kid, String aud, String sub) {
        try {
            PublicKey publicKey = getPublicKey(kid);
            if (publicKey == null) {
                return false;
            }
            JwtParser jwtParser = Jwts.parser().setSigningKey(publicKey);
            jwtParser.requireIssuer(APPLE_OFFICE_URL);
            jwtParser.requireAudience(aud);
            jwtParser.requireSubject(sub);
            Jws<Claims> claim = jwtParser.parseClaimsJws(identityToken);
            if (claim != null && claim.getBody().containsKey("auth_time")) {
                return true;
            }
            log.error(">>>>> 验证Apple用户信息不通过 aud:{} sub:{} ", aud, sub);
        } catch (Exception e) {
            log.error(">>>>> 验证Apple用户信息失败 aud:{} sub:{} ", aud, sub, e);
        }
        return false;
    }

    /**
     * 获取Apple公钥
     *
     * @return 公钥
     */
    private static PublicKey getPublicKey(String kid) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            JSONObject data = restTemplate.getForObject(GET_APPLE_PUBLIC_KEY_URL, JSONObject.class);
            JSONArray jsonArray = data.getJSONArray("keys");
            String n = "";
            String e = "";
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (StringUtils.equals(jsonObject.getString("kid"), kid)) {
                    n = jsonObject.getString("n");
                    e = jsonObject.getString("e");
                }
            }
            BigInteger modulus = new BigInteger(1, Base64.decodeBase64(n));
            BigInteger publicExponent = new BigInteger(1, Base64.decodeBase64(e));
            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, publicExponent);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (Exception e) {
            log.error(">>>>> 获取Apple公钥失败 ", e);
            throw new BizException(RetCodeEnum.RET_INTERNAL_SERVER_ERROR);
        }
    }
}
