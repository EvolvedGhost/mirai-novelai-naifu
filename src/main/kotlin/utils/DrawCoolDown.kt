package com.evolvedghost.utils

import com.evolvedghost.MainConfig.coolDownTime
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DrawCoolDown {
    private var isCool = true
    private var mutex = Mutex()
    private var leftTime = -1L

    suspend fun start(): Boolean {
        mutex.withLock {
            if (isCool) {
                leftTime = -1L
                isCool = false
                return true
            }
            return false
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun end() {
        GlobalScope.launch {
            leftTime = 0
            while (leftTime < coolDownTime) {
                delay(1000)
                leftTime++
            }
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

    fun getLeftTime(): Long {
        return if(leftTime==-1L) -1L
        else coolDownTime - leftTime
    }
}