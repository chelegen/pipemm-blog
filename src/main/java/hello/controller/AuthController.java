package hello.controller;

import hello.entity.LoginResult;
import hello.entity.Result;
import hello.service.AuthService;
import hello.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;

@Controller
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @Inject
    public AuthController(UserService userService, AuthenticationManager authenticationManager, AuthService authService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @GetMapping("/auth")
    @ResponseBody
    public Result auth() {
        return authService.getCurrentUser()
                .map(LoginResult::success)
                .orElse(LoginResult.success("用户没有登录", false));
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Result logout() {
        return authService.getCurrentUser()
                .map(user -> LoginResult.success("注销成功", false))
                .orElse(LoginResult.success("用户没有登录", false));

    }

    @PostMapping("/auth/register")
    @ResponseBody   // 告诉springboot 返回的是一个json
    public Result register(@RequestBody Map<String, String> usernameAndPasswordJson) {
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");
        if (username.length() < 1 || username.length() > 15) {
            return LoginResult.failure("无效用户名");
        }
        if (password.length() < 1 || password.length() > 15) {
            return LoginResult.failure("无效密码");
        }
        try {
            userService.save(username, password);
        } catch (DuplicateKeyException e) {
            return LoginResult.failure("用户已经存在");
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());
        authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(token);
        return LoginResult.success("注册成功", userService.getUserByUsername(username));
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> usernameAndPasswordJson) {
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");
//        UserDetails userDetails;
//        try {
//            userDetails = userService.loadUserByUsername(username);
//        } catch (UsernameNotFoundException e) {
//            return LoginResult.failure("用户不存在");
//        }
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

    /*  Spring Security 是使用org.springframework.security.core.userdetails.User类作为用户登录凭据( Principal )的。
        在 authenticate 方法内部，会通过传进来的 principal 得到用户名，principal 可以是很多类型，最终都是获取 username。
        随后会去调用 loadUserByUsername
        如果用户不存在 会抛出 UsernameNotFoundException ,通过一个属性控制 hideUserNotFoundExceptions ，是否转成 BadCredentialsException    */
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList());

        try {
            authenticationManager.authenticate(token);
            // 把用户信息（Cookie）保存在一个地方
            SecurityContextHolder.getContext().setAuthentication(token);
            return LoginResult.success("登录成功", userService.getUserByUsername(username));
        } catch (UsernameNotFoundException e) {
            return LoginResult.failure("用户不存在");
        } catch (BadCredentialsException e) {
            return LoginResult.failure("密码不正确");
        }
    }
}
