package tk.tomby.tedit.plugins.ant;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.helper.DefaultExecutor;
import org.apache.tools.ant.helper.ProjectHelperImpl;

public class AntProcessor {
	
	private ProjectHelper helper = new ProjectHelperImpl();
	
	public Project process(File file) {
		Project project = new Project();
		project.init();
		helper.parse(project, file);
		return project;
	}
	
	public void execute(Project project, String[] targets) {
		DefaultExecutor exec = new DefaultExecutor();
		exec.executeTargets(project, targets);
	}

}
