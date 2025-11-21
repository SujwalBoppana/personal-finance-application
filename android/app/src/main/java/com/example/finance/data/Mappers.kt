package com.example.finance.data

import com.example.finance.data.local.entity.AccountEntity
import com.example.finance.data.local.entity.BudgetEntity
import com.example.finance.data.local.entity.CategoryEntity
import com.example.finance.data.local.entity.TransactionEntity
import com.example.finance.domain.model.Account
import com.example.finance.domain.model.Budget
import com.example.finance.domain.model.Category
import com.example.finance.domain.model.Transaction

fun AccountEntity.toDomain(): Account {
    return Account(
        id = id,
        name = name,
        type = type,
        balance = balance,
        color = color
    )
}

fun Account.toEntity(): AccountEntity {
    return AccountEntity(
        id = id,
        name = name,
        type = type,
        balance = balance,
        color = color
    )
}

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        amount = amount,
        category = category,
        date = date,
        accountId = accountId,
        note = note,
        type = type
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        amount = amount,
        category = category,
        date = date,
        accountId = accountId,
        note = note,
        type = type
    )
}

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        icon = icon,
        type = type
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        icon = icon,
        type = type
    )
}

fun BudgetEntity.toDomain(): Budget {
    return Budget(
        id = id,
        category = category,
        amount = amount,
        month = month,
        year = year
    )
}

fun Budget.toEntity(): BudgetEntity {
    return BudgetEntity(
        id = id,
        category = category,
        amount = amount,
        month = month,
        year = year
    )
}
