@echo off
set CURRENT_PATH=%CD%
set NPP_PATH="D:\Software\Notepad++"

echo Killing N++ instances
taskkill /im notepad++.exe /f

echo Delete Plugin
del "D:\Software\Notepad++\plugins\KFramework.dll"

echo Copy the new Version
echo D|xcopy "%CURRENT_PATH%\bin\KFramework.dll" "%NPP_PATH%\plugins\KFramework.dll" /Y

echo Remove the Build
del "%CURRENT_PATH%\bin\KFramework.dll"

echo Start N++
start "Notepad++ starter" /MIN /D "%NPP_PATH%" notepad++.exe