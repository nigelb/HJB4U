@echo off
%~d0
cd %~p0

setLocal EnableDelayedExpansion
set CLASSPATH="
for /R ../java %%a in (*.jar) do (
set CLASSPATH=!CLASSPATH!;%%a
)
set CLASSPATH=!CLASSPATH!"

java hjb4u.launch.Launch