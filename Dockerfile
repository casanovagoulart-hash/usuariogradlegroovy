# =====================================================================
# ETAPA 1: Construção da Aplicação (Build com Gradle)
# =====================================================================
# Utiliza a imagem oficial do Gradle com Java 17 baseada em Debian.
# Nomeamos explicitamente este estágio como 'build' para referenciá-lo depois.
FROM gradle:8.14-jdk17 AS build

# Define o diretório de trabalho padrão dentro do container.
WORKDIR /app

# Copia todo o código-fonte do projeto local para dentro do container.
COPY . .

# Executa o build do projeto utilizando o Gradle.
# A flag '--no-daemon' evita que o Gradle mantenha processos em segundo plano,
# economizando memória e evitando travamentos em ambientes de container.
# O comando 'bootJar' gera exclusivamente o artefato executável
# com o Spring Boot embutido, evitando a geração do arquivo '-plain.jar' que causa
# conflito no comando COPY --from=build /app/build/libs/*.jar.
RUN gradle bootJar --no-daemon

# =====================================================================
# ETAPA 2: Imagem Final de Execução (Runtime Leve com Alpine)
# =====================================================================
# Utiliza uma imagem minimalista e segura (Alpine Linux com Java 17 JRE/JDK) 
# apenas para rodar o arquivo .jar compilado, reduzindo o tamanho final da imagem.
FROM eclipse-temurin:17-jdk-alpine

# Define o diretório de trabalho padrão para a aplicação final.
WORKDIR /app

# Copia o arquivo .jar gerado na pasta build/libs do Gradle da etapa anterior.
# CORREÇÃO: O nome do estágio anterior é 'build' (e não maven_upstream),
# e o Gradle por padrão gera os artefatos na pasta build/libs/.
COPY --from=build /app/build/libs/*.jar /app/usuariogradlegroovy.jar

# Expõe a porta onde o microsserviço vai escutar as requisições.
# Nota: O EXPOSE está configurado para 8080 (consistente com o seu application.properties e docker-compose).
EXPOSE 8080

# Comando executado automaticamente ao iniciar o container para subir a aplicação Spring Boot.
ENTRYPOINT ["java", "-jar", "usuariogradlegroovy.jar"]
