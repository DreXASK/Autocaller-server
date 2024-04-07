package com

enum class ServerConnectionStatus() {
	Connected(),
	Connecting(),
	PendingToken(),
	Disconnected()
}