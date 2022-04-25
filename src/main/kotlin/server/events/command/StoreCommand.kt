package server.events.command

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import server.commands.Command
import server.events.AnyStore
import javax.persistence.AttributeConverter
import javax.persistence.Converter


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
            }

            override fun convertToDatabaseColumn(command: StoreCommand): String =
                preConverterCommand.convertToDatabaseColumn(StorePreCommand(GSON.toJson(command.cardCommand), command.type))
            override fun convertToEntityAttribute(dbData: String): StoreCommand =
                preConverterCommand.convertToEntityAttribute(dbData).run {
                    StoreCommand(GSON.fromJson(cardCommand, type.commandType), type)
                }
        }
    }
}
