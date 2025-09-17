// SPDX-FileCopyrightText: 2023-2025 Tampere region
//
// SPDX-License-Identifier: LGPL-2.1-or-later

package fi.tampere.trevaka.archival

import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
internal class Success {
    @get:XmlElement
    var records: Records? = null

    @get:XmlElement
    var files: Files? = null

    override fun toString(): String = "Success(records=$records, files=$files)"
}

internal class Records {
    @get:XmlElement
    var record: Record? = null

    override fun toString(): String = "Records(record=$record)"
}

internal class Record {
    @get:XmlElement
    var otherId: String? = null

    @get:XmlElement
    var archiveId: String? = null

    override fun toString(): String = "Record(otherId=$otherId, archiveId=$archiveId)"
}

internal class Files {
    @get:XmlElement
    var file: List<File>? = null

    override fun toString(): String = "Files(file=$file)"
}

internal class File {
    @get:XmlElement
    var fileName: String? = null

    @get:XmlElement
    var originalId: String? = null

    @get:XmlElement
    var archiveId: String? = null

    override fun toString(): String = "FileResponse(fileName=$fileName, originalId=$originalId, archiveId=$archiveId)"
}

@XmlRootElement
internal class Error {
    @get:XmlElement(name = "error_code")
    var errorCode: String? = null

    @get:XmlElement(name = "error_summary")
    var errorSummary: String? = null

    override fun toString(): String = "Error(errorCode=$errorCode, errorSummary=$errorSummary)"
}
