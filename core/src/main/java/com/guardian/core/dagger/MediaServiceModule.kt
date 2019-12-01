package com.guardian.core.dagger

import android.content.Context
import com.guardian.core.R
import com.guardian.core.mediaplayer.PackageValidator
import com.guardian.core.mediaplayer.PackageValidatorImpl
import dagger.Module
import dagger.Provides

@Module
class MediaServiceModule {
    @Provides
    fun providePackageValidator(context: Context): PackageValidator =
        PackageValidatorImpl(context, R.xml.allowed_media_browser_callers)
}