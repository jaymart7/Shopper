package model.mapper

import model.presentation.Account
import model.response.AccountResponse

fun AccountResponse.toAccount() = Account(
    name = name,
    username = username
)