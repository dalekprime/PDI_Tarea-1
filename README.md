===README TAREA 1 PDI===
Bryan Silva - CI.27531458
Oriana Arellano - CI.28309661

===ACLARATORIAS===

- Para el proyecto es necesario el JDK de Liberica Full en version 23.

- Para aplicar un gradiente es necesario haber aplicado previamente uno de los 
filtros de Prewitt, Sobel o Roberts.

- Se implementaron dos modos de perfilado: 4 vecinos y 8 vecinos, que determinan 
la detección de bordes.

- El filtro Roberts solo está disponible en tamaño 2×2, ya que su definición 
clásica se basa en diferencias diagonales inmediatas y no se extiende 
naturalmente a tamaños mayores.

- La vista principal (BasicView) se encarga de manejar el estado de la aplicación.
Mientras que la lógica de procesamiento está extraída y organizada en controladores.

- Para cargar un kernel personalizado se utiliza el botón Custom Kernel.El kernel se 
lee desde un archivo .txt, el cual debe tener el alto y luego el ancho seguido de 
la matriz que contiene los valores a operar separado por espacios.

- En la sección Editar, en la parte superior, se puede recargar la imagen original en 
cualquier momento.

- Las funciones de Undo y Redo permiten deshacer y rehacer cambios, con un máximo de 
5 estados almacenados.

- Los filtos de Gauss y Mediana fueron implementados únicamente en formas cuadradas 
(ej. 3×3, 5×5). Además el número máximo aceptado es 7 en ambas.

- Los filtros de Promedio, Prewitt y Sobel aceptan cualquier combinación rectangular desde 
1×2 o 2×1 hasta 7×7.

- Se puede obtener el perfil de la imagen en cualquier canal.

- Se puede visualizar la curva tonal y el histograma en cualquiera de los canales RGB.

- Se tiene dos formas de hacer zoom in, con el Nearest Neighbor y la interpolación bilineal. 
Y dos formas de hacer zoom out, con el Nearest Neighbor y Super Sampling.

- El brillo y el contraste se aplican en tiempo real y para asegurar el cambio hay que
oprimir el botón abajo del slider.

- Cualquier imagen puede ser guardada en cualquiera de los formatos y por defecto imagenes
que hubieran sido cargadas como png, bpm o ppm, si se eligiera formato rle seran guardadas
con la cabecera P3 y si se eligiera el formato Netpbm seran guardadas como ppm.

- Para determinar los valores de las matrices de Prewitt y Roberts en tamaños 3×3, 5×5 
y 7×7 se utilizó el siguiente documento: https://www.hlevkin.com/hlevkin/47articles/SobelScharrGradients5x5.pdf
Los demás kernels se calculan dinámicamente basados en las fórmulas descritas en dicho documento.