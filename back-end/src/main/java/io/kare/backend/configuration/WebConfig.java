package io.kare.backend.configuration;

import io.kare.backend.component.user.UserJwtTokenGenerator;
import io.kare.backend.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final UserJwtTokenGenerator userJwtTokenGenerator;
    private final UserService userService;

    public WebConfig(UserJwtTokenGenerator userJwtTokenGenerator, UserService userService) {
        this.userJwtTokenGenerator = userJwtTokenGenerator;
        this.userService = userService;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserArgumentResolver(this.userJwtTokenGenerator, this.userService));
    }
}