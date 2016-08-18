# Create department
Method POST
Url /department
Request header Content-Typ = application/json
Request Body
    {
        "name":"test",              // name
        "location":"chengdu",       // location, optional
        "managerId":1,              // manager id, optional
        "openPositions":0,          // open positions
        "parentDepartmentId":1      // parent department id, optional
    }
Response Body
    {
      "id": 1,                      // id
      "name": "test",
      "location": "chengdu",
      "managerId": 1,
      "openPositions": 0,
      "parentDepartmentId": 1
    }

# List all departments
Method GET
Url /departments
Response Body
    [
      {
        "id": 1,
        "name": "test",
        "location": "chengdu",
        "managerId": null,
        "openPositions": 0,
        "parentDepartmentId": null
      }
    ]

# Get department by id
Method GET
Url /department/{id}
Response Body
    {
      "id": 1,
      "name": "test",
      "location": "chengdu",
      "managerId": null,
      "openPositions": 0,
      "parentDepartmentId": null
    }

# Get parent department to id
Method GET
Url /department/{id}/parent
Response Body
    {
      "id": 1,
      "name": "test",
      "location": "chengdu",
      "managerId": null,
      "openPositions": 0,
      "parentDepartmentId": null
    }

# Get sub departments to id
Method GET
Url /department/{id}/children
Response Body
    [{
      "id": 1,
      "name": "test",
      "location": "chengdu",
      "managerId": null,
      "openPositions": 0,
      "parentDepartmentId": null
    }]

# Get employees of departments
Method GET
Url /department/{id}/employees
Response Body
    [
      {
        "id": 1,
        "firstname": "first",
        "lastname": "last",
        "ldapUsername": "login",
        "gender": "Male",
        "birthdate": 0,
        "title": "test",
        "grade": "grade",
        "departmentId": null
      },
    ]

# Update department
Method PUT
Url /department
Request header Content-Typ = application/json
Request Body
    {
        "id": 1,                    // id
        "name":"test",              // name, useless
        "location":"chengdu",       // location, optional
        "managerId":1,              // manager id, optional
        "openPositions":0,          // open positions
        "parentDepartmentId":1      // parent department id, optional
    }
Response Body
    {
      "id": 1,
      "name": "test",
      "location": "chengdu",
      "managerId": 1,
      "openPositions": 0,
      "parentDepartmentId": 1
    }

# Remove department
Method DELETE
Url /department/{id}


# Create employee
Method POST
Url /employee
Request header Content-Typ = application/json
Request Body
    {
        "firstname":"first",        // firstname
        "lastname":"last",          // lastname
        "ldapUsername":"login",     // ldap username
        "gender":"male",            // gender, optional
        "birthday":1223432,         // birthday, optional, datetime in milliseconds
        "title":"test",             // title
        "grade":"grade",            // grade
        "departmentId":3            // department id, optional
    }
Response Body
    {
      "id": 2,                      // id
      "firstname": "first",
      "lastname": "last",
      "ldapUsername": "login",
      "gender": "Male",
      "birthdate": 0,
      "title": "test",
      "grade": "grade",
      "departmentId": 3
    }

# List all employees
Method GET
Url /employees
Response Body
    [
      {
        "id": 1,
        "firstname": "first",
        "lastname": "last",
        "ldapUsername": "login",
        "gender": "Male",
        "birthdate": 0,
        "title": "test",
        "grade": "grade",
        "departmentId": null
      },
    ]

# Get employee by id
Method GET
Url /employee/{id}
Response Body
    {
      "id": 1,
      "firstname": "first",
      "lastname": "last",
      "ldapUsername": "login",
      "gender": "Male",
      "birthdate": 0,
      "title": "test",
      "grade": "grade",
      "departmentId": null
    }

# Get department of employee
Method GET
Url /employee/{id}/department
Response Body
    {
      "id": 1,
      "name": "test",
      "location": "chengdu",
      "managerId": null,
      "openPositions": 0,
      "parentDepartmentId": null
    }

# Delete employee
Method DELETE
Url /employee/{id}