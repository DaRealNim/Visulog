package up.visulog.analyzer;

import java.util.ArrayList;
import java.util.List;

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
		List<String> authorsDone=new ArrayList<String>();
		for(Commit i : gitLog) {
			if(authorsDone.isEmpty() || !authorsDone.contains(i.author)) {
				List<Commit> authorCommits=new ArrayList<Commit>();
				authorCommits=getCommitForThisAuthor(gitLog,i.author);
				authorsDone.add(i.author);
				timeAverage(tabTimeAverage(authorCommits)); // à stocker dans result;
			}
		}
		return result;
	}

	//get in a list all the author's commits
	public List<Commit> getCommitForThisAuthor(List<Commit> gitLog, String name) {
		List<Commit> authorCommits=new ArrayList<Commit>();
		for(Commit i : gitLog) {
			if(i.author.equals(name)) authorCommits.add(i);
		}
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

	//return a double[] of frequency between two commits to help timeAverage
	public double[] tabTimeAverage(List<Commit> authorCommits) {// final name will maybe be different
		double[] tab;
		int size=authorCommits.size();
		if(authorCommits.size()==1) {
			tab=new double[1];
			tab[0]=0;
		}
		tab = new double[authorCommits.size()/2];
		int index=0;
		for(int i=0;i<authorCommits.size();i++) {
			if(authorCommits.get(i)!=authorCommits.get(authorCommits.size()-1)) {
			tab[index] = timeBetweenTwoCommits(authorCommits.get(i),authorCommits.get(i+1));
			index++;
		}
		}
		return tab;
	}

	//TODO 4: calcul de la période entre deux commits(en jour)
	public double timeBetweenTwoCommits(Commit a, Commit b) {
		double time = 0;
		return time;
	}
	
	@Override
	public void run() {
		//result = TODO 1
	}

	@Override
	public Result getResult() {
		return null;
	}

}
