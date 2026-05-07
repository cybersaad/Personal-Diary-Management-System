@echo off
REM ============================================================
REM  PDMS Installer Builder — Creates a standalone .exe app
REM  Created by Saad and Zahid
REM  Requires: JDK 14+ (jpackage)
REM ============================================================

echo.
echo ========================================
echo   PDMS .exe Installer Builder
echo ========================================
echo.

REM Step 1: Build the JAR first
echo [1/3] Building JAR...
call build.bat

REM Step 2: Clean previous installer output
echo [2/3] Cleaning previous installer...
if exist installer rmdir /s /q installer

REM Step 3: Create the native .exe app image
echo [3/3] Creating PDMS.exe (this may take a moment)...
jpackage --type app-image ^
    --input dist ^
    --main-jar PDMS.jar ^
    --name PDMS ^
    --app-version 1.0 ^
    --vendor "Saad and Zahid" ^
    --description "Personal Diary Management System" ^
    --dest installer ^
    --icon src\images\logo.ico

if errorlevel 1 (
    echo.
    echo [ERROR] jpackage failed. Make sure you have JDK 14+ installed.
    pause
    exit /b 1
)

echo.
echo ========================================
echo   .EXE BUILD SUCCESSFUL!
echo ========================================
echo.
echo   Executable:   installer\PDMS\PDMS.exe
echo.
echo   To install, copy the entire "installer\PDMS" folder
echo   to any location on your PC and run PDMS.exe.
echo.
pause
