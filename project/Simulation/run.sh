#!/usr/bin/env sh
set -eu
cd "$(dirname "$0")"
if ! command -v javac >/dev/null 2>&1; then
  echo "ERROR: javac was not found. Install a JDK and add it to PATH." >&2
  exit 1
fi
mkdir -p bin
printf '%s\n' 'Compiling traffic simulation...'
javac -encoding UTF-8 -d bin src/*.java
printf '%s\n' 'Starting traffic simulation...'
exec java -cp bin App
