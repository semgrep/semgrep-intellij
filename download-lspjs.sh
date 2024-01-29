#!/usr/bin/env bash
export DOWNLOAD_DIR="lspjs"
export LSPJS_VERSION=$(cat ./semgrep-version)
export VSCODE_LANGUAGE_SERVER_VERSION="9.0.1"
set -eu
# Check if lspjs exists and if its a symlink then exit
if [ -L $DOWNLOAD_DIR ]; then
    echo "lspjs symlink exists, not downloading as you are most likely using a local version"
    exit 0
fi
mkdir -p lspjs/dist
for var in "$@"
do
    curl https://static.semgrep.dev/static/turbo/$LSPJS_VERSION/language_server/dist/$var -o ./$DOWNLOAD_DIR/dist/$var
done

cd $DOWNLOAD_DIR/dist
# check if package.json exists
if [ ! -f package.json ]; then
    echo "package.json does not exist, creating one"
    npm init -y
    npm install vscode-languageserver@$VSCODE_LANGUAGE_SERVER_VERSION
fi