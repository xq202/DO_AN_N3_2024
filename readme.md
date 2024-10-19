N3 hello
**backend**
run java spring boot
# api login
POST /auth/login
param: 
- email
- password
response:
{
    status: true : false,
    user: {
        fullname
        gender: int = 0, 1, 2 (chua biet, nam, nu)
        email
        phoneNumber
        address
        dateOfBirth
        ...
    },
    token,
    expiredAt,
    message
}


**frontend**
