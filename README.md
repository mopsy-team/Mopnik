# Mopnik
Program implementujący metodyki

# Kompilacja i uruchomienie:

Instalacja Javy:
sudo apt-get install default-jdk

Instalacja Maven:
sudo apt install maven

Instalacja Mopsima w lokalnym repozytorium:
Idź do głównego katalogu Mopnik
mvn install:install-file -Dfile=src/libs/mopsim.jar -DgroupId=mopsim -DartifactId=mopsim -Dversion=0.2

Kompilacja programu Mopnik:
mvn compile

Uruchomienie programu Mopnik:
mvn exec:java
