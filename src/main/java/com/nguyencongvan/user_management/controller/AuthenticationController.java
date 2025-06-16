package com.nguyencongvan.user_management.controller;

import com.nguyencongvan.user_management.dto.request.AuthenticationRequest;
import com.nguyencongvan.user_management.dto.request.IntrospectRequest;
import com.nguyencongvan.user_management.dto.request.InvalidatedTokenRequest;
import com.nguyencongvan.user_management.dto.request.RefreshTokenRequest;
import com.nguyencongvan.user_management.dto.response.ApiResponse;
import com.nguyencongvan.user_management.dto.response.AuthenticationResponse;
import com.nguyencongvan.user_management.dto.response.IntrospectResponse;
import com.nguyencongvan.user_management.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/token")
    @Operation(
            summary = "Authenticate user",
            description = "Verify username and password. Returns a signed JWT token if credentials are valid."
    )
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/introspect")
    @Operation(
            summary = "Check token validity",
            description = "Parses and validates a token to determine if it is still valid and not expired or revoked."
    )
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout user",
            description = "Invalidates the given token by adding it to the blacklist, preventing further usage."
    )
    public ApiResponse<Void> logout(@RequestBody InvalidatedTokenRequest request)
            throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh access token",
            description = "Takes a valid refresh token and returns a new JWT access token. The refresh token is then invalidated."
    )
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refresh(request);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }
}
