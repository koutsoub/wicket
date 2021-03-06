#!/bin/sh
#  Licensed to the Apache Software Foundation (ASF) under one or more
#  contributor license agreements.  See the NOTICE file distributed with
#  this work for additional information regarding copyright ownership.
#  The ASF licenses this file to You under the Apache License, Version 2.0
#  (the "License"); you may not use this file except in compliance with
#  the License.  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

set -e

echo "Apache Wicket Release script"
echo "----------------------------"
echo "Building a release for Apache Wicket. We will need the passphrase for"
echo "GPG to sign the release."
echo "This program assumes you use a jdk 1.5 explicitly configured when"
echo "invoking the 'mvn5' Maven 2 command."
echo ""

echo "Enter release version:"
read version

echo "Enter your GPG passphrase (input will be hidden)"
stty_orig=`stty -g` 
stty -echo 
read passphrase
stty $stty_orig

echo "modifying poms with the new version: $version"
find . -name "pom.xml" | xargs sed -i -e "s/1.5-SNAPSHOT/$version/g"
find . -name "pom.xml" | xargs sed -i -e "s/wicket\/trunk/wicket\/releases\/$version/g"

echo "committing changes"
svn commit -m "modified poms for release $version"

# Clear the current NOTICE.txt file
echo "Creating notice file."

NOTICE=NOTICE
> $NOTICE 
echo "Apache Wicket" >> $NOTICE
echo "Copyright 2008 The Apache Software Foundation" >> $NOTICE
echo "" >> $NOTICE
echo "This product includes software developed at" >> $NOTICE
echo "The Apache Software Foundation (http://www.apache.org/)." >> $NOTICE
echo "" >> $NOTICE
echo "This is an aggregated NOTICE file for the Apache Wicket projects included" >> $NOTICE
echo "in this distribution." >> $NOTICE
echo "" >> $NOTICE
echo "NB: DO NOT ADD LICENSES/NOTICES/ATTRIBUTIONS TO THIS FILE, BUT IN THE" >> $NOTICE
echo "    NOTICE FILE OF THE CORRESPONDING PROJECT. THE RELEASE PROCEDURE WILL" >> $NOTICE
echo "    AUTOMATICALLY INCLUDE THE NOTICE IN THIS FILE." >> $NOTICE
echo "" >> $NOTICE

# next concatenate all NOTICE files from sub projects to the root file
for i in `find . -name "NOTICE" -not -regex ".*/target/.*" -not -regex "./NOTICE"`
do
	echo "---------------------------------------------------------------------------" >> $NOTICE
	echo "src/"$i | sed -e "s/\/src.*//g" >> $NOTICE
	echo "---------------------------------------------------------------------------" >> $NOTICE
	cat $i >> $NOTICE
	echo >> $NOTICE
done

# prebuilding to work around javadoc generation problem
mvn clean install -Dmaven.test.skip=true
mvn javadoc:jar

# clean all projects
echo "Clean all projects"
mvn5 clean -Pall

# package and assemble the release
echo "Package and assemble the release"
mvn5 -ff -Dgpg.passphrase="$passphrase" -Prelease deploy javadoc:javadoc assembly:attached $1

filename=`ls target/dist/apache-wicket*gz`
gpg --print-md MD5 $filename > $filename.md5
gpg --print-md SHA1 $filename > $filename.sha
echo "$passphrase" | gpg --passphrase-fd 0 --armor --output $filename.asc --detach-sig $filename

filename=`ls target/dist/apache-wicket*zip`
gpg --print-md MD5 $filename > $filename.md5
gpg --print-md SHA1 $filename > $filename.sha
echo "$passphrase" | gpg --passphrase-fd 0 --armor --output $filename.asc --detach-sig $filename

echo "Uploading release"
svn export http://svn.apache.org/repos/asf/wicket/common/KEYS target/dist/KEYS
ssh people.apache.org mkdir public_html/wicket-$version
scp -r target/dist target/m2-repo people.apache.org:public_html/wicket-$version

