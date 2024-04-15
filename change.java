import java.io.IOException;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHGist;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main {
    public static void main(String[] args) throws IOException {
        String githubToken = args[0];
        String gistId = args[1];
        String username = "Madness676";
        String repositoryName = "HashHash";
        String filePath = "new.txt";

        String latestCommitHash = getLatestGistCommitHash(gistId);
        
        GitHub github = new GitHubBuilder()
                .withOAuthToken(githubToken)
                .build();

        GHGist gist = github.getUser(username).getGist(gistId);
        GHContent file = gist.getFile(filePath);

        if (latestCommitHash != null) {
            if (file != null) {
                file.update("Updating file via GitHub Actions", latestCommitHash);
                System.out.println("File updated successfully!");
            } else {
                gist.createFile("Creating file via GitHub Actions", latestCommitHash);
                System.out.println("File created successfully!");
            }
        } else {
            System.out.println("Failed to retrieve latest commit hash of the Gist.");
        }
    }

    private static String getLatestGistCommitHash(String gistId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/gists/" + gistId)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                return latestCommitHash;
            }
        }
        return null;
    }
}
