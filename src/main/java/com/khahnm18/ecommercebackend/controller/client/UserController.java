package com.khahnm18.ecommercebackend.controller.client;

import com.khahnm18.ecommercebackend.dto.response.ApiResponse;
import com.khahnm18.ecommercebackend.dto.response.user.MyProfileUserResponse;
import com.khahnm18.ecommercebackend.service.user.UserService;
import com.khahnm18.ecommercebackend.util.Translator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "USER-CONTROLLER")
@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "User management APIs")
public class UserController {

    private final Translator translator;
    private final UserService userService;

    @GetMapping("/my-profile")
    @Operation(summary = "Get my user profile")
    ApiResponse<MyProfileUserResponse> getMyProfile() {
        log.info("Get my profile");
        return ApiResponse.<MyProfileUserResponse>builder()
            .status(HttpStatus.OK.value())
            .data(userService.getMyProfile())
            .message(translator.toLocale("user.my-profile.success"))
            .build();
    }

}
