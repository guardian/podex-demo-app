package com.guardian.core.dagger.xml

import java.io.InputStream

interface XmlPullParserAdapter {
    suspend fun deSerialiseXml(xmlInput: InputStream,
                               rootDataObjectInitializer: () -> XmlDataObject
    ): XmlDataObject
}