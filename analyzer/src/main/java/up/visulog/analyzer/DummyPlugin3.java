package up.visulog.analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

public class DummyPlugin3 extends Plugin {
	private Result result;

	public DummyPlugin3(Configuration generalConfiguration) {
		super(generalConfiguration);
	}

	public Result getString(BufferedReader r) throws IOException {
		List<String> l=new ArrayList<String>();
		String line;
		var res=new Result();
		while((line=r.readLine())!=null) {
		l.add(line);	
		}
		res.inputStream=l;
		return res;
		
	}
	
	public void run() {
		try {
			result=getString(Commit.parseFromCommand(configuration.getGitPath(),"log", "--stat"));
		} catch (IOException e) {
		System.out.println("erreur");
		}
	}

	
	public  Result getResult() {
		if(result==null) {
			run();
			return result;
		}
		else return result;
	}

static class Result implements AnalyzerPlugin.Result {
	private List<String> inputStream;
		
		public String getResultAsString() {
			return inputStream.toString();
		}
		
		public String getResultAsHtmlDiv() {
			   StringBuilder html = new StringBuilder("<div>commande result: <ul>");
			  for(String s : inputStream) {
				   html.append("<div>").append(s).append("<div>");
			   }
			   html.append("</ul></div>");
	            return html.toString();
	            }
	}
}
