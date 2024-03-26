package com

enum class AuthStatus(val text: String) {
	Authorized("Авторизован"),
	Authorization("Авторизирование"),
	Unauthorized("Не авторизован")
}