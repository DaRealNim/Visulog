package up.visulog.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

public class CommitFrequencyPerUserPlugin extends Plugin {

	private Result result;

	public CommitFrequencyPerUserPlugin(Configuration generalConfiguration) {
		super(generalConfiguration);
	}

	//TODO 1(globale, renvoit le result de la méthode run): récupérer tous les commits dans une List<Commit>,
	//puis parcourir la List<Commit> pour appeler d'autres méthodes aux(calcul période) pour chaque auteur
	public Result frequencyPerUser(List<Commit> gitLog) {
		var result = new Result();
		List<String> authorsDone = new ArrayList<String>(); //list of authors who has commits
		for(Commit i : gitLog) {
			if(authorsDone.isEmpty() || !authorsDone.contains(i.author)) {
				//create List<Commit> for this author
				List<Commit> authorCommits = getCommitForThisAuthor(gitLog,i.author);
				authorsDone.add(i.author);
				
				//stock in result time average for each author
				result.frequencyPerUser.put(i.author, timeAverage(tabTime(authorCommits)));
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
        result.totalCommitsPerUser.put(name, authorCommits.size()); 
		return authorCommits;
	}

	//return average time between each commit
	public double timeAverage(double[] time) {
		double average = 0;
		for(int i=0; i<time.length; i++) {
			average += time[i];
		}
		return average/time.length;
	}

	//return a double[] of each time between two commits, this double[] will be use for timeAverage
	public double[] tabTime(List<Commit> authorCommits) {
		double[] tabTime;
		if(authorCommits.size()==1) {
			//this author has only one commit
			tabTime = new double[1];
			tabTime[0] = 0; //per default if only one commit
		} else {
			tabTime = new double[authorCommits.size()];
			int index = 0;
			for(int i=0; i < authorCommits.size(); i++) {
				if(authorCommits.get(i)!=authorCommits.get(authorCommits.size()-1)) {
					tabTime[index] = timeBetweenTwoCommits(authorCommits.get(i),authorCommits.get(i+1));
					index++;
				}
			}
		}
		return tabTime;
	}

	//TODO 4: calcul de la période entre deux commits(en jour)
	public double timeBetweenTwoCommits(Commit a, Commit b) {
		double time = 0;
		return time;
	}
	
	@Override
	public void run() {
		result = frequencyPerUser(Commit.parseLogFromCommand(configuration.getGitPath()));
	}

	@Override
	public Result getResult() {
		if (result == null)
            run();
        return result;
	}
	
	static class Result implements AnalyzerPlugin.Result {
		
		private final Map<String, Double> frequencyPerUser = new HashMap();
		private final Map<String, Integer> totalCommitsPerUser = new HashMap();
		
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
