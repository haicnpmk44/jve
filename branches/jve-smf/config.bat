@set JAVA_CMD="%JAVA_HOME%\bin\java"
@if "%JAVA_HOME%"== "" SET JAVA_CMD="java"
%JAVA_CMD% -classpath lib\jmf.jar;lib\fobs4jmf.jar JMStudio