package com.ethanjohnson.flipside

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform