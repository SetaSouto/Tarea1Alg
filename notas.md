# Notas Tarea 1

Ya hice una clase rectángulo que para hacer uno se debe indicar su esquina superior izquierda y su esquina inferior derecha.  

Ahora pasaremos a la creación de los nodos para formar el Rtree. A priori creo yo, por lo que entiendo de la tarea, no deberiamos tener problemas por nodo (o sea, nos caben todos los rectángulos posibles de un nodo en memoria) pero deberíamos tener problemas al por ejemplo tener $n$ nodos con sus $M$ espacios llenos y tenerlos en memoria no se podría. Habría que escribirlos en disco. ¿Cómo haremos esto? Aún no tengo idea. Programaré sin tener esto en mente (espero no sea un error muy grave) y cuando hayan problemas de memoria, supongo que java nos indicará esto con una exception o un error, y ahí entonces podremos hacer catch y escribir en disco, etc.  

En teoría los rectángulos finales se encuentran en las hojas (que son nodos también) así que diremos que la clase Rectangle y Node son implementaciones de la interfaz INode, de esta manera podemos calcular el MBR recursivamente, el MBR del padre es la convinación de los MBR de los hijos y finalmente el MBR de una hoja es en sí mismo el propio rectángulo.

Un nodo puede tener a lo más $M$ nodos hijos, o en su defecto, hojas (Rectangle), o sea una solución es hacer un collections de hijos.

OJO: Node es una clase abstracta, donde se debe implementar la inserción (así tenemos distintas versiones del split).  
