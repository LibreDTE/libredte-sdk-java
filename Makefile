JC = javac
EJEMPLOS_CLASSES = classes
JFLAGS = -g
EJEMPLO = Ejemplo_001_generar_dte
#EJEMPLO = Ejemplo_002_actualizar_estado
#EJEMPLO = Ejemplo_003_dte_emitido_timbre
#EJEMPLO = Ejemplo_004_dte_emitido_consultar
#EJEMPLO = Ejemplo_005_dte_emitido_pdf

all:
	mkdir -p build
	$(JC) $(JFLAGS) -classpath lib/json-simple-1.1.1.jar:. src/cl/libredte/*.java -d build
	cd build && jar -cf libredte.jar *
	mv build/libredte.jar lib

test: all
	mkdir -p $(EJEMPLOS_CLASSES)
	$(JC) $(JFLAGS) -classpath lib/json-simple-1.1.1.jar:lib/libredte.jar:. ejemplos/$(EJEMPLO).java -d $(EJEMPLOS_CLASSES)
	java -cp $(EJEMPLOS_CLASSES):lib/json-simple-1.1.1.jar:lib/libredte.jar $(EJEMPLO)

clean:
	rm -rf $(EJEMPLOS_CLASSES) build lib/libredte.jar ejemplos/*.pdf ejemplos/*.png
