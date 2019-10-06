package com.kaola.lib_network.protocol

class BaseResponse<out T>(val status: Int, val message: String, val data: T)