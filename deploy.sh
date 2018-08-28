#!/usr/bin/env bash
set -e

cd target/build
git init
git add .
git commit -m "Deploy to GitHub Pages"
git push --force --quiet "git@github.com:duncanjbrown/connect4.git" master:gh-pages
rm -rf .git
