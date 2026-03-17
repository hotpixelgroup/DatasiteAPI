#!/bin/sh
# Maven Wrapper script
set -e

MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$(cd "$(dirname "$0")" && pwd)}"
MAVEN_WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
MAVEN_WRAPPER_PROPERTIES="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"

if [ -f "$MAVEN_WRAPPER_PROPERTIES" ]; then
    MAVEN_DIST_URL=$(grep 'distributionUrl' "$MAVEN_WRAPPER_PROPERTIES" | cut -d'=' -f2-)
fi

MAVEN_HOME="$HOME/.m2/wrapper/dists/$(basename "$MAVEN_DIST_URL" .zip)"

if [ ! -d "$MAVEN_HOME" ]; then
    echo "Downloading Maven..."
    mkdir -p "$MAVEN_HOME"
    TMPFILE=$(mktemp /tmp/maven-XXXXXX.zip)
    curl -sL "$MAVEN_DIST_URL" -o "$TMPFILE"
    unzip -qo "$TMPFILE" -d "$MAVEN_HOME"
    rm "$TMPFILE"
fi

MAVEN_BIN=$(find "$MAVEN_HOME" -name mvn -type f -path "*/bin/mvn" | head -1)

if [ -z "$MAVEN_BIN" ]; then
    echo "Error: Could not find mvn executable"
    exit 1
fi

exec "$MAVEN_BIN" "$@"
