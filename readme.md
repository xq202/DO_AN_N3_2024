N3 hello
**backend**
run java spring boot
# api login
POST /auth/login<br>
param: <br>
- email<br>
- password<br>
response:<br>
{<br>
    status: true : false,<br>
    responseCode: int,<br>
    message,<br>
    data: {<br>
        user: {<br>
            fullname<br>
            gender: int = 0, 1, 2 (chua biet, nam, nu)<br>
            email,<br>
            phoneNumber,<br>
            address,<br>
            dateOfBirth,<br>
            ...<br>
        },<br>
        token,<br>
        expiredAt,<br>
    }<br>
}<br>
