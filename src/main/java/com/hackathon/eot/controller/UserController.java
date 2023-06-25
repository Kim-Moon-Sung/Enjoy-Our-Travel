package com.hackathon.eot.controller;

import com.hackathon.eot.dto.request.UserJoinRequest;
import com.hackathon.eot.dto.request.UserLoginRequest;
import com.hackathon.eot.dto.response.Response;
import com.hackathon.eot.dto.response.UserJoinResponse;
import com.hackathon.eot.dto.response.UserLoginResponse;
import com.hackathon.eot.model.dto.User;
import com.hackathon.eot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request);
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getUserAccountId(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }
}
