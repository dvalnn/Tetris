## Getting Started

Welcome to the our project repository. We are building a java version of a Tetris Attack style game.

As of right now there are no released, so if you want to check out our current progress you need to 
download and compile the source code for yourself.
Here is how you can do it:

### Compiling using the Maven build tools:

#### Installing the Maven toolset

 - Arch linux: 
 ```
 sudo pacman -S maven
 ```
 - Debian based linux distributions:
 ```
 sudo apt install maven
 ```
 - Other systems:

   Download and install the Apache Maven toolset and all of its system requirements from [this download page](https://maven.apache.org/download.cgi)

#### Compiling and Running the game:
- Clone the repository's main (github) or master (gitlab) branch
- Go to the project root folder run the following commands:

```bash
mvn compile
mvn exec:java
```
The game should start up immediately after maven finished the build tasks.

#### Compiling and running Unit Testing

```bash
mvn compile
mvn test
```
The game tests should run immediately  after maven finished the build tasks.


## Controls:

- Move piece: directional arrows
- Hard Drop: space
- Rotate piece: z/x

### Changing keybinds:

All keybinds can be costumized in the game via settings menu