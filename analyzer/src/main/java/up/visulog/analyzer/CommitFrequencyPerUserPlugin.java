package up.visulog.analyzer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.Webgen;
import java.awt.Color;

public class CommitFrequencyPerUserPlugin extends Plugin {

	private Result result;

	public CommitFrequencyPerUserPlugin(Configuration generalConfiguration) {
		super(generalConfiguration);
	}

	//return the final result to run() with every users' frequency
	public Result frequencyPerUser(List<Commit> gitLog) {
		var result = new Result();
		List<String> authorsDone = new ArrayList<String>(); //list of authors who has commits
		for(Commit i : gitLog) {
			if(authorsDone.isEmpty() || !authorsDone.contains(i.author)) {
				//create List<Commit> for this author
				List<Commit> authorCommits = getCommitForThisAuthor(gitLog,i.author);
				authorsDone.add(i.author);

				//stock in result time average for each author
				result.frequencyPerUser.put(i.author+" ("+authorCommits.size()+" commits au total)", timeAverage(tabTime(authorCommits)));
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

	//return average time between each commit
	public String timeAverage(double[] time) {
		double somme = 0;
		for(int i=0; i<time.length; i++) {
			somme += time[i];
		}
		double average = (double) somme/time.length;
		DecimalFormat f = new DecimalFormat();
		f.setMaximumFractionDigits(1);
		return f.format(average);
	}

	//return a double[] of each time between two commits, this double[] will be use for timeAverage
	public double[] tabTime(List<Commit> authorCommits) {
		double[] tabTime;
		if(authorCommits.size()==1) {
			//this author has only one commit
			tabTime = new double[1];
			tabTime[0] = 0; //per default if only one commit
		} else {
			tabTime = new double[authorCommits.size()-1];
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

	//calcul de la période entre deux commits(en jour)
	public double timeBetweenTwoCommits(Commit a, Commit b) {
		double time = 0;
		long diff = (a.date).getTime() - (b.date).getTime();
		time = (diff / (1000*60*60*24));
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

		private final Map<String, String> frequencyPerUser = new HashMap();

		@Override
		public String getResultAsString() {
			return frequencyPerUser.toString();
		}

		@Override
		public String getResultAsHtmlDiv() {
			StringBuilder html = new StringBuilder("<div>Temps moyen entre deux commits (en jour): <ul>");
            for (var item : frequencyPerUser.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append(" jours</li>");
            }
            html.append("</ul></div>");
            return html.toString();
		}

		public Webgen.Graph[] getResultAsGraphArray() {
            ArrayList<String> labels = new ArrayList<String>();
            ArrayList<Double> data = new ArrayList<Double>();
            for (var item : frequencyPerUser.entrySet()) {
                labels.add(item.getKey());
                data.add(Double.valueOf(item.getValue().replace(",", ".")));
            }
            String[] labelsArray = new String[labels.size()];
            double[] dataArray = new double[data.size()];
            for(int i=0; i<labelsArray.length; i++) labelsArray[i] = labels.get(i);
            for(int i=0; i<dataArray.length; i++) dataArray[i] = data.get(i);
            return new Webgen.Graph[]{
                new Webgen.BarGraph("<i class=\"far fa-calendar-alt\"></i>","Commit frequency", labelsArray, new String[]{"Time between two commits (days)"}, new double[][]{dataArray}, new Color[][]{Webgen.generateRandomColorArray(dataArray.length)})
            };
        }

		@Override
		public String getDisplayName() {
			return "Time between commits per user";
		}
	}



}
