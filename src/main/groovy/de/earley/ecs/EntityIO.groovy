package de.earley.ecs

import de.earley.ecs.core.Entity
import de.earley.ecs.util.EntityDataDSL
import org.codehaus.groovy.control.CompilerConfiguration
/**
 * Created 23/03/16
 * @author Timothy Earley
 *
 * Handles loading scripts into entities
 *
 */
class EntityIO {

	private static final config = new CompilerConfiguration(scriptBaseClass: EntityDataDSL.name)
	private static final shell = new GroovyShell(config)

	/**
	 * Runs all files as groovy entities
	 * @param files to be run
	 * @return entites loaded
	 */
	public static Map<String, Entity> loadScript(File... files) {

		def entities = [:]

		shell.setVariable("entities", entities)
		for (file in files) {
			shell.evaluate( file )
		}

		return entities

	}

	/**
	 * Write the object to the file
	 * @param file which file to write to
	 * @param object needs to be serializable
	 */
	private static void write(File file, object) {
		def oos = new ObjectOutputStream( new FileOutputStream( file ) )
		oos.writeObject(object)
		oos.close()
	}

	/**
	 * Reads the file as an object
	 * @param file the file
	 * @return the object
	 */
	private static read(File file) {
		def ois = new ObjectInputStream( new FileInputStream( file ))
		def data = ois.readObject()
		ois.close()
		return data
	}

	/**
	 * Saves all entities in the file
	 * @param savefile the savefile
	 * @param entities as an array or vargs (saved as array)
	 */
	public static void saveAll(File savefile, Entity... entities) {
		write(savefile, entities)
	}

	/**
	 * Saves a single entity
	 * @param savefile where to save
	 * @param entity what to save
	 */
	public static void save(File savefile, Entity entity) {
		write(savefile, entity)
	}

	/**
	 * Loads the file as an entity array
	 * @param file the savefile
	 * @return the entity array
	 */
	public static Entity[] loadAll(File file) {
		(Entity[]) read(file)
	}

	/**
	 * Loads a single entity
	 * @param file the savefile
	 * @return the loaded entity
	 */
	public static Entity load(File file) {
		(Entity) read(file)
	}

}
