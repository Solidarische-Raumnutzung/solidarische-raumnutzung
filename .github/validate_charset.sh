#!/bin/bash
set -eux
set -x
git config core.quotepath off
IFS='
'
for file in $(git ls-files | grep -v '\.jpg$'); do
    encoding=$(file -b --mime-encoding "$file")
    if [ "$encoding" != "utf-8" ] && [ "$encoding" != "us-ascii" ] && [ "$encoding" != "binary" ]; then
        echo "$file has invalid encoding: $encoding"
        exit 1
    fi
done