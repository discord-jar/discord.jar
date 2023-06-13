import com.seailz.discordjar.command.annotation.SlashCommandInfo;
import com.seailz.discordjar.command.listeners.slash.SlashCommandListener;
import com.seailz.discordjar.events.model.interaction.command.SlashCommandInteractionEvent;
import com.seailz.discordjar.utils.permission.Permission;

@SlashCommandInfo(
        name = "test", description = "Test Command",
        defaultMemberPermissions = {
            Permission.ADMINISTRATOR
        },
        canUseInDms = false
)
public class TestCmd extends SlashCommandListener {
    @Override
    protected void onCommand(SlashCommandInteractionEvent event) {
        event.reply("hello lol").setEphemeral(true).run();
    }
}
