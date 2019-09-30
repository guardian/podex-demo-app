package com.guardian.core.lib

import java.io.InputStream

interface XmlPullParserAdapter {
    suspend fun deSerialiseXml(xmlInput: InputStream, xmlDataObject: XmlDataObject): XmlDataObject
}