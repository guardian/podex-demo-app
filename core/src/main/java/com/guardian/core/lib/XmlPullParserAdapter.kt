package com.guardian.core.lib

import java.io.InputStream

interface XmlPullParserAdapter {
    suspend fun deSerialiseXml(xmlInput: InputStream,
                               rootDataObjectInitializer: () -> XmlDataObject): XmlDataObject
}