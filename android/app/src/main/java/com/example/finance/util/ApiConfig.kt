package com.example.finance.util

object ApiConfig {
    /**
     * API Base URL Configuration
     * 
     * IMPORTANT: Change this URL to point to your backend server
     * 
     * Examples:
     * - Local development (emulator): "http://10.0.2.2:8080/api/"
     * - Local development (physical device): "http://192.168.x.x:8080/api/"
     * - Production server: "http://your-server-ip:8080/api/"
     * - Production with domain: "https://api.yourapp.com/api/"
     */
    const val BASE_URL = "http://10.0.2.2:8080/api/"
    
    /**
     * Quick configuration presets
     * Uncomment the one you want to use and comment out BASE_URL above
     */
    
    // For Android Emulator (localhost on host machine)
    // const val BASE_URL = "http://10.0.2.2:8080/api/"
    
    // For Physical Device on same network (replace with your computer's IP)
    // const val BASE_URL = "http://192.168.1.100:8080/api/"
    
    // For Remote Server (replace with your server IP or domain)
    // const val BASE_URL = "http://your-server-ip:8080/api/"
    
    // For Production with HTTPS
    // const val BASE_URL = "https://api.yourapp.com/api/"
    
    /**
     * Connection timeout in seconds
     */
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
}
