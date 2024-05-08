package com.database.messageTemplates

import com.utils.DataError
import com.utils.Result
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object MessageTemplates: LongIdTable("message_templates") {

    private val name = varchar("name", 100)
    private val text = varchar("text", 500)
    private val isSurnamePlaceholderUsed = bool("is_surname_placeholder_used")
    private val isNamePlaceholderUsed = bool("is_name_placeholder_used")
    private val isPatronymicPlaceholderUsed = bool("is_patronymic_placeholder_used")
    private val isPhoneNumberPlaceholderUsed = bool("is_phoneNumber_placeholder_used")
    private val isSexPlaceholderUsed = bool("is_sex_placeholder_used")
    private val isAgePlaceholderUsed = bool("is_age_placeholder_used")


    fun insert(messageTemplate: MessageTemplateDto): Result<Unit, DataError.MessageTemplateError.Insert> {
        return try {
            transaction {
                MessageTemplates.insert {
                    it[name] = messageTemplate.name
                    it[text] = messageTemplate.text
                    it[isSurnamePlaceholderUsed] = messageTemplate.placeholders.isSurnamePlaceholderUsed
                    it[isNamePlaceholderUsed] = messageTemplate.placeholders.isNamePlaceholderUsed
                    it[isPatronymicPlaceholderUsed] = messageTemplate.placeholders.isPatronymicPlaceholderUsed
                    it[isPhoneNumberPlaceholderUsed] = messageTemplate.placeholders.isPhoneNumberPlaceholderUsed
                    it[isSexPlaceholderUsed] = messageTemplate.placeholders.isSexPlaceholderUsed
                    it[isAgePlaceholderUsed] = messageTemplate.placeholders.isAgePlaceholderUsed
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.MessageTemplateError.Insert.UnknownError(e))
        }
    }


    fun fetch(id: Long): Result<MessageTemplateDto, DataError.MessageTemplateError.Fetch> {
        return try {
            transaction {
                val messageTemplateModel = MessageTemplates.selectAll().where { MessageTemplates.id.eq(id) }.single()
                val messageTemplateDto = MessageTemplateDto(
                    id = messageTemplateModel[MessageTemplates.id].value,
                    name = messageTemplateModel[name],
                    text = messageTemplateModel[text],
                    placeholders = MessageTemplatePlaceholdersDto(
                        isSurnamePlaceholderUsed = messageTemplateModel[isSurnamePlaceholderUsed],
                        isNamePlaceholderUsed = messageTemplateModel[isNamePlaceholderUsed],
                        isPatronymicPlaceholderUsed = messageTemplateModel[isPatronymicPlaceholderUsed],
                        isPhoneNumberPlaceholderUsed = messageTemplateModel[isPhoneNumberPlaceholderUsed],
                        isSexPlaceholderUsed = messageTemplateModel[isSexPlaceholderUsed],
                        isAgePlaceholderUsed = messageTemplateModel[isAgePlaceholderUsed],
                    )
                )
                Result.Success(messageTemplateDto)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.MessageTemplateError.Fetch.MessageTemplateDoesNotExist)
        }
    }

    fun fetchAll(): Result<List<MessageTemplateDto>, DataError.MessageTemplateError.Fetch> {
        return try {
            transaction {
                val messageTemplateList = mutableListOf<MessageTemplateDto>()
                val messageTemplatesModelList = MessageTemplates.selectAll().toList()

                messageTemplatesModelList.map {
                    messageTemplateList.add(
                        MessageTemplateDto(
                            id = it[MessageTemplates.id].value,
                            name = it[name],
                            text = it[text],
                            placeholders = MessageTemplatePlaceholdersDto(
                                isSurnamePlaceholderUsed = it[isSurnamePlaceholderUsed],
                                isNamePlaceholderUsed = it[isNamePlaceholderUsed],
                                isPatronymicPlaceholderUsed = it[isPatronymicPlaceholderUsed],
                                isPhoneNumberPlaceholderUsed = it[isPhoneNumberPlaceholderUsed],
                                isSexPlaceholderUsed = it[isSexPlaceholderUsed],
                                isAgePlaceholderUsed = it[isAgePlaceholderUsed],
                            )
                        )
                    )
                }

                Result.Success(messageTemplateList)
            }
        } catch (e: NoSuchElementException) {
            Result.Error(DataError.MessageTemplateError.Fetch.MessageTemplateDoesNotExist)
        }
    }

    fun remove(id: Long): Result<Unit, DataError.MessageTemplateError.Remove> {
        return try {
            transaction {
                MessageTemplates.deleteWhere { MessageTemplates.id.eq(id) }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.MessageTemplateError.Remove.UnknownError(e))
        }
    }

}

