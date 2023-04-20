# tacs-1c-2023
### Trabajo Practico de Técnicas Avanzadas de Construcción de Software. ###

## Build ##
Abren un cmd o powershell en el directorio `tacs-1c-2023` y ejecutan este comando:

```
docker compose up
```
## RUTAS y Metodos ##

### /login ###
```
POST: Crea una session para el usuario.

OPTION: Porque CORS manda un option para ver si puede mandar POST.
```

### /users ###
```
GET: Trae los recursos de esa URI.

OPTION: Te dice que metodos podes usar con esa URI.

POST: Crea un nuevo recurso para esa URI, en el frontend es /register que hace un fetch con metodo POST a /users y sus datos en el Body.

DELETE: Borra los recursos de esa URI.
```
### /users/:id ###
```
GET: Trae un recurso con esa id.

OPTION: Te dice que metodos podes usar con esa URI.

PUT: Modifica un recurso con ese id.

DELETE: Elimina el recurso.
```
### /events ###
```
GET: Trae los eventos.

OPTION: Te dice que metodos podes usar con esa URI.

POST: Crea un nuevo recurso para esa URI.

DELETE: Borra los recursos de esa URI.
```
### /events/:id ###
```
GET: Trae un recurso con esa id.

OPTION: Te dice que metodos podes usar con esa URI.

PUT: Modifica un recurso con ese id.

DELETE: Elimina el recurso.
```
### /events/:idEvent/options ###
```
GET: Trae las Opciones del Evento que tiene idEvento.

OPTION: Te dice que metodos podes usar con esa URI.

POST: Crea una nueva Opcion para el Evento.

DELETE: Borra las Opciones.
```

### /events/:idEvent/options/:id ###
```
GET: Trae una opcion de evento especifico de ese evento.

OPTION: Te dice que metodos podes usar con esa URI.

PUT: Modifica una opcion de evento especifico de ese evento.

DELETE: Elimina una opcion de evento especifico de ese evento.
```
### /events/:idEvent/options/:idOption/votes ###
```
GET: Trae los votos de la opcion idOption del evento idEvent.

OPTION: Te dice que metodos podes usar con esa URI.

POST: Crea un nuevo voto para la opcion idOption del evento idEvent.

DELETE: Elimina los votos de la opcion idOption del evento idEvent.
```
### /events/:idEvent/options/:idOption/votes/:id ###
```
GET: Trae el voto id de la opcion idOption del evento idEvent.

OPTION: Te dice que metodos podes usar con esa URI.

DELETE: Elimina el voto id de la opcion idOption del evento idEvent.
```
### /monitoring ###
```
GET: Trae un contador con la cantidad de eventos creados y horarios votados anotados en las últimas 2 horas.
```
## Links ##
* [Discord](https://discord.gg/ChK8N2h5 "Discord")
* [Trello](https://trello.com/w/tacs1c2023)

## Grupo ##
Apellido, Nombre | Legajo | Email | Cuenta GitHub
------------- | ------------- | ------------- | -------------
Margiottiello, Tomas  |  167240-0 | tmargiottiello@frba.utn.edu.ar |
Ciruzzi, Genaro | 168276-3 | gciruzzi@frba.utn.edu.ar | [JuliaMartiUTN](https://github.com/JuliaMartiUTN)
Martí, Julia | 171834-4 | jmarti@frba.utn.edu.ar | [Gen13673](https://github.com/Gen13673)
Dominguez, Facundo Nicolas | 156045-1 | facudominguez@frba.utn.edu.ar | [fndominguez](https://github.com/fndominguez)
Gabito, Bernardo | 171653-0 | bgabitobrodsky@frba.utn.edu.ar | [bgabitobrodsky](https://github.com/bgabitobrodsky)
Monti, Fernando | 143137-7 | fmonti@frba.utn.edu.ar | [agustinmonti](https://github.com/agustinmonti)
