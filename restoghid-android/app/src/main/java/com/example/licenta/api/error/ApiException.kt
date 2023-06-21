package com.example.licenta.api.error

import java.lang.Exception

class ApiException(message: String) : Exception(message)

class NetworkException(message: String) : Exception(message)