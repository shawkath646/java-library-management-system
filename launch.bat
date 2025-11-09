@echo off
title Library Management System
color 0A

echo ========================================
echo    Library Management System
echo ========================================
echo.

set JAVAFX_LIB=lib\javafx-sdk-25.0.1\lib
set MYSQL_JAR=lib\mysql-connector-j-8.0.33.jar
set OUTPUT_DIR=target\classes

echo [1/3] Compiling Java sources...
javac --module-path %JAVAFX_LIB% --add-modules javafx.controls,javafx.fxml -cp "%MYSQL_JAR%;%JAVAFX_LIB%\*" -d %OUTPUT_DIR% src\main\java\*.java src\main\java\controllers\*.java src\main\java\dao\*.java src\main\java\models\*.java src\main\java\utils\*.java

if %ERRORLEVEL% NEQ 0 (
    color 0C
    echo.
    echo [ERROR] Compilation failed!
    echo.
    pause
    exit /b 1
)

echo [OK] Compilation successful!
echo.

echo [2/3] Copying resources...
xcopy /E /I /Y src\main\resources %OUTPUT_DIR% >nul 2>&1
echo [OK] Resources copied!
echo.

echo [3/3] Launching application...
echo Close the window when done.
echo.

java --module-path %JAVAFX_LIB% --add-modules javafx.controls,javafx.fxml -cp "%OUTPUT_DIR%;%MYSQL_JAR%" Main

if %ERRORLEVEL% NEQ 0 (
    color 0C
    echo.
    echo [ERROR] Application failed to start!
    echo.
    echo Common issues:
    echo   - MySQL not running
    echo   - Database not created
    echo   - Wrong database credentials
    echo.
    pause
    exit /b 1
)

echo.
echo Application closed successfully!
pause
