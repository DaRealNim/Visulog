package up.visulog.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import up.visulog.analyzer.CountCommitsPerAuthorPlugin.Result;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

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
				var nb = result.linesAdded.getOrDefault(i.author, 0);
	            result.linesAdded.put(i.author, nb + getLinesAdded(i));
				
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
	
	//return number of lines added in Commit commit
	public int getLinesAdded(Commit commit) {
		return 0;
	}
	
	//return number of lines deleted in Commit commit
		public int getLinesDeleted(Commit commit) {
			return 0;
		}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
    public Result getResult() {
        if (result == null)
            run();
        return result;
    }
	
	static class Result implements AnalyzerPlugin.Result{
		private final Map<String, Integer> linesAdded = new HashMap();
		private final Map<String, Integer> linesDeleted = new HashMap();

		@Override
		public String getResultAsString() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getResultAsHtmlDiv() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
