package io.kare.backend.configuration;

import io.kare.backend.annotation.User;
import io.kare.backend.component.user.UserJwtTokenGenerator;
import io.kare.backend.service.UserService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserJwtTokenGenerator userJwtTokenGenerator;
    private final UserService userService;

    public UserArgumentResolver(UserJwtTokenGenerator userJwtTokenGenerator, UserService userService) {
        this.userJwtTokenGenerator = userJwtTokenGenerator;
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(User.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("Authorization");
        if (token == null) {
            return null;
        }
        token = token.replace("Bearer ", "");
        String userId = this.userJwtTokenGenerator.getUserIdFromToken(token);

        return this.userService.getUserById(userId);
    }
}
