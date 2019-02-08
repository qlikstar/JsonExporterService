``` 
curl -X POST \
  http://localhost:9000/api/v1/user \
  -H 'Content-Type: application/json' \
  -H 'Postman-Token: 414ee5e2-0fa2-4be4-af71-ad402276fead' \
  -H 'cache-control: no-cache' \
  -d '{

    "firstName": "Sanket",
    "lastName": "Mishra",
    "email": "sv@gmail.com",
    "age": 27,
    "additionalFields": null,
    "address": {
        "streetAddress": "1325 San Ramon Road",
        "additionalAddress": null,
        "houseNo": null,
        "city": "Hayward",
        "stateCode": "CA",
        "zipcode": 98769
    }
}'
```