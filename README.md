# ZIO Http and Iron
 
Try to model a domain using refined types.

For that purpose using https://github.com/Iltotore/iron

Domain just consists of 
 - entity: User
 - valueObjects: Age, Name 

We model restrictions for valueObject via refined types.

\+ We do not validate requests (it already consist of correct types). So JSON codec fails if something is incorrect.

\- JSON codec fail fast. 

\- We steel need to have persistent layer separately. So we can provide db codecs or make a record for persistence with primitive types and cast in back in unsafe way


For integration test use testcontainers.




```bash
  docker compose run -d
```

## Correct request 
```bash
  curl -X POST -H "Content-Type: application/json" -d '{"name": "JohnDoe", "age": 15}' http://localhost:8080/users
```

