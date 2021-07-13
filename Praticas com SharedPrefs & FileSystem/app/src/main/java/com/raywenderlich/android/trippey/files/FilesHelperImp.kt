package com.raywenderlich.android.trippey.files

import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files

class FilesHelperImp(private val directory: File) : FilesHelper {
    override fun saveData(fileName: String, data: String) {
        val file = buildFile(fileName)
        val fileOutputStream = buildOutputString(file)

        try {
            fileOutputStream.use {
                it.write(data.toByteArray())
            }
        } catch (error: Throwable) {
            error.printStackTrace()
        }
    }

    override fun getData(): List<File> = directory.listFiles()?.toList() ?: emptyList()


    override fun deleteData(fileName: String) {
        val targetFile = buildFile(fileName)

        if (targetFile.exists()) {
            targetFile.delete()
        }
    }

    private fun buildFile(fileName: String): File{
        return File(directory, fileName)
    }

    private fun buildOutputString(file: File) : FileOutputStream {
        return FileOutputStream(file)
    }
}