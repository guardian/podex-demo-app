package com.guardian.core.dagger.xml

import java.io.InputStream

interface XmlPullParserAdapter {
    fun deSerialiseXml(
        xmlInput: InputStream,
        rootDataObjectInitializer: () -> XmlDataObject
    ): XmlDataObject
}