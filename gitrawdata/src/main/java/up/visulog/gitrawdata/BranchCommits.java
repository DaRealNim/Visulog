package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BranchCommits {
    private String nomDeLaBranche;
    private int nbCommits;

    public BranchCommits(String nomDeLaBranche, int nbCommits) {
        this.nomDeLaBranche = nomDeLaBranche;
        this.nbCommits = nbCommits;
    }

    public static List<BranchCommits> countCommitsPerBranch(Path gitPath) {
        List<String> branches = getAllLocalBranches(gitPath);
        List<BranchCommits> results = new ArrayList<>();
        results.add(new BranchCommits("test", 2));
        return results;
    }

    private static List<String> getAllLocalBranches(Path gitPath) { /// permet de récuperer toutes les branches locales
                                                                    /// utilisées dans le projet.
        ProcessBuilder getBranches = new ProcessBuilder("git", "branch");
        Process process;
        try {
            process = getBranches.start();
        } catch (IOException e) {
            /// ajouter un traitement en cas d'exception.
            throw new RuntimeException("Error running \"git branch\".", e);
        }
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return parseBranch(reader);
    }

    private static List<String> parseBranch(BufferedReader reader) {
        List<String> branches = new ArrayList<>();
        try {
            while (reader.ready()) {
                branches.add(reader.readLine());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return branches;
    }
}
