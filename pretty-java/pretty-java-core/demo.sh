#!/bin/bash

mvn clean install
 mvn exec:java -Dexec.mainClass="io.joshuasalcedo.pretty.core.model.progress.threads.impl.ProgressRunnerDemo"
