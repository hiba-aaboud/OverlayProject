all: build
build:
	javac -cp lib/amqp-client-5.20.0.jar:lib/slf4j-api-2.0.9.jar:lib/slf4j-simple-2.0.9.jar src/*.java src/node/*.java src/graph/*.java
clean:
	rm -f src/*.class src/node/*.class src/graph/*.class 
	# rm -r bin
init: 
	java -cp lib/amqp-client-5.20.0.jar:lib/slf4j-api-2.0.9.jar:lib/slf4j-simple-2.0.9.jar:src/ graph/Init matrix.txt
run:
	java -cp lib/amqp-client-5.20.0.jar:lib/slf4j-api-2.0.9.jar:lib/slf4j-simple-2.0.9.jar:src/ Main 

