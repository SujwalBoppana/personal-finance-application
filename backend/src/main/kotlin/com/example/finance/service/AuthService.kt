package com.example.finance.service

import com.example.finance.domain.User
import com.example.finance.dto.request.LoginRequest
import com.example.finance.dto.request.RegisterRequest
import com.example.finance.dto.response.AuthResponse
import com.example.finance.dto.response.UserResponse
import com.example.finance.exception.BadRequestException
import com.example.finance.exception.UnauthorizedException
import com.example.finance.repository.UserRepository
import com.example.finance.security.JwtTokenProvider
import com.example.finance.security.UserPrincipal
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    
    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw BadRequestException("Email already exists")
        }
        
        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        val savedUser = userRepository.save(user)
        val token = jwtTokenProvider.generateTokenFromUserId(savedUser.id)
        
        return AuthResponse(
            token = token,
            user = UserResponse(
                id = savedUser.id,
                email = savedUser.email,
                createdAt = savedUser.createdAt,
                updatedAt = savedUser.updatedAt
            )
        )
    }
    
    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { UnauthorizedException("Invalid email or password") }
        
        if (!passwordEncoder.matches(request.password, user.password)) {
            throw UnauthorizedException("Invalid email or password")
        }
        
        val token = jwtTokenProvider.generateTokenFromUserId(user.id)
        
        return AuthResponse(
            token = token,
            user = UserResponse(
                id = user.id,
                email = user.email,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        )
    }
}
