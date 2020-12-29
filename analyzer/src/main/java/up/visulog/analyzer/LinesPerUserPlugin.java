package up.visulog.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.Webgen;
import java.awt.Color;
import java.util.Arrays;

public class LinesPerUserPlugin extends Plugin{
	private Result result;

	public LinesPerUserPlugin(Configuration generalConfiguration) {
		super(generalConfiguration);
	}


	public Result linesPerUser(List<Commit> gitLog) {
		var result = new Result();
		List<String> authorsList = new ArrayList<String>(); //list of authors who has commits
		for(Commit i : gitLog) {
			if(authorsList.isEmpty() || !authorsList.contains(i.author)) {
				//create List<Commit> for this author
				List<Commit> authorCommits = getCommitForThisAuthor(gitLog,i.author);
				authorsList.add(i.author);

				//stock in result
				String lines = getLinesAdded(authorCommits)+" / "+getLinesDeleted(authorCommits);
				result.linesAddedDeleted.put(i.author,lines);

			}

		}
		return result;
	}

	//get in a list all the author's commits
	public List<Commit> getCommitForThisAuthor(List<Commit> gitLog, String name) {
		List<Commit> authorCommits = new ArrayList<Commit>();
		for(Commit i : gitLog) {
			if(i.author.equals(name)) authorCommits.add(i);
		}
		return authorCommits;
	}

	//return number of lines added in authorCommits(all commits of one author)
	public int getLinesAdded(List<Commit> authorCommits) {
		int added = 0;
		String s = "changed, ";
		for(Commit commit : authorCommits) {
			if(commit.stat != null) {
				String statistics = commit.stat;
				int posStart = statistics.indexOf(s);
				int posEnd = statistics.indexOf("insertions(+)");
				if(posEnd == -1) posEnd = statistics.indexOf("insertion(+)"); //insertion without s if just one line added
				if(posStart != -1 && posEnd != -1) {
					added += Integer.valueOf(statistics.substring((posStart+s.length()),(posEnd-1)));
				}
			}
		}
		return added;
	}

	//return number of lines deleted in authorCommits(all commits of one author)
	public int getLinesDeleted(List<Commit> authorCommits) {
		int deleted = 0;
		for(Commit commit : authorCommits) {
			if(commit.stat != null) {
				String s = "(+), "; //if there are insertions
				String statistics = commit.stat;
				int posStart = statistics.indexOf(s);
				if(posStart == -1) { //there is no insertions, only deletions
					s = "changed, ";
					posStart = statistics.indexOf(s); //if there are insertion before
				}
				int posEnd = statistics.indexOf("deletions(-)");
				if(posEnd == -1) posEnd = statistics.indexOf("deletion(-)"); //deletion without s if juste one line deleted
				if(posStart != -1 && posEnd != -1) {
					deleted += Integer.valueOf(statistics.substring((posStart+s.length()),(posEnd-1)));
				}
			}
		}
		return deleted;
	}


	@Override
	public void run() {
		result = linesPerUser(Commit.parseLogFromCommand(configuration.getGitPath()));
	}

	@Override
    public Result getResult() {
        if (result == null)
            run();
        return result;
    }



	static class Result implements AnalyzerPlugin.Result{
		private final Map<String, String> linesAddedDeleted = new HashMap();

		@Override
		public String getResultAsString() {
			return linesAddedDeleted.toString();
		}

		@Override
		public String getResultAsHtmlDiv() {
			StringBuilder html = new StringBuilder("<div>Number of lines added/deleted per author: <ul>");
            for (var item : linesAddedDeleted.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
		}

		public Webgen.Graph[] getResultAsGraphArray() {
            ArrayList<String> labels = new ArrayList<String>();
            ArrayList<Integer> data = new ArrayList<Integer>();
			ArrayList<Integer> data2 = new ArrayList<Integer>();
            for (var item : linesAddedDeleted.entrySet()) {
                labels.add(item.getKey());
                data.add(Integer.valueOf(item.getValue().split("/")[0].replace(" ", "")));
				data2.add(Integer.valueOf(item.getValue().split("/")[1].replace(" ", "")));
            }
            String[] labelsArray = new String[labels.size()];
            int[] dataArray = new int[data.size()];
			int[] dataArray2 = new int[data2.size()];
            for(int i=0; i<labelsArray.length; i++) labelsArray[i] = labels.get(i);
            for(int i=0; i<dataArray.length; i++) dataArray[i] = data.get(i);
			for(int i=0; i<dataArray2.length; i++) dataArray2[i] = data2.get(i);
			Color[] c1 = new Color[dataArray.length];
			Color[] c2 = new Color[dataArray.length];
			Arrays.fill(c1, new Color(0, 255, 0));
			Arrays.fill(c2, new Color(255, 0, 0));
            return new Webgen.Graph[]{new Webgen.BarGraph("Lines per User", labelsArray, new String[]{"Lines added", "Lines deleted"}, new int[][]{dataArray, dataArray2}, new Color[][]{c1, c2})};
        }

		@Override
	    public String getDisplayName() {
	        return "Lines per user";
	    }

	}
}
