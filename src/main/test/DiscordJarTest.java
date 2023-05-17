import com.seailz.discordjar.DiscordJar;

import java.util.concurrent.ExecutionException;

public class DiscordJarTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        DiscordJar djar = new DiscordJar("MTA0OTY1MjE5ODI0Nzc2Mzk2OA.GNmxoU.U0OH77B-5fDs04B8rEl3Tf98xiZ4tGlw1fIiYA");
        System.out.println(djar.getSelfApplicationInfo());
    }

}
