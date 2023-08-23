import com.seailz.discordjar.command.annotation.SlashCommandInfo;
import com.seailz.discordjar.command.listeners.slash.SlashCommandListener;
import com.seailz.discordjar.events.model.interaction.command.SlashCommandInteractionEvent;

@SlashCommandInfo(
        name = "testrl",
        description = "testrl"
)
public class ExampleCommand extends SlashCommandListener {
    @Override
    protected void onCommand(SlashCommandInteractionEvent event) {

    }
}
