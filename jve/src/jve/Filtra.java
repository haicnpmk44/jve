package jve;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import org.ourgrid.mygrid.scheduler.JobNotFoundException;
import org.ourgrid.mygrid.ui.ConcreteUIServices;
import org.ourgrid.mygrid.ui.CouldNotAddJobException;
import org.ourgrid.mygrid.ui.MyGridSetter;
import org.ourgrid.mygrid.ui.UIServices;
import org.ourgrid.specs.IOBlock;
import org.ourgrid.specs.IOEntry;
import org.ourgrid.specs.JobSpec;
import org.ourgrid.specs.JobSpecificationException;
import org.ourgrid.specs.TaskSpec;
import org.ourgrid.specs.TaskSpecificationException;
import org.ourgrid.specs.main.CompilerException;

import engine.ui.text.KeyboardReader;

/**
 * Programa pra gridificar a filtragem de um video.
 */
public class Filtra {
	public static void main(String[] args) {
		if(args.length < 3){
			System.err.println("Uso: java " +
					Filtra.class.getName() +
					" [grid <gdf>] <filtro1> <input1> <output1> [<filtro2> <input2> <output2>] ...");
			System.exit(1);
		}
		
		try {
			rodaVarios(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void rodaUm(String filtro, String input, String output) throws Exception {
		// ex.: 2grayScale teste3.avi teste3_saida.avi
		KeyboardReader kbr = new KeyboardReader();
		kbr.runLine("new filter "+filtro+" filtro");
		kbr.runLine("apply -f filtro -input "+input+" -output "+output);
		kbr.runLine("exit");
	}

	private static void rodaVarios(String[] args) throws Exception {
		if(args[0].equals("grid")){
			rodaVariosEmGrid(args);
		} else {
			rodaVariosSemGrid(args);
		}
	}

	private static void rodaVariosSemGrid(String[] args) throws Exception {
		int i = 0;
		while(i+2 < args.length){
			String filtro = args[i+0];
			String input = args[i+1];
			String output = args[i+2];
			
			rodaUm(filtro, input, output);
		}
	}
	
	private static void rodaVariosEmGrid(String[] args) throws Exception {
		//args[0] == "grid"
		//args[1] == <grid description file>
		String gdf = args[1];
		
		ArrayList<JVETask> tasks = new ArrayList<JVETask>();
		int i = 2;
		while(i+2 < args.length){
			String filtro = args[i+0];
			String input = args[i+1];
			String output = args[i+2];
			
			tasks.add( criaJVETask(filtro, input, output) );
		}
		rodaCadaUmEmGrid(gdf, tasks);
	}

	private static JVETask criaJVETask(String filtro, String input, String output) {
		JVETask t = new JVETask();
		t.filtro = filtro;
		t.input = input;
		t.output = output;
		return t;
	}

	private static void rodaCadaUmEmGrid(String gdf, ArrayList<JVETask> tasks) throws Exception {
		//set the grid
		try {
			MyGridSetter gridSetter = new MyGridSetter();
			gridSetter.setGrid(gdf);
		} catch (RemoteException re) {
			throw new Exception("Could not contact MyGrid!", re);
		} catch (CompilerException ce) {
			throw new Exception("Could not set the grid!", ce);
		}
		
		//create job
		try {
			JobSpec rodaTudoJob = criaRodaTudoJob(tasks);
			addAndWaitForJob(rodaTudoJob);
		} catch (IOException ioe) {
			throw new Exception("Could not create encode job spec!", ioe);
		} catch (JobSpecificationException e) {
			throw new Exception("Problems with the encode Job Specification!", e);
		}
	}
	
	private static void addAndWaitForJob(JobSpec aJobSpec) throws Exception {
		UIServices services = ConcreteUIServices.getInstance();
		try {
			int jobId = services.addJob(aJobSpec);
			services.waitForJob(jobId);
		} catch (CouldNotAddJobException cnaje) {
			throw new Exception("Could not add the Job Spec!", cnaje);
		} catch (JobNotFoundException jnfe) {
			throw new Exception("Could not wait for the Job Spec!", jnfe);
		} catch (RemoteException re) {
			throw new Exception("Could not contact MyGrid!", re);
		}
	}
	
	private static JobSpec criaRodaTudoJob(ArrayList<JVETask> tasksForTheJob) throws IOException, JobSpecificationException {
		JobSpec job = new JobSpec("JVE encoder");
		
		ArrayList<TaskSpec> tasks = new ArrayList<TaskSpec>();
		
		//encode videos tasks
		for (int i=0; i<tasksForTheJob.size(); i++) {
			//create task and send
			tasks.add(criaRodaUmFiltroTask(tasksForTheJob.get(i)));
		}
		
		job.setTaskSpecs(tasks);
		return job;
	}
	
	private static TaskSpec criaRodaUmFiltroTask(JVETask task) {
		IOBlock initBlock = new IOBlock();
		initBlock.putEntry(new IOEntry("PUT", task.input, "jve-video.input-$TASK"));
		initBlock.putEntry(new IOEntry("STORE", task.input, "jve.jar"));
		
		IOBlock finalBlock = new IOBlock();
		finalBlock.putEntry(new IOEntry("GET", "jve-video.output-$TASK", task.output));
		
		String remoteScript = "nice java -classpath=$STORAGE/jve.jar " +
		Filtra.class.getName()+" " +
		task.filtro +
		" $PLAYPEN/jve-video.input-$TASK " +
		" -o $PLAYPEN/jve-video.output-$TASK";
		
		TaskSpec taskSpec = null;
		try {
			taskSpec = new TaskSpec(initBlock, remoteScript, finalBlock);
		} catch (TaskSpecificationException e) {
			//should never be thrown
			System.out.println("This shouldn't happen. Error code: KNHKUHKSD");
			e.printStackTrace();
		}
		return taskSpec;
	}
	
	private static class JVETask {
		public String output;
		public String input;
		public String filtro;
	}
}