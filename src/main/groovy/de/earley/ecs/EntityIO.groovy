package de.earley.ecs

import de.earley.ecs.core.Entity
import de.earley.ecs.util.EntityDataDSL
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
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
	* Converts the path to a file/directory
	* @param filepath must be on the classpath
	* @return the file
	*/
	private static File pathToFile(String filepath) {
		def uri = ClassLoader.getSystemClassLoader().getResource(filepath)?.toURI()
		if (!uri) throw new IOException("The file $filepath could not be found.")
		return new File ( uri )
	}

	/**
	* Converts the filenames to files and runs the scripts
	* @param file paths
	* @returns entities loaded
	*/
	public static Map<String, Entity> loadScript(String... filepaths) {
		loadScript ( (File[]) filepaths.collect { pathToFile it }.toArray() )
	}

	/**
	 * Runs all files as groovy entities
	 * @param files to be run
	 * @return entites loaded
	 */
	public static Map<String, Entity> loadScript(File... files) {

		def entities = [:]

		shell.setVariable("entities", entities)
		for (file in files) {
			shell.evaluate(file)
		}

		return entities

	}

	/**
	* Loads all scripts in the directoy
	* @param dir the directoy as a filepath
	*  @return entites loaded
	*/
	public static Map<String, Entity> loadScriptDirectory(String dirname) {
		loadScriptDirectory ( pathToFile ( dirname ) )
	}

	/**
	* Loads all scripts in the directoy
	* @param dir the directoy
	*  @return entites loaded
	*/
	public static Map<String, Entity> loadScriptDirectory(File dir) {
		loadScript ( dir.listFiles() )
	}

	/**
	 * Write the object to the file
	 * @param file which file to write to
	 * @param object needs to be serializable
	 */
	private static void write(File file, object) {
		def oos = new ObjectOutputStream(new FileOutputStream(file))
		oos.writeObject(object)
		oos.close()
	}

	/**
	 * Reads the file as an object
	 * @param file the file
	 * @return the object
	 */
	private static read(File file) {
		def ois = new ObjectInputStream(new FileInputStream(file))
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

	public static void saveJSON(File file, List<Entity> e, boolean pretty = true) {
		def json = JsonOutput.toJson(e)
		file.write pretty ? JsonOutput.prettyPrint(json) : json
	}

	public static List<Entity> loadJSON(File file) {
		def list = new JsonSlurper().parse(file)
		def entities = []
		// since uuid does not get deserialized properly, ie. cast from string does not work, separate it.
		list.each {
			Map map ->
				def id = map.remove('id')
				entities << new Entity(map).with {setId(UUID.fromString(id)); delegate}
		}
		entities
	}

}
