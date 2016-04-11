# Groovy_ECS
Entity Component System written in Groovy with the main focus on creating a simple DSL.
This allows users to create entities with arbitrary attributes which are stored in nested lists, as well as extending entities with more properties (inheritence).
The data and logic are completely seperated with data declared in the afformentioned DSL and the components (logic) being plugged into the System. These components are then given all entities on each update to manipluate to their liking.

##DSL

```groovy
def sword = [
		damage: 1,
		range: false,
]

create Zombie: [
				type: "zombie",
				health: 20,
				weapon: sword
		]

"Zombie".extend "Advanced Zombie": [
				weapon: [
						damage: sword.damage * 2
				]
		]
```

##Components

```groovy
def ecs = new EntityComponentSystem()

// Runs every update
ecs.addComponent {
  entities -> entities.each { println it }
}

// Runs on entity spawn
ecs.onSpawn.addObserver {
  observer, entitiy -> println "$entity has spawned"
}

// Runs on entity removed
ecs.onRemove.addObserver {
	observer, entitiy -> println "Removed $entity"
}
```

##Add to gradle

Using jitpack.io, simply add the repository and dependency

```gradle
repositories {
	maven { url "https://jitpack.io" }
}

dependencies {
	compile 'com.github.TimothyEarley:Groovy-Entity-Component-System:master-SNAPSHOT'
}
```
