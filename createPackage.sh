#!/bin/bash

export DEPENDENCY_DIR=target/dependency
export PACKAGE_DIR=package
export JAR=target/sf-data-dictionary-1.0-SNAPSHOT.jar
export DATA_FILES=datafiles

# clean previous
rm -r $PACKAGE_DIR
mkdir $PACKAGE_DIR

# copy required files
cp -rf $DEPENDENCY_DIR $PACKAGE_DIR
cp $JAR $PACKAGE_DIR/dependency
cp createDictionary.* $PACKAGE_DIR
cp -rf $DATA_FILES $PACKAGE_DIR
cp app.properties $PACKAGE_DIR

# set executable permissions
cd $PACKAGE_DIR
chmod 755 createDictionary.sh

echo 'FINISHED PACKAGE CREATE'
