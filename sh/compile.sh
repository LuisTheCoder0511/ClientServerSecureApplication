#!/bin/bash
sudo rm -rf ./../out
javac -d ./../out/production/CybersecurityProject $(find ./../src -name "*.java")