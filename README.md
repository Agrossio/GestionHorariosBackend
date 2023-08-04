# Gestión de horarios

## Sugerencias para desarrollo con GitHub
### Clonar
git clone https://github.com/bsb-organization/bsb-arquetipo-back.git

### Setear origen de github
git remote set-url origin https://github.com/bsb-organization/bsb-arquetipo-back.git

Las ramas main y dev estan bloqueadas asi que no se puede pushear directamente.
Por eso para hacer cambio habria que crearse una rama para ese cambio posicionado en dev y con el siguiente comando

### Traerse todos los cambios de dev para actualizar tu versión local
# Antes de comenzar a trabajar
### Posicionarse en rama dev local
git checkout dev

### Traerse todos los cambios de dev a la dev local
git pull origin dev

### Posicionarse en rama de desarrollo local
git checkout {rama-tarea}

### Mergear rama dev local a rama propia local
git merge dev

## Trabajo sobre una rama-tarea

### Ver estado de los cambios de los archivos
git status

### Stagear los cambios
git add {ruta/nombre-archivo} (separar por espacios si son varios archivos)

### Commitear los cambios
git commit -m "nombre commit"

### Pushar cambios a rama propia en la nube
git push origin {rama-propia} 
o 
git push origin HEAD (HEAD es la rama actualmente seleccionada)

