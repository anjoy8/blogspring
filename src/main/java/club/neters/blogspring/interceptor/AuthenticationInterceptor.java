package club.neters.blogspring.interceptor;

import club.neters.blogspring.annotation.Authorize;
import com.auth0.jwk.GuavaCachedJwkProvider;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Value("${ids4.jwks.url}")
    private String jwksUrl;

    @Value("${ids4.issuer}")
    private String issuer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        // 判断执行的目标方法是否有Authorize注解，有Authorize注解的才进行token验证
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method=handlerMethod.getMethod();
        if (method.isAnnotationPresent(Authorize.class)) {
            String originToken = request.getHeader("Authorization");
            if (originToken == null || originToken.isEmpty()){
                // token为空返回401状态码
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            try {
                String token = getToken(originToken);
                // 解析token
                DecodedJWT jwt = JWT.decode(token);
                JwkProvider http = new UrlJwkProvider(new URL(jwksUrl));
                // 缓存jkws，默认缓存5个key，10小时，可自定义缓存策略
                JwkProvider provider = new GuavaCachedJwkProvider(http);
                Jwk jwk = provider.get(jwt.getId());
                Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(),null);
                // 验证token
                JWTVerifier verifier = JWT.require(algorithm)
                        //验证颁发者
                        .withIssuer(issuer)
                        // 验证scope
                        .withArrayClaim("scope","testapi")
                        .build();
                verifier.verify(token);
            } catch (JWTVerificationException exception){
                // token验证失败返回401
                PrintWriter writer = response.getWriter();
                writer.write(exception.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request,  HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private String getToken(String originToken) {
        String[] arr = originToken.split(" ");
        return arr[1];

    }
}

