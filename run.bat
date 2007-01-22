@echo off
set TEDIT_HOME=D:\programas\tedit-0.1.1

set CLASSPATH=%JAVA_HOME%\lib\tools.jar;%TEDIT_HOME%\config

set CLASSPATH=%CLASSPATH%;%TEDIT_HOME%\main\tEdit-0.1.1.jar
set CLASSPATH=%CLASSPATH%;%TEDIT_HOME%\main\tEdit-resources-0.1.1.jar
set CLASSPATH=%CLASSPATH%;%TEDIT_HOME%\main\tEdit-scripts-0.1.1.jar

set CLASSPATH=%CLASSPATH%;%TEDIT_HOME%\lib\commons-beanutils-1.7.0.jar
set CLASSPATH=%CLASSPATH%;%TEDIT_HOME%\lib\commons-collections-3.2.jar
set CLASSPATH=%CLASSPATH%;%TEDIT_HOME%\lib\commons-digester-1.8.jar
set CLASSPATH=%CLASSPATH%;%TEDIT_HOME%\lib\commons-lang-2.2.jar
set CLASSPATH=%CLASSPATH%;%TEDIT_HOME%\lib\commons-logging-1.1.jar
set CLASSPATH=%CLASSPATH%;%TEDIT_HOME%\lib\flexdock-0.5.0.jar
set CLASSPATH=%CLASSPATH%;%TEDIT_HOME%\lib\groovy-all-1.0.jar
set CLASSPATH=%CLASSPATH%;%TEDIT_HOME%\lib\groovy-engine.jar
set CLASSPATH=%CLASSPATH%;%TEDIT_HOME%\lib\jruby-0.9.2.jar
set CLASSPATH=%CLASSPATH%;%TEDIT_HOME%\lib\jruby-engine.jar
set CLASSPATH=%CLASSPATH%;%TEDIT_HOME%\lib\log4j-1.2.13.jar

@echo on
start javaw -Xmx32m -Xms8m -classpath %CLASSPATH% -Dtedit.home=%TEDIT_HOME% tk.tomby.tedit.Main %1 %2 %3