package com.ebay.build.profiler.lifecycle;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.eventspy.AbstractEventSpy;
import org.apache.maven.execution.ExecutionEvent;

import com.ebay.build.profiler.profile.DiscoveryProfile;
import com.ebay.build.profiler.profile.MojoProfile;
import com.ebay.build.profiler.profile.PhaseProfile;
import com.ebay.build.profiler.profile.ProjectProfile;
import com.ebay.build.profiler.profile.SessionProfile;
import com.ebay.build.profiler.render.OutputRenderer;

/**
 * MavenLifecycleProfiler will profile the maven build.
 * 
 * @author kmuralidharan
 * @author mmao
 */

@Named
@Singleton
public class MavenLifecycleProfiler extends AbstractEventSpy {

	private DiscoveryProfile discoveryProfile;
	private SessionProfile sessionProfile;
	private ProjectProfile projectProfile;
	private PhaseProfile phaseProfile;
	private MojoProfile mojoProfile;
	private Context context; 
	
	@Override
	public void init(Context context) throws Exception {
		System.out.println( " __  __" );
		System.out.println( "|  \\/  |__ _Apache__ ___" );
		System.out.println( "| |\\/| / _` \\ V / -_) ' \\  ~ eBay Raptor Build Profiling ~" );
		System.out.println( "|_|  |_\\__,_|\\_/\\___|_||_|");
		System.out.println( "" );

		this.context = context;
	}

	@Override
	public void onEvent(Object event) throws Exception {
		if (event instanceof ExecutionEvent) {

			ExecutionEvent executionEvent = (ExecutionEvent) event;

			if (executionEvent.getType() == ExecutionEvent.Type.ProjectDiscoveryStarted) {
				discoveryProfile = new DiscoveryProfile(context, executionEvent);
				
			} else if (executionEvent.getType() == ExecutionEvent.Type.SessionStarted) {
				sessionProfile = new SessionProfile(executionEvent);
			} else if (executionEvent.getType() == ExecutionEvent.Type.SessionEnded) {
				projectProfile.addPhaseProfile(phaseProfile);
				sessionProfile.stop();
				discoveryProfile.stop();
				OutputRenderer renderer = new OutputRenderer(sessionProfile, discoveryProfile);
				renderer.renderToScreen();
				//renderer.renderToJSON();
			} else if (executionEvent.getType() == ExecutionEvent.Type.ProjectStarted) {
				projectProfile = new ProjectProfile(executionEvent.getProject(),executionEvent);
			} else if (executionEvent.getType() == ExecutionEvent.Type.ProjectSucceeded
					|| executionEvent.getType() == ExecutionEvent.Type.ProjectFailed) {
				phaseProfile.stop();
				projectProfile.stop();
				sessionProfile.addProjectProfile(projectProfile);
			} else if (executionEvent.getType() == ExecutionEvent.Type.MojoStarted) {
				String phase = executionEvent.getMojoExecution()
						.getLifecyclePhase();
				if (phaseProfile == null) {
					phaseProfile = new PhaseProfile(phase,executionEvent);
				} else if (!phaseProfile.getPhase().equals(phase)) {
					phaseProfile.stop();
					projectProfile.addPhaseProfile(phaseProfile);
					phaseProfile = new PhaseProfile(phase,executionEvent);
				}
				mojoProfile = new MojoProfile(executionEvent.getMojoExecution(),executionEvent);
			} else if (executionEvent.getType() == ExecutionEvent.Type.MojoSucceeded
					|| executionEvent.getType() == ExecutionEvent.Type.MojoFailed) {
				mojoProfile.stop();
				phaseProfile.addMojoProfile(mojoProfile);
			}
		}
	}

	@Override
	public void close() throws Exception {

	}
}
