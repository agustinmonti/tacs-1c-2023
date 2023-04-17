# tacs-1c-2023
### Trabajo Practico de Técnicas Avanzadas de Construcción de Software. ###

## Build Backend ##
Abren un cmd o powershell en el directorio `tacs-1c-2023\tacs-1c-2023\backend` y ejecutan estos comandos:

```
maven package
```

Eso les crea los jar que va a necesitar Docker para construir la imagen

```
docker build -t tacs-1c-2023:entrega1 .
```

Eso crea la imagen que podes correr con:

```
docker run -p 8080:8080 --name app tacs-1c-2023:entrega1
```

## Links ##
* [Discord](https://discord.gg/ChK8N2h5 "Discord")
* [Trello](https://trello.com/w/tacs1c2023)

## Grupo ##
Apellido, Nombre | Legajo | Email | Cuenta GitHub
------------- | ------------- | ------------- | -------------
Margiottiello, Tomas  |  167240-0 | tmargiottiello@frba.utn.edu.ar |
Ciruzzi, Genaro | 168276-3 | gciruzzi@frba.utn.edu.ar |
Martí, Julia | 171834-4 | jmarti@frba.utn.edu.ar |
Dominguez, Facundo Nicolas | 156045-1 | facudominguez@frba.utn.edu.ar |
Gabito, Bernardo | 171653-0 | bgabitobrodsky@frba.utn.edu.ar |
Monti, Fernando | 143137-7 | fmonti@frba.utn.edu.ar | [agustinmonti](https://github.com/agustinmonti)
