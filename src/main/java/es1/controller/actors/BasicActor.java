package controller.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public abstract class BasicActor extends AbstractActor {

	private final int id;
	private final ActorRef referee;

	protected BasicActor(int id,ActorRef referee) {
		super();
		this.id = id;
		this.referee = referee;
	}
	
	protected void logDebug(String msg) {
		// System.out.println("[ " + getName() +"] " + msg);
	}

}
