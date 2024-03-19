package com

enum class ServerConnectionStatus(val text: String) {
	Connected("Подключено"),
	Connecting("Подключение к серверу..."),
	PendingToken("Ожидание разрешения от сервера"),
	Disconnected("Не подключено")
}