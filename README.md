# Fountain Implementation
This is an implementation of the [FountainAPI](https://github.com/FountainMC/FountainAPI).

## Setting up the workspace
There are scripts to decompile Minecraft and apply all the patches needed for editing.

```
$ python3 decompile.py
$ python3 applyPatches.py
```

The generated Minecraft source will be in `src/main/java/net/minecraft/`.

## Compiling
Running `mvn clean package` will generate a runnable jar in `target/`.