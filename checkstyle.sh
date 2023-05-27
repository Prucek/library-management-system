#!/bin/bash

modules=`find . -mindepth 2 -name 'pom.xml' | xargs dirname`

for module in $modules; do
    if ! ./mvnw -f "$module" checkstyle:check \
               --batch-mode \
               --color always \
               --show-version \
               -Dcheckstyle.violationSeverity=warning \
               -Dcheckstyle.config.location=${CI_PROJECT_DIR:-.}/checkstyle-config.xml; then
        failed=1
    fi
done

[ -z "$failed" ]
