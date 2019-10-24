package com.guardian.core.library.xml

import java.io.InputStream

interface XmlPullParserAdapter {
    fun deSerialiseXml(
        xmlInput: InputStream,
        rootDataObjectInitializer: () -> XmlDataObject
    ): XmlDataObject
}