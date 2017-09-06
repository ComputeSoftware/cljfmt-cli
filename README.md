# cljfmt-cli

A NodeJS CLI to reformat your Clojure(Script) code using [cljfmt](https://github.com/weavejester/cljfmt).

## Installation

Right now the only way to install the program is to build it from the source. Luckily,
this is extremely easy. Clone this repo and run this command:

```bash
boot prod-build target
```

This will build the NodeJS script to the target directory. Inside the target directory
you will find a file named `cljfmt`. Let's make this file executable.

```bash
chmod +x target/cljfmt
```

Now place cljfmt somewhere convenient on your system. Often users will place it somewhere
on their PATH.

## Usage in IntelliJ

**Note**: You must have installed the CLI first.

1. Navigate to File > Settings.
2. Navigate to Tools > External Tools.
3. Click green plus (+) button to create a new External Tool.
4. Set program to the location that you installed `cljfmt` to.
5. Set parameters to `--fix $FilePath$`.

Now anytime you run this external tool it will format your currently selected file using
cljfmt.