package com.tapyou.test.domain.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("code")
    val code: Int? = null,
    @SerialName("errorCode")
    val errorCode: Int? = null,
    @SerialName("errorMessage")
    val errorMessage: String? = "",
    @SerialName("errorDescription")
    val errorDescription: String? = ""
)
