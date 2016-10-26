# Notas Tarea 1

Ya hice una clase rectángulo que para hacer uno se debe indicar su esquina superior izquierda y su esquina inferior derecha.  

Ahora pasaremos a la creación de los nodos para formar el Rtree. A priori creo yo, por lo que entiendo de la tarea, no deberiamos tener problemas por nodo (o sea, nos caben todos los rectángulos posibles de un nodo en memoria) pero deberíamos tener problemas al por ejemplo tener $n$ nodos con sus $M$ espacios llenos y tenerlos en memoria no se podría. Habría que escribirlos en disco. ¿Cómo haremos esto? Aún no tengo idea. Programaré sin tener esto en mente (espero no sea un error muy grave) y cuando hayan problemas de memoria, supongo que java nos indicará esto con una exception o un error, y ahí entonces podremos hacer catch y escribir en disco, etc.  
