### Introduction

This is a self-contained service that exposes a REST API endpoint to import files that can either
be CSV, JSON or XML data files. Currently, this supports uploading JSON files only.

One way choose to upload the data in one of the following methods:
1. By calling the `POST` method on `/user` endpoint. The Swagger specs should be helpful to get started
2. By bulk uploading a list of users using the `/user/bulk` endpoint
3. By using a JSON file and uploading the file using `/user/bulk/import` endpoint. 

Using the method `1`, we can directly get back the results. Whereas, using method `2` and `3`, if the data
is valid, the request gets `ACCEPTED` and the endpoint responds back with a Bulk JOB UUID and a status.
This bulk job UUID can be queried later, to retrieve the status of the bulk job, using the following endpoint
`/user/bulk/{UUID}`. 

This is because, whenever we use the methods `2` and `3`, this makes an asynchronous call to process the records,
so that the thread serving the request is not blocked.

The file `UserController.java` could be a good starting point.

### Steps to run:
1. Install Docker on your machine.

2. Clone the git repository and navigate into the directory.

2. Run the latest version of MySQL as docker container:
```
docker run --name=docker-mysql --env="MYSQL_ROOT_PASSWORD=password"  \
--env="MYSQL_PASSWORD=password" --env="MYSQL_DATABASE=decisionsciences" -d mysql:latest
```

3. Build the jar for the Spring boot application and then build the docker image:
```
./gradlew  build && docker build . -t json-exporter-service
```

4. Finally, run the Spring boot container in tandem with the MySQL container:
```
docker run -p 9000:9000 --name json-exporter-service --link docker-mysql:mysql -d json-exporter-service
```

5. You may choose to view the logs and see if the server has booted up:
```
docker logs json-exporter-service
```


### Swagger UI 
You may also try to go to this URL and try out using swagger-ui.
```
http://localhost:9000/swagger-ui.html#/
```

### API Requests and Responses:

1. Create an Address:

```
curl -X POST \
  http://localhost:9000/api/v1/user/ \
  -H 'Content-Type: application/json' \
  -d '{

	"id": 4,
    "firstName": "Sammy",
    "lastName": "Tom",
    "email": "sammy@gmail.com",
    "age": 21,
    "additionalFields": null,
    "streetAddress": "877 San Carlos Rd",
    "additionalAddress": "Soe",
    "houseNo": "2B",
    "city": "San Francisco",
    "stateCode": "CA",
    "zipcode": 96719
}'

Response:

{
    "status": 200,
    "data": {
        "id": 28,
        "firstName": "Sammy",
        "lastName": "Tom",
        "email": "sammy@gmail.com",
        "age": 21,
        "additionalFields": null,
        "streetAddress": "877 San Carlos Rd",
        "additionalAddress": "Soe",
        "houseNo": "2B",
        "city": "San Francisco",
        "stateCode": "CA",
        "zipcode": 96719,
        "userCreatedAt": "2019-02-09T18:33:07Z",
        "userUpdatedAt": "2019-02-09T18:36:26Z"
    }
}
```
2. Get all the users:

```
curl -X GET 'http://localhost:9000/api/v1/user?page=3&size=2'

Response:

{
    "status": 200,
    "data": {
        "content": [
            {
                "id": 16,
                "firstName": "Zeenat",
                "lastName": "Sheikh",
                "email": "Zeenat@gmail.com",
                "age": 21,
                "additionalFields": null,
                "streetAddress": "876 San Carlos Rd",
                "additionalAddress": "",
                "houseNo": "2A",
                "city": "San Francisco",
                "stateCode": "CA",
                "zipcode": 98769,
                "userCreatedAt": "2019-02-09T17:47:29Z",
                "userUpdatedAt": "2019-02-09T17:47:29Z"
            },
            {
                "id": 26,
                "firstName": "Sam",
                "lastName": "DCosta",
                "email": "sammy@gmail.com",
                "age": 21,
                "additionalFields": null,
                "streetAddress": "876 San Carlos Rd",
                "additionalAddress": "Soe",
                "houseNo": "2A",
                "city": "San Francisco",
                "stateCode": "CA",
                "zipcode": 96719,
                "userCreatedAt": "2019-02-09T18:15:02Z",
                "userUpdatedAt": "2019-02-09T18:15:02Z"
            }
        ],
        "pageable": {
            "sort": {
                "sorted": false,
                "unsorted": true,
                "empty": true
            },
            "offset": 6,
            "pageSize": 2,
            "pageNumber": 3,
            "paged": true,
            "unpaged": false
        },
        "last": true,
        "totalPages": 4,
        "totalElements": 8,
        "size": 2,
        "number": 3,
        "sort": {
            "sorted": false,
            "unsorted": true,
            "empty": true
        },
        "first": false,
        "numberOfElements": 2,
        "empty": false
    }
}
```

3. Get all the addresses:
```
curl -X GET 'http://localhost:9000/api/v1/address?page=3&size=2'

Response:

{
    "status": 200,
    "data": {
        "content": [
            {
                "createdAt": "2019-02-09T17:47:29Z",
                "updatedAt": "2019-02-09T17:47:29Z",
                "id": 15,
                "streetAddress": "876 San Carlos Rd",
                "additionalAddress": "",
                "houseNo": "2A",
                "city": "San Francisco",
                "stateCode": "CA",
                "zipcode": 98769
            },
            {
                "createdAt": "2019-02-09T18:15:02Z",
                "updatedAt": "2019-02-09T18:15:02Z",
                "id": 25,
                "streetAddress": "876 San Carlos Rd",
                "additionalAddress": "Soe",
                "houseNo": "2A",
                "city": "San Francisco",
                "stateCode": "CA",
                "zipcode": 96719
            }
        ],
        "pageable": {
            "sort": {
                "sorted": false,
                "unsorted": true,
                "empty": true
            },
            "offset": 6,
            "pageSize": 2,
            "pageNumber": 3,
            "paged": true,
            "unpaged": false
        },
        "last": false,
        "totalPages": 8,
        "totalElements": 15,
        "size": 2,
        "number": 3,
        "first": false,
        "sort": {
            "sorted": false,
            "unsorted": true,
            "empty": true
        },
        "numberOfElements": 2,
        "empty": false
    }
}
```

4. Create multiple users in one request using the `bulk` edpoint:

```
curl -X POST \
  http://localhost:9000/api/v1/user/bulk \
  -H 'Content-Type: application/json' \
  -d '[
    {
        "firstName": "Pratik",
        "lastName": "Vishnu",
        "email": "pv@gmail.com",
        "age": 21,
        "additionalFields": null,
        "streetAddress": "890 Willow Road",
        "additionalAddress": "90",
        "houseNo": null,
        "city": "Hayward",
        "stateCode": "CA",
        "zipcode": 98709
    },
    {
        "firstName": "Joel",
        "lastName": "Mimmi",
        "email": "jmm@gmail.com",
        "age": 29,
        "additionalFields": null,
        "streetAddress": "654 Lawrence Avenuw",
        "additionalAddress": "",
        "houseNo": null,
        "city": "San Francisco",
        "stateCode": "CA",
        "zipcode": 98169
    },
    {
        "firstName": "Robin",
        "lastName": "T",
        "email": "robn@gmail.com",
        "age": 19,
        "additionalFields": null,
        "streetAddress": "232 Walker St",
        "additionalAddress": "",
        "houseNo": null,
        "city": "San Francis",
        "stateCode": "CA",
        "zipcode": 98709
    }
]'

Response:

{
    "status": 200,
    "data": {
        "id": "90987b4c-4251-4ec5-a102-b0e57eae4a6",
        "jobStatus": "ACCEPTED",
        "result": {
            "totalNoOfObjects": 3,
            "numberOfObjectsProcessed": 0,
            "numberOfObjectsFailed": 0,
            "failedObjectList": []
        }
    }
}

```

5. Create multiple users using the `/bulk/import` endpoint:

```
curl -X POST \
  http://localhost:9000/api/v1/user/import \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: c379b93e-4c2d-40eb-b4d9-b7a94205fada' \
  -H 'cache-control: no-cache' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -F file=@/Users/smishra/Documents/personal/JsonExporterService/src/main/resources/Sampledata.json

Response:

{
    "status": 200,
    "data": {
        "id": "60117b4c-4251-4ec5-a102-b0e57eae40e0",
        "jobStatus": "ACCEPTED",
        "result": {
            "totalNoOfObjects": 3,
            "numberOfObjectsProcessed": 0,
            "numberOfObjectsFailed": 0,
            "failedObjectList": []
        }
    }
}
```

6. Finally, get the status of the Bulk Job UUID that we received in the previous steps:

```
curl -X GET http://localhost:9000/api/v1/user/bulk/c952f9c1-1625-4792-83c0-699d327ca2af 

Response:

{
    "status": 200,
    "data": {
        "id": "c952f9c1-1625-4792-83c0-699d327ca2af",
        "jobStatus": "PROCESSED",
        "result": {
            "totalNoOfObjects": 3,
            "numberOfObjectsProcessed": 3,
            "numberOfObjectsFailed": 0,
            "failedObjectList": []
        }
    }
}

```