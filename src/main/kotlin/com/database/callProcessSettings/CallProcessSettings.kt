package com.database.callProcessSettings

import com.utils.DataError
import com.utils.Result
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException

object CallProcessSettings {

    private const val FILE_NAME = "CallProcessSettings.json"

    fun writeToFile(callProcessSettingsDto: CallProcessSettingsDto): Result<Unit, DataError.CallProcessSettingsError.WriteToFile> {
        return try {
            val jsonText = Json.encodeToString(callProcessSettingsDto)
            File(FILE_NAME).writeText(jsonText)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.CallProcessSettingsError.WriteToFile.UnknownError(e))
        }
    }

    fun fetchFromFile(): Result<CallProcessSettingsDto, DataError.CallProcessSettingsError.FetchFromFile> {
        return try {
            val jsonText = File(FILE_NAME).readText()
            val callProcessSettingsDto = Json.decodeFromString<CallProcessSettingsDto>(jsonText)
            Result.Success(callProcessSettingsDto)
        } catch (e: FileNotFoundException) {
            Result.Error(DataError.CallProcessSettingsError.FetchFromFile.FileNotFound)
        }
    }

}

