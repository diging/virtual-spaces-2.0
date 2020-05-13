#!/usr/bin/env bash

echo $TOMCAT_USERNAME

sed -i 's/<pw-change-me>/'"$TOMCAT_PASSWORD"'/g' /usr/local/tomcat/conf/tomcat-users.xml
sed -i 's/<username-change-me>/'"$TOMCAT_USERNAME"'/g' /usr/local/tomcat/conf/tomcat-users.xml

/usr/local/tomcat/bin/catalina.sh run
