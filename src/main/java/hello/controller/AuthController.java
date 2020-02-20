package hello.controller;

import hello.entity.Result;
import hello.entity.User;
import hello.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class AuthController {
    private UserService userService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/auth")
    @ResponseBody
    public Object auth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByUsername(authentication == null ? null : authentication.getName());

        if (loggedInUser == null) {
            return Result.success("User not logged in", false);
        } else {
            return Result.success(null, true, loggedInUser);
        }
    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Object logout() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByUsername(userName);

        if (loggedInUser == null) {
            //用户未登录
            return Result.failure("User not logged in");
        } else {
            SecurityContextHolder.clearContext();
            // 注销成功
            return Result.success("Logout successful", false);
        }
    }

    @PostMapping("/auth/register")
    @ResponseBody   // 告诉springboot 返回的是一个json
    public Result register(@RequestBody Map<String, String> usernameAndPasswordJson) {
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");
        if (username.length() < 1 || username.length() > 15) {
            // 无效用户名
            return Result.failure("invalid username");
        }
        if (password.length() < 1 || password.length() > 15) {
            // 无效密码
            return Result.failure("invalid password");
        }
        try {
            userService.save(username, password);
        } catch (DuplicateKeyException e) {
            // 用户存在
            return Result.failure("user already exists");
        }
        // 注册成功
        return Result.success("registration success", false);

    }

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String, String> usernameAndPasswordJson) {
        String username = usernameAndPasswordJson.get("username");
        String password = usernameAndPasswordJson.get("password");
        UserDetails userDetails;
        try {
            userDetails = userService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            // 用户不存在
            return Result.failure("User does not exist");
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password);

        try {
            authenticationManager.authenticate(token);
            // 把用户信息（Cookie）保存在一个地方
            SecurityContextHolder.getContext().setAuthentication(token);
            // 登陆成功
            return Result.success("Landed successfully", true, userService.getUserByUsername(username));
        } catch (BadCredentialsException e) {
            // 密码错误
            return Result.failure("wrong password");
        }
    }
}
