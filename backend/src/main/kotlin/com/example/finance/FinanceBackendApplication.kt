package com.example.finance

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FinanceBackendApplication

fun main(args: Array<String>) {
    runApplication<FinanceBackendApplication>(*args)
}
