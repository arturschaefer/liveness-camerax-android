package com.schaefer.livenesscamerax.domain.checker

import com.schaefer.livenesscamerax.core.factory.Factory

internal class LivenessCheckerFactory : Factory<LivenessChecker> {
    override fun create(): LivenessChecker {
        return LivenessCheckerImpl()
    }
}
