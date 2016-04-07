package de.earley.ecs.observer
/**
 *
 * Helper class to combine setChanged and notifyObservers into one function
 *
 * Created 24/03/16
 * @author Timothy Earley
 */
class Subject extends Observable {

	@Override
	void notifyObservers(Object arg) {
		setChanged()
		super.notifyObservers(arg)
	}
}
