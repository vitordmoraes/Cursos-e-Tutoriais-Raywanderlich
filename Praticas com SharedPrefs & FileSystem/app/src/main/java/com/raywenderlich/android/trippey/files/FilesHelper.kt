package com.raywenderlich.android.trippey.files

import java.io.File
import java.nio.file.Files

interface FilesHelper {

    fun saveData(filename:String, data: String)

    fun  getData(): List<File>

    fun deleteData(fileName: String)

}