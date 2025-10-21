# Etapa de build usando Maven e JDK 17 (evita usar mvnw e problemas de permissão)
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copia o pom para baixar dependências em cache
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Copia o código-fonte e faz o build
COPY src ./src
RUN mvn -q -DskipTests clean package

# Etapa de runtime com JRE 17
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar /app/app.jar

# O Render injeta a variável PORT automaticamente; seu app já usa ${PORT:8080}
EXPOSE 8080

# Perfil de produção
CMD ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]