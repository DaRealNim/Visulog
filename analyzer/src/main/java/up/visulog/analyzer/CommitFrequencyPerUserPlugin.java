package up.visulog.analyzer;

import up.visulog.config.Configuration;

public class CommitFrequencyPerUserPlugin extends Plugin {

	private Result result;
	
	public CommitFrequencyPerUserPlugin(Configuration generalConfiguration) {
		super(generalConfiguration);
	}
	
	//TODO 1(globale, renvoit le result de la méthode run): récupérer tous les commits dans une List<Commit>,
	        //puis parcourir la List<Commit> pour appeler d'autres méthodes aux(calcul période) pour chaque auteur
	
	//TODO 2: calcul de la moyenne de toutes les périodes pour chaque personne
	
	//TODO 3: renvoit tableau de toutes les périodes par personne(pour permettre le todo 2)
	
	//TODO 4: calcul de la période entre deux commits(en jour)
	
	@Override
	public void run() {
		//result = TODO 1
	}

	@Override
	public Result getResult() {
		return null;
	}
}
