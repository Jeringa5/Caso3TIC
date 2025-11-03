# Caso3TIC

## Miembros del grupo
1. Jeronimo Lopez, j.lopezm234, 202320969
2. Daniel Espitia, da.espitiab,201716251

## Compilar
javac -d out -sourcepath . Caso3/*.java
## Crear ruta esperada por Principal
mkdir -p Caso3TIC/Caso3
## Programa 1
cp Pruebas/prueba1.txt Caso3TIC/Caso3/configuracion.txt
java -cp out Caso3.Principal > Pruebas/salida_programa1.txt
## Programa 2
cp Pruebas/prueba2.txt Caso3TIC/Caso3/configuracion.txt
java -cp out Caso3.Principal > Pruebas/salida_programa2.txt
## Programa 3
cp Pruebas/prueba3.txt Caso3TIC/Caso3/configuracion.txt
java -cp out Caso3.Principal > Pruebas/salida_programa3.txt
