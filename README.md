# cljfmt-cli

A NodeJS CLI to reformat your Clojure(Script) code using [cljfmt](https://github.com/weavejester/cljfmt).

## Installation

Here is a one-liner to install cljfmt to `/usr/local/bin`:

```bash
bash -c 'sudo wget -O /usr/local/bin/cljfmt https://github.com/ComputeSoftware/cljfmt-cli/releases/download/v1.0/cljfmt && sudo chmod +x /usr/local/bin/cljfmt'
```


### Installation from source
You will need [Boot](https://github.com/boot-clj/boot) installed on your system. Clone this repo and run this command:

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
4. Input a name for your external tool. We'll use cljfmt.
5. Set program to the location that you installed `cljfmt` to. If `cljfmt` is on your path then you can simply use `cljfmt`.
6. Set parameters to `--fix $FilePath$`.

To run the external tool, navigate to *Tools > External Tools > cljfmt*. This will run the formatter on your currently
selected file. 

You can take this one step further and bind the formatter to some keys. To do this, navigate to
*File > Settings > Keymap*. Then go to *External Tools > External Tools* and find the name of your external tool. 
Double click it and select "Add Keyboard Shortcut."