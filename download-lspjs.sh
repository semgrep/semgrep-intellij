#!/usr/bin/env bash
set -eu
export VSCODE_LANGUAGE_SERVER_VERSION="9.0.1"
# Check if lspjs exists and if its a symlink then exit
if [ -L dist/lspjs ]; then
    echo "lspjs symlink exists, not downloading as you are most likely using a local version"
    exit 0
fi
mkdir -p dist/lspjs
SEMGREP_VERSION=$(cat ./semgrep-version)
echo "Downloading lspjs from S3 for version $SEMGREP_VERSION"
for filename in Main.bc.js language-server-wasm.js semgrep-lsp-bindings.js semgrep-lsp.js
do
    echo "Downloading $filename"
    curl "https://static.semgrep.dev/static/turbo/$SEMGREP_VERSION/language_server/dist/$filename" -o "./dist/lspjs/$filename"
done
echo "Downloaded lspjs"

cd dist/lspjs
# check if package.json exists
if [ ! -f package.json ]; then
    echo "package.json does not exist, creating one"
    npm init -y
fi
npm add vscode-languageserver@$VSCODE_LANGUAGE_SERVER_VERSION
