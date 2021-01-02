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
        ProcessBuilder getNbCommits;
        Process process;
        InputStream is;
        BufferedReader reader;
        List<BranchCommits> result = new ArrayList<>();
        for (String branche : branches) {
            try {
                System.out.println(branche);
                getNbCommits = new ProcessBuilder("git", "rev-list", "--count", branche);
                process = getNbCommits.start();
                is = process.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is));
                result.add(new BranchCommits(branche, parseNbCommits(reader)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static List<String> getAllLocalBranches(Path gitPath) { /// permet de récuperer toutes les branches locales
                                                                    /// et distantes
                                                                    /// utilisées dans le projet.
        ProcessBuilder getBranches = new ProcessBuilder("git", "branch", "--all");
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
        reader.lines().forEach((s) -> {
            if (!s.contains("HEAD")) {
                if (s.trim().contains("*"))
                    branches.add(s.substring(2).trim());
                else
                    branches.add(s.trim());
            }
        });
        return branches;
    }

    private static int parseNbCommits(BufferedReader reader) {
        try {
            return Integer.valueOf(reader.readLine());
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    public int getNbCommits() {
        return nbCommits;
    }

    public String getNomDeLaBranche() {
        return nomDeLaBranche;
    }
}
