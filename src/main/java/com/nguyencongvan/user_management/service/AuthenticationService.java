package com.nguyencongvan.user_management.service;

import com.nguyencongvan.user_management.dto.request.AuthenticationRequest;
import com.nguyencongvan.user_management.dto.request.IntrospectRequest;
import com.nguyencongvan.user_management.dto.request.InvalidatedTokenRequest;
import com.nguyencongvan.user_management.dto.request.RefreshTokenRequest;
import com.nguyencongvan.user_management.dto.response.AuthenticationResponse;
import com.nguyencongvan.user_management.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException;

    AuthenticationResponse refresh(RefreshTokenRequest request) throws ParseException, JOSEException;

    void logout(InvalidatedTokenRequest request) throws ParseException, JOSEException;
}
