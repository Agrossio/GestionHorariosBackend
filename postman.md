### POST {{host}}/person
{
"name": "Tester",
"lastname": "Person",
"email": "test@mail",
"cuil": "20-4545646-4",
"password": "Password1"
}

### POST {{host}}/dev/test@mail

### POST {{host}}/auth/login
{
"email": "test@mail",
"password": "Password1"
}

### GET {{host}}/person/test@mail