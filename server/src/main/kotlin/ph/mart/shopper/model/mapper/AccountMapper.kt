package ph.mart.shopper.model.mapper

import ph.mart.shopper.model.presentation.Account
import ph.mart.shopper.model.request.AccountRequest
import ph.mart.shopper.model.response.AccountResponse

internal fun AccountRequest.toAccount(id: Int): Account = Account(
    id = id,
    name = name,
    username = username,
    password = password
)

internal fun Account.toAccountResponse(): AccountResponse = AccountResponse(
    username = username,
    name = name
)