package com.guardian.core.mediaplayer.daggermocks

import com.guardian.core.mediaplayer.PackageValidator

class TestPackageValidator : PackageValidator {
    override fun isKnownCaller(callingPackage: String, callingUid: Int): Boolean {
        return true
    }
}