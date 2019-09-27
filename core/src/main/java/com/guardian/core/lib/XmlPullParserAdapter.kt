package com.guardian.core.lib

import java.io.InputStream

interface XmlPullParserAdapter<out T> {
    fun parseXml(xmlInput: InputStream): T
}