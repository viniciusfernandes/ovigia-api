### conectar remotamente
mongosh --host localhost --port 27017 --username vinicius --password 1234 --authenticationDatabase admin

### criar manualmente a database ovigia
use ovigia

### check remotely
mongosh --host localhost --port 27017

### criando usuarios
db.createUser({ user: "vinicius", pwd: "1234", roles: [{ role: "readWrite", db: "ovigia" }]})
db.updateUser("admin", { roles: [{ role: "readWrite", db: "ovigia" }]})

### list networks
docker network ls
docker network inspect mongodb-network
docker network rm <network_name_or_id>

