<!-- Plugin description -->

# Semgrep Plugin for IntelliJ-based IDEs

## Prerequisites

Semgrep IntelliJ Plugin communicates with Semgrep command-line interface (CLI) to run scans. Install Semgrep CLI before
you can use the IntelliJ Plugin. To install Semgrep CLI:

```sh
# For macOS
$ brew install semgrep

# For Ubuntu/WSL/Linux/macOS
$ python3 -m pip install semgrep

# To try Semgrep without installation run via Docker
$ docker run --rm -v "${PWD}:/src" returntocorp/semgrep semgrep
```

## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "
  semgrep-intellij"</kbd> >
  <kbd>Install</kbd>

- Manually:

  Download the [latest release](https://github.com/returntocorp/semgrep-intellij/releases/latest) and install it
  manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Use full potential of Semgrep

Try Autofix.

Add and update new rules to expand Semgrep extension capabilities.

You can fine-tune and customize rules to improve your scan results:

1. Go to [Semgrep Registry](https://semgrep.dev/explore). Ensure that you are signed in.
1. Explore the Semgrep Registry, select a rule, and then click **Add to Rule Board**.
1. Manage rules in the [Policies](https://semgrep.dev/orgs/-/board) page.

### Language support

Semgrep supports 30+ languages.

| Category     | Languages                                                                                                                                                                     |
|--------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| GA           | C# · Go · Java · JavaScript · JSX · Kotlin · JSON · PHP · Python · Ruby · Scala · Terraform · TypeScript                                                                      |
| Beta         | Rust                                                                                                                                                                          |
| Experimental | Bash · C · C++ · Clojure · Dart · Dockerfile · Elixir · HTML · Julia · Jsonnet · Lisp · Lua · OCaml · R · Scheme · Solidity · Swift · YAML · XML · Generic (ERB, Jinja, etc.) |

## Support

If you need our support, join the [Semgrep community Slack workspace](https://go.semgrep.dev/slack) and tell us about
any problems you encountered.

<!-- Plugin description end -->
