package server.db.postgresql.entities

import server.events.command.StoreCommand
import javax.persistence.*

@Entity
@Table(name = "SimpleEvents")
class SimpleCommand(
    @Convert(converter = StoreCommand.Companion.ConverterCommand::class)
    @Column(columnDefinition = "jsonb", name = "command", nullable = false)
    val command: StoreCommand) {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null
}
