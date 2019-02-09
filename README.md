You may also try to go to this URL and try out using swagger-ui.
```
http://{host-name}:{port}/swagger-ui.html#/
```

Requests:

``` 
curl -X POST \
  http://localhost:9000/api/v1/user \
  -H 'Content-Type: application/json' \
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