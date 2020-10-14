# API

- See resources/SimpleTODO.postman_collection.json

## Authentication

### Create User

### Needed role

- ADMIN (role assignments)
- - (role assignment USER)
- If property "simpletodo.signup" is set to "DISABLED", no new user can register itself

### Method

POST

### URL

http://localhost:9090/api/auth/signup

### Body

{
    "username": "User",
    "email": "user@example.com",
    "password": "password",
    "roles": ["admin", "user"]
}

### Sign in user

### Needed role

- -

### Method

POST

### URL

http://localhost:9090/api/auth/signin

### Body
{
    "username": "User",
    "password": "password"
}

## Connection check

### Availability

### Needed role

- -

### Method

GET

### URL

http://localhost:9090/api/connection/available

### Authorized

### Needed role

- Admin
- User

### Method

GET

### URL

http://localhost:9090/api/connection/authorized

### Headers

Authorization Bearer TOKEN

## I18n

### All languages

### Needed role

- -

### Method

GET

### URL

http://localhost:9090/api/i18n/languages

### Specific language file

### Needed role

- -

### Method

GET

### URL

http://localhost:9090/api/i18n/languages/LANGID

## Workspaces

### Get all for user

### Needed role

- Admin
- User

### Method

GET

### URL

http://localhost:9090/api/workspaces

### Headers

Authorization Bearer TOKEN

### Get specific

### Needed role

- Admin
- User

### Method

GET

### URL

http://localhost:9090/api/workspaces/WSID

### Headers

Authorization Bearer TOKEN

### Create new

### Needed role

- Admin
- User

### Method

POST

### URL

http://localhost:9090/api/workspaces

### Headers

Authorization Bearer TOKEN

### Body

{
    "name": "New Workspace",
    "users": ["USRID"]
}

### Update

### Needed role

- Admin
- User

### Method

PUT

### URL

http://localhost:9090/api/workspaces/WSID

### Headers

Authorization Bearer TOKEN

### Body

{
    "name": "Updated name",
    "users": ["USRID"]
}

### Delete

### Needed role

- Admin
- User

### Method

DELETE

### URL

http://localhost:9090/api/workspaces/WSID

### Headers

Authorization Bearer TOKEN

### Delete all

### Needed role

- Admin

### Method

DELETE

### URL

http://localhost:9090/api/workspaces

### Headers

Authorization Bearer TOKEN

## Lists

### Get all for user

### Needed role

- Admin
- User

### Method

GET

### URL

http://localhost:9090/api/workspaces/WSID/lists

### Headers

Authorization Bearer TOKEN

### Get specific

### Needed role

- Admin
- User

### Method

GET

### URL

http://localhost:9090/api/workspaces/WSID/lists/LID

### Headers

Authorization Bearer TOKEN

### Create new

### Needed role

- Admin
- User

### Method

POST

### URL

http://localhost:9090/api/workspaces/WSID/lists

### Headers

Authorization Bearer TOKEN

### Body

{
    "name": "New list"
}

### Update

### Needed role

- Admin
- User

### Method

PUT

### URL

http://localhost:9090/api/workspaces/WSID/lists/LID

### Headers

Authorization Bearer TOKEN

### Body

{
    "name": "Updated name"
}

### Move

### Needed role

- Admin
- User

### Method

PUT

### URL

http://localhost:9090/api/workspaces/WSID/lists/LID/move

### Headers

Authorization Bearer TOKEN

### Body

{
    "workspaceId": "WID"
}

### Delete

### Needed role

- Admin
- User

### Method

DELETE

### URL

http://localhost:9090/api/workspaces/WSID/lists/LID

### Headers

Authorization Bearer TOKEN

### Delete all in workspace

### Needed role

- Admin
- User

### Method

DELETE

### URL

http://localhost:9090/api/workspaces/WSID/lists

### Headers

Authorization Bearer TOKEN

## Todos

### Get all for user

### Needed role

- Admin
- User

### Method

GET

### URL

http://localhost:9090/api/workspaces/WSID/lists/LID/todos

### Headers

Authorization Bearer TOKEN

### Get specific

### Needed role

- Admin
- User

### Method

GET

### URL

http://localhost:9090/api/workspaces/WSID/lists/LID/todos/TID

### Headers

Authorization Bearer TOKEN

### Create new

### Needed role

- Admin
- User

### Method

POST

### URL

http://localhost:9090/api/workspaces/WSID/lists/LID/todos

### Headers

Authorization Bearer TOKEN

### Body

{
    "msg": "new Todo",
    "dueDate": null
}

### Update

### Needed role

- Admin
- User

### Method

PUT

### URL

http://localhost:9090/api/workspaces/WSID/lists/LID/todos/TID

### Headers

Authorization Bearer TOKEN

### Body

{
    "msg": "Updated name",
    "dueDate": null
}

### Move

### Needed role

- Admin
- User

### Method

PUT

### URL

http://localhost:9090/api/workspaces/WSID/lists/LID/todos/TID/move

### Headers

Authorization Bearer TOKEN

### Body

{
    "listId": "LID"
}

### Delete

### Needed role

- Admin
- User

### Method

DELETE

### URL

http://localhost:9090/api/workspaces/WSID/lists/LID/todos/TID

### Headers

Authorization Bearer TOKEN

### Delete all in list

### Needed role

- Admin
- User

### Method

DELETE

### URL

http://localhost:9090/api/workspaces/WSID/lists/LID/todos

### Headers

Authorization Bearer TOKEN

## Users

### Get all

### Needed role

- Admin

### Method

GET

### URL

http://localhost:9090/api/users

### Headers

Authorization Bearer TOKEN

### Get specific

### Needed role

- Admin
- User

### Method

GET

### URL

http://localhost:9090/api/users/UID

### Headers

Authorization Bearer TOKEN

### Update

### Needed role

- Admin
- User

### Method

PUT

### URL

http://localhost:9090/api/users/UID

### Headers

Authorization Bearer TOKEN

### Body

{
    "name": "Updated name",
    "users": ["USRID"]
}

### Delete

### Needed role

- Admin
- User

### Method

DELETE

### URL

http://localhost:9090/api/users/UID

### Headers

Authorization Bearer TOKEN

### Delete all

### Needed role

- Admin

### Method

DELETE

### URL

http://localhost:9090/api/users

### Headers

Authorization Bearer TOKEN

### Forgot password

### Needed role

- -

### Method

POST

### URL

http://localhost:9090/api/users/password/forgot

### Body
{
    "username": "User",
    "email": "Email"
}

### Reset password

### Needed role

- -

### Method

PUT

### URL

http://localhost:9090/api/users/password/reset/TokenID

### Resend activation

### Needed role

- User
- Admin

### Method

PUT

### URL

http://localhost:9090/api/users/verify/resend

### Email Address Verification

### Needed role

- -

### Method

PUT

### URL

http://localhost:9090/api/users/verify/TokenID
