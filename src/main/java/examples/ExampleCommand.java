package examples;

import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.command.CommandOption;
import com.seailz.discordjar.command.CommandOptionType;
import com.seailz.discordjar.command.listeners.CommandListener;
import com.seailz.discordjar.command.listeners.slash.SlashCommandListener;
import com.seailz.discordjar.command.listeners.slash.SlashSubCommand;
import com.seailz.discordjar.command.listeners.slash.SubCommandGroup;
import com.seailz.discordjar.command.listeners.slash.SubCommandListener;
import com.seailz.discordjar.events.model.interaction.command.SlashCommandInteractionEvent;

/**
 * This is an example of a slash command.
 * It will reply with "Hello World!" when the command is executed.
 * <p>
 * To register commands, you must use the {@link DiscordJar#registerCommands(CommandListener...)} method.
 * You can also implement other types of commands,
 * like {@link com.seailz.discordjar.command.listeners.MessageContextCommandListener MessageContextCommandListener} and {@link com.seailz.discordjar.command.listeners.UserContextCommandListener UserContextCommandListener}.
 * <p>
 * When setting up your command, you must use the {@link com.seailz.discordjar.command.annotation.SlashCommandInfo SlashCommandInfo} annotation to provide info about the command to discord.jar
 *
 * @author Seailz
 * @since 1.0
 * @Date: <b>2022-11-28</b> - Added sub commands
 * @see DiscordJar#registerCommands(CommandListener...)
 * @see com.seailz.discordjar.command.listeners.MessageContextCommandListener
 * @see com.seailz.discordjar.command.listeners.UserContextCommandListener
 */
public class ExampleCommand extends SlashCommandListener {

    /**
     * This is for sub commands, and options.
     */
    public ExampleCommand() {
        // This is how you register sub commands.

        addSubCommand(
                new SlashSubCommand(
                        "sub",
                        "This is a sub command!"
                ),
                new ExampleSubCommand()
        );

        // You can also add sub command groups
        SubCommandGroup group = new SubCommandGroup("group");
        group.setDescription("This is a sub command group!");
        group.addSubCommand(
                new SlashSubCommand(
                        "sub",
                        "This is a sub command in a group!"
                ),
                new ExampleSubCommand()
        );

        // This is how you add options
        addOption(
                new CommandOption(
                        "option",
                        "This is an option!",
                        CommandOptionType.STRING,
                        true
                )
        );
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        event.reply("Hello World!");
    }


    /**
     * You can also add sub commands to your slash command.
     * When one or more sub commands are implemented, the base command will not be usable.
     * <p>
     * To register sub commands, you'll need to use the {@code addSubCommand} method in the constructor
     * of your base command.
     */
    static class ExampleSubCommand implements SubCommandListener {
        @Override
        public void onSubCommand(SlashCommandInteractionEvent event) {
            event.reply("Hello from a sub command!");
        }
    }
}