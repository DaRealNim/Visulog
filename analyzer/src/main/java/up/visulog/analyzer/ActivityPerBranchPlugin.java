package up.visulog.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.BranchCommits;

public class ActivityPerBranchPlugin extends Plugin {
	private Result result;

	public ActivityPerBranchPlugin(Configuration generalConfiguration) {
		super(generalConfiguration);
	}



	@Override
	public void run() {
		result = new Result(BranchCommits.countCommitsPerBranch(configuration.getGitPath()));
	}

	@Override
	public Result getResult() {
		if (result == null)
			run();
		return result;
	}

	static class Result implements AnalyzerPlugin.Result {
		private final Map<String, Integer> nbCommitsPerBranch = new HashMap<>();

		private Result(List<BranchCommits> branchCommits) {
			for (BranchCommits branchCommit : branchCommits) {
				nbCommitsPerBranch.put(branchCommit.getNomDeLaBranche(), branchCommit.getNbCommits());
			}
		}

		public Map<String, Integer> getNbCommitsPerBranch() {
			return nbCommitsPerBranch;
		}

		@Override
		public String getResultAsString() {
			return nbCommitsPerBranch.toString();
		}

		@Override
		public String getResultAsHtmlDiv() {
			StringBuilder html = new StringBuilder("<div>Number of Commits per branch: <ul>");
			for (var item : nbCommitsPerBranch.entrySet()) {
				html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
			}
			html.append("</ul></div>");
			return html.toString();
		}
	}
}
