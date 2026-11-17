@echo off
setlocal
cd /d "%~dp0"
where javac >nul 2>nul
if errorlevel 1 (
  echo ERROR: javac was not found. Install a JDK and add its bin folder to PATH.
  pause
  exit /b 1
)
if not exist bin mkdir bin
echo Compiling traffic simulation...
javac -encoding UTF-8 -d bin src\*.java
if errorlevel 1 (
  echo.
  echo Compilation failed. See the errors above.
  pause
  exit /b 1
)
echo Starting traffic simulation...
java -cp bin App
if errorlevel 1 (
  echo.
  echo The application exited with an error.
  pause
  exit /b 1
)
endlocal
