#!/bin/bash

function runGame() {
  local seed="$1"
  local sample_size="$2"
  local output_path="$3"
  
  mkdir -p "$(dirname ${output_path})"
  /Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/bin/java "-javaagent:/Applications/IntelliJ IDEA 2017.2.3.app/Contents/lib/idea_rt.jar=57331:/Applications/IntelliJ IDEA 2017.2.3.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath /Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/ext/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/jfxswt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/lib/packager.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home/lib/tools.jar:/Users/jbrains/Workspaces/20171129-Siemens/trivia-java/target/test-classes:/Users/jbrains/Workspaces/20171129-Siemens/trivia-java/target/classes:/Users/jbrains/.m2/repository/junit/junit/4.8.2/junit-4.8.2.jar:/Users/jbrains/.m2/repository/io/vavr/vavr/0.9.0/vavr-0.9.0.jar:/Users/jbrains/.m2/repository/io/vavr/vavr-match/0.9.0/vavr-match-0.9.0.jar com.adaptionsoft.games.uglytrivia.test.GoldenMasterGameRunner "$seed" "$sample_size" 1> "$output_path"
}

output_root_path="./src/test/data"
seed="28172"
sample_size="10000"
if [[ "generate" == "$1" ]]
then
  runGame "$seed" "$sample_size" "${output_root_path}/golden-master/games-${seed}-${sample_size}.txt"
  echo "Generated new golden master output."
  exit 0
elif [[ "check" == "$1" ]]
then
  rm -fr "${output_root_path}/golden-master-run/*"
  runGame "$seed" "$sample_size" "${output_root_path}/golden-master-run/games-${seed}-${sample_size}.txt"
  echo "Checking golden master output."
  diff -r "${output_root_path}/golden-master" "${output_root_path}/golden-master-run"
  if [[ "$?" == 0 ]]; then echo "OK."; else echo "Differences detected."; fi
  exit "$?"
else
  echo You must specify either ''check'' or ''generate'' as the first parameter to this program.
  exit 1
fi

