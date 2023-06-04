import com.seailz.discordjar.DiscordJar;
import com.seailz.discordjar.model.channel.AudioChannel;
import com.seailz.discordjar.model.guild.Guild;
import com.seailz.discordjar.utils.rest.DiscordRequest;
import com.seailz.discordjar.voice.model.provider.VoiceProvider;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import java.util.concurrent.ExecutionException;

public class DiscordJarTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException, DiscordRequest.UnhandledDiscordAPIErrorException {
        DiscordJar jar = new DiscordJar(args[0]);

        // Lavaplayer
        AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);

        AudioPlayer player = playerManager.createPlayer();

        AudioChannel channel = jar.getAudioChannelById("1092499443909136484");
        System.out.println(channel.raw());
        System.out.println(channel.name());

        Guild guild = jar.getGuildById("993461660792651826");
        System.out.println(guild.name());
        playerManager.loadItem("https://www.youtube.com/watch?v=25jHkLvTRtA&ab_channel=Discord", new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                player.playTrack(track);

                final AudioFrame[] frame = {null};
                channel.connect(new VoiceProvider(guild) {
                    @Override
                    public byte[] provide20ms() {
                        /// Provide from player
                        return frame[0].getData();
                    }

                    @Override
                    public boolean canProvide() {
                        frame[0] = player.provide();
                        return frame[0] != null;
                    }
                });
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                System.out.println("pl");
            }

            @Override
            public void noMatches() {
                System.out.println("nm");
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                System.out.println("lf");
            }
        });

    }
}
