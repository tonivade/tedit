package tk.tomby.tedit.plugins.ant;

import java.io.File;

import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.helper.DefaultExecutor;
import org.apache.tools.ant.helper.ProjectHelperImpl;

import tk.tomby.tedit.services.TaskManager;

public class AntProcessor {
	
	private ProjectHelper helper = new ProjectHelperImpl();
	
	public Project process(File file) {
		Project project = new Project();
		project.init();
		helper.parse(project, file);
		return project;
	}
	
	public void execute(final Project project, final String[] targets) {
		Runnable work = new Runnable() {
			public void run() {
				DefaultLogger logger = new DefaultLogger();
				project.addBuildListener(logger);
				
				logger.setOutputPrintStream(System.out);
				logger.setErrorPrintStream(System.err);
				
				logger.setMessageOutputLevel(Project.MSG_INFO);
				
				DefaultExecutor exec = new DefaultExecutor();
				exec.executeTargets(project, targets);
				
				project.removeBuildListener(logger);
			}
		};
		TaskManager.execute(work);
	}

}
