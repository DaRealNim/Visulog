package up.visulog.analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

public class DummyPlugin3 extends Plugin {
	private Result result;

	DummyPlugin3(Configuration configuration) {
		super(configuration);
	}

	public Result getString(InputStream i) throws IOException {
		String s;
		List<String> l=new ArrayList<String>();
        BufferedReader r = new BufferedReader(new InputStreamReader(i));
		while((s=r.readLine())!=null) {
			l.add(s);
		}
		Result res=new Result();
		res.inputStream=l;
		return res;
	}
	
	public void run() {
		try {
			result=getString(Commit.parseFromCommand(configuration.getGitPath(),"log", null));
			System.out.print("ça fonctionn");
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
	            for (String s : inputStream) {
	                html.append("<li>").append(s).append("</li>");
	            }
	            html.append("</ul></div>");
	            return html.toString();
	            }
	}
}
