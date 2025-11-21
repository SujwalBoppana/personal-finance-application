package com.example.finance.data.mapper

import com.example.finance.data.remote.dto.AccountDto
import com.example.finance.data.remote.dto.BudgetDto
import com.example.finance.data.remote.dto.CategoryDto
import com.example.finance.data.remote.dto.TransactionDto
import com.example.finance.domain.model.Account
import com.example.finance.domain.model.Budget
import com.example.finance.domain.model.Category
import com.example.finance.domain.model.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun AccountDto.toDomain(): Account {
    return Account(
        id = id,
        name = name,
        type = type,
        balance = balance,
        color = color
    )
}

fun TransactionDto.toDomain(): Transaction {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("UTC") // Assuming backend sends UTC
    val parsedDate = try {
        dateFormat.parse(date) ?: Date()
    } catch (e: Exception) {
        Date()
    }

    return Transaction(
        id = id,
        amount = amount,
        category = category,
        date = parsedDate,
        accountId = accountId,
        note = note,
        type = type
    )
}

fun BudgetDto.toDomain(): Budget {
    return Budget(
        id = id,
        category = category,
        amount = amount,
        month = month,
        year = year
    )
}

fun CategoryDto.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        icon = icon,
        type = type
    )
}
