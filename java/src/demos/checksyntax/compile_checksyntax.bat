echo off

REM # This script/batch file is to compile the gsp demo. Users can make changes 
REM # to the demo and use this script/batch file to compile. JAVA_HOME environment variable
REM # should be set in the setenv.bat before running this script.

REM # Set the target directory where the compiled class should be copied.
set targetdir=build

REM # Change directory to gsp Demo Home directory
cd ..\..\..

REM # Run the setenv to set the environment variables.
call bin\setenv.bat

   
    if NOT EXIST %JAVA_CMD% (
    echo. 
    echo ***************************
    echo JAVA_HOME is not set in the bin\setenv.bat
    echo Please set the JAVA_HOME. 
    echo eg. JAVA_HOME=C:\Program Files\Java\jdk1.7.0_80
    echo ***************************
    echo.
    cd src\demos\checksyntax
    pause
    goto END
    )

REM # Compile the gsp demo
%JAVAC_CMD% -d %targetdir% -classpath %CLASSPATH% src\demos\checksyntax\checksyntax.java

echo Completed.

REM # Change directory to the original directory
cd src\demos\checksyntax

pause

:END

