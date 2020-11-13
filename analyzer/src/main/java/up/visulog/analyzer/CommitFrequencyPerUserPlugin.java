package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

public class CommitFrequencyPerUserPlugin extends Plugin {

	private Result result;
	
	public CommitFrequencyPerUserPlugin(Configuration generalConfiguration) {
		super(generalConfiguration);
	}
	
	//TODO 1(globale, renvoit le result de la méthode run): récupérer tous les commits dans une List<Commit>,
	        //puis parcourir la List<Commit> pour appeler d'autres méthodes aux(calcul période) pour chaque auteur
	
	//return average time between each commit
	public double timeAverage(double[] time) {
		double average = 0;
		for(int i=0; i<time.length; i++) {
			average += time[i];
		}
		return average/time.length;
	}
	
	//TODO 3: renvoit tableau de toutes les périodes par personne(pour permettre le todo 2)
	
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
