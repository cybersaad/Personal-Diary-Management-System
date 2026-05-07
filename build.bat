@echo off
REM ============================================================
REM  PDMS Build Script — Personal Diary Management System
REM  Created by Saad and Zahid
REM ============================================================

echo.
echo ========================================
echo   PDMS Build Script
echo ========================================
echo.

REM Step 1: Clean previous builds
echo [1/4] Cleaning previous builds...
if exist out rmdir /s /q out
if exist dist rmdir /s /q dist
mkdir out
mkdir dist

REM Step 2: Compile all Java source files
echo [2/4] Compiling source code...
javac -encoding UTF-8 -d out ^
    src\util\FileUtil.java ^
    src\model\Mood.java ^
    src\model\Priority.java ^
    src\model\DiaryEntry.java ^
    src\model\GroceryItem.java ^
    src\model\Memory.java ^
    src\model\MoodEntry.java ^
    src\model\Task.java ^
    src\manager\DiaryManager.java ^
    src\manager\GroceryManager.java ^
    src\manager\MemoryManager.java ^
    src\manager\MoodManager.java ^
    src\manager\TaskManager.java ^
    src\gui\UITheme.java ^
    src\gui\DashboardFrame.java ^
    src\gui\DiaryFrame.java ^
    src\gui\GroceryFrame.java ^
    src\gui\TaskFrame.java ^
    src\gui\MoodFrame.java ^
    src\gui\MemoryFrame.java ^
    src\main\DiaryApplication.java

if errorlevel 1 (
    echo.
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)
echo        Compilation successful.

REM Step 3: Copy images into the output folder (for classpath access)
echo [3/4] Copying resources...
xcopy /s /i /q src\images out\images >nul 2>&1

REM Step 4: Create the executable JAR
echo [4/4] Packaging JAR...
jar cfm dist\PDMS.jar MANIFEST.MF -C out .

if errorlevel 1 (
    echo.
    echo [ERROR] JAR packaging failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo   BUILD SUCCESSFUL!
echo ========================================
echo.
echo   JAR created:  dist\PDMS.jar
echo.
echo   Run with:     java -jar dist\PDMS.jar
echo.
pause
