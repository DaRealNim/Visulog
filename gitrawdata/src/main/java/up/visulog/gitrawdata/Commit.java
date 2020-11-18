package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Commit {
    // FIXME: (some of) these fields could have more specialized types than String
    public final String id;
    public final String date;
    public final String author;
    public final String description;
    public final String mergedFrom;
    public final String stat; // number of insertions and deletions for each commit

    public Commit(String id, String author, String date, String description, String mergedFrom, String stat) {
        this.id = id;
        this.author = author;
        this.date = date;
        this.description = description;
        this.mergedFrom = mergedFrom;
        this.stat = stat;
    }

    // TODO: factor this out (similar code will have to be used for all git commands)
    public static List<Commit> parseLogFromCommand(Path gitPath) {
        ProcessBuilder builder =
                new ProcessBuilder("git", "log", "--shortstat").directory(gitPath.toFile());
        Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException("Error running \"git log\".", e);
        }
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return parseLog(reader);
    }

    public static List<Commit> parseLog(BufferedReader reader) {
        var result = new ArrayList<Commit>();
        Optional<Commit> commit = parseCommit(reader);
        while (commit.isPresent()) {
            result.add(commit.get());
            commit = parseCommit(reader);
        }
        return result;
    }
    
    public static List<Commit> parseFromCommand(Path gitPath) {
    	Scanner sc=new Scanner(System.in);
    	ProcessBuilder builder;

    	String com=" ";
    	String opt=" ";
    	com=sc.nextLine();
    	opt=sc.nextLine();
    	if(!(opt.equals(" "))) {
    		builder =new ProcessBuilder("git", com,opt).directory(gitPath.toFile());
    	}else {
    		builder = new ProcessBuilder("git", com).directory(gitPath.toFile());
    	}
        Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException("Error running \"git\".", e);
        }
        InputStream is = process.getInputStream();//read the process output 
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return parse(reader);
    }

    public static List<Commit> parse(BufferedReader reader) { //prints the content of is
        var result = new ArrayList<Commit>();
        Optional<Commit> commit = parseCommit(reader);
        while (commit.isPresent()) {
            result.add(commit.get());
            commit = parseCommit(reader);
        }
        return result;
    }

    /**
     * Parses a log item and outputs a commit object. Exceptions will be thrown in case the input does not have the proper format.
     * Returns an empty optional if there is nothing to parse anymore.
     */
    public static Optional<Commit> parseCommit(BufferedReader input) {
    	int lineCount = 0;
        try {

            var line = input.readLine();
            lineCount++; //keeps track of which line we're at ine the file for errors
            if (line == null) return Optional.empty(); // if no line can be read, we are done reading the buffer
            var idChunks = line.split(" ");
            if (!idChunks[0].equals("commit")) parseError();
            var builder = new CommitBuilder(idChunks[1]);

            line = input.readLine();
            lineCount++;
            while (!line.isEmpty()) {
                var colonPos = line.indexOf(":");
                var fieldName = line.substring(0, colonPos);
                var fieldContent = line.substring(colonPos + 1).trim();
                switch (fieldName) {
                    case "Author":
                        builder.setAuthor(fieldContent);
                        break;
                    case "Merge":
                        builder.setMergedFrom(fieldContent);
                        break;
                    case "Date":
                        builder.setDate(fieldContent);
                        break;
                    default: // warns the user that some field was ignored
                    	System.out.println(fieldName+" at line "+lineCount+" was ignored; name invalid. should be Author/Merge/Date");
                }
                line = input.readLine(); //prepare next iteration
                lineCount++;
                if (line == null) parseError(); // end of stream is not supposed to happen now (commit data incomplete)
            }

            // now read the commit message per se
            var description = input
                    .lines() // get a stream of lines to work with
                    .takeWhile(currentLine -> !currentLine.isEmpty()) // take all lines until the first empty one (commits are separated by empty lines). Remark: commit messages are indented with spaces, so any blank line in the message contains at least a couple of spaces.
                    .map(String::trim) // remove indentation
                    .reduce("", (accumulator, currentLine) -> accumulator + currentLine); // concatenate everything
            builder.setDescription(description);
            
            if(builder.getMergedFrom()==null) { //if this is not a merge commit
            	var statistiques = input
            			.lines() //get a stream of lines to work with
            			.takeWhile(currentLine -> !currentLine.isEmpty()) // take all lines until the first empty one (commits are separated by empty lines). Remark: commit messages are indented with spaces, so any blank line in the message contains at least a couple of spaces.
            			.reduce("", (accumulator, currentLine) -> accumulator + currentLine); // concatenate everything
            	builder.setStat(statistiques);
            }
            return Optional.of(builder.createCommit());
        } catch (IOException e) {
            parseError();
        }
        return Optional.empty(); // this is supposed to be unreachable, as parseError should never return
    }

    // Helper function for generating parsing exceptions. This function *always* quits on an exception. It *never* returns.
    private static void parseError() {
        throw new RuntimeException("Wrong commit format.");
    }

    @Override
    public String toString() {
        return "Commit { " +
                "id='" + id + '\'' +
                (mergedFrom != null ? (", mergedFrom...='" + mergedFrom + '\'') : "") +
                ", date='" + date + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                (stat != null ? (", statistiques='" + stat + '\'') : "") +
                '}';
    }

}
