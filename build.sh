mvn clean package
FUCKYOU=/mnt/e/SteamLibrary/steamapps/common/ProjectZomboid

cp target/javaagent-1.0-SNAPSHOT.jar $FUCKYOU
cp target/javaagent-1.0-SNAPSHOT-jar-with-dependencies.jar $FUCKYOU
