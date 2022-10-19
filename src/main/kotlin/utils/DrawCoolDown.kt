package com.evolvedghost.utils

import com.evolvedghost.MiraiNovelaiNaifuConfig.coolDownTime
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DrawCoolDown {
    private var isCool = true
    private var mutex = Mutex()

    suspend fun start(): Boolean {
        mutex.withLock {
            if (isCool) {
                isCool = false
                return true
            }
            return false
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun end() {
        GlobalScope.launch {
            delay(coolDownTime * 1000)
            mutex.withLock {
                isCool = true
            }
        }
    }

    suspend fun endInstantly() {
        mutex.withLock {
            isCool = true
        }
    }
}