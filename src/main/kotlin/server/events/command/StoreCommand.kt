package server.events.command

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import server.commands.Command
import server.commands.card.CardCreateCommand
import server.commands.card.CardUpdateNameCommand
import server.events.AnyStore
import javax.persistence.AttributeConverter
import javax.persistence.Converter
import server.events.command.TypeCommand.*


data class StoreCommand(val cardCommand: Command, val type: TypeCommand): AnyStore {
    companion object {
        class StorePreCommand(val cardCommand: String, val type: TypeCommand)

        @Converter(autoApply = true)
        open class PreConverterCommand : AttributeConverter<StorePreCommand, String> {
            companion object {
                private val GSON = Gson()
                private val commandsListType = object : TypeToken<StorePreCommand>() {}.type
            }

            override fun convertToDatabaseColumn(command: StorePreCommand): String = GSON.toJson(command)
            override fun convertToEntityAttribute(dbData: String): StorePreCommand = GSON.fromJson(dbData, commandsListType)
        }

        @Converter(autoApply = true)
        open class ConverterCommand : AttributeConverter<StoreCommand, String> {
            companion object {
                private val GSON = Gson()
                private val preConverterCommand = PreConverterCommand()
                private val cardCreateType = object : TypeToken<CardCreateCommand>() {}.type
                private val cardUpdateNameType = object : TypeToken<CardUpdateNameCommand>() {}.type
            }

            override fun convertToDatabaseColumn(command: StoreCommand): String =
                preConverterCommand.convertToDatabaseColumn(StorePreCommand(GSON.toJson(command.cardCommand), command.type))
            override fun convertToEntityAttribute(dbData: String): StoreCommand =
                preConverterCommand.convertToEntityAttribute(dbData).run {
                    when(type) {
                        CARD_CREATE_COMMAND -> StoreCommand(GSON.fromJson(cardCommand, cardCreateType), type)
                        CARD_UPDATE_NAME_COMMAND -> StoreCommand(GSON.fromJson(cardCommand, cardUpdateNameType), type)
                    }
                }
        }
    }
}
