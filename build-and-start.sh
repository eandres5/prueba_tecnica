#!/bin/bash

echo "Microservicios iniciando...."
echo "================================================"

# Configuración
CLIENTE_DIR="clientes"
CUENTAS_DIR="cuentas"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para log con colores
log_info() { echo -e "${BLUE}ℹ $1${NC} "; }
log_success() { echo -e "${GREEN} $1${NC} "; }
log_warning() { echo -e "${YELLOW} $1${NC} "; }
log_error() { echo -e "${RED} $1${NC} "; }

test_docker_image() {
    local image=$1
    log_info "Probando imagen: $image"
    if docker pull "$image" > /dev/null 2>&1; then
        log_success "$image - DISPONIBLE"
        return 0
    else
        log_warning "$image - NO DISPONIBLE"
        return 1
    fi
}

fix_docker_images() {
    log_info "Revisando imagenes DOCKER"
    
    log_info "Probando conexión a Docker Hub..."
    if ! docker pull hello-world > /dev/null 2>&1; then
        log_error "Problema de conexión a Docker Hub"
        log_info "Verifica tu conexión a internet y DNS"
        exit 1
    fi
    log_success "Conexión a Docker Hub OK"

    log_info "Buscando imagen Java 21 disponible..."
    local available_image=""
    
    if test_docker_image "eclipse-temurin:21-jre"; then
        available_image="eclipse-temurin:21-jre"
    elif test_docker_image "amazoncorretto:21"; then
        available_image="amazoncorretto:21"
    elif test_docker_image "openjdk:21"; then
        available_image="openjdk:21"
    elif test_docker_image "openjdk:17"; then
        available_image="openjdk:17"
        log_warning "Usando Java 17 como fallback (Java 21 no disponible)"
    else
        log_error "No se encontró ninguna imagen Java disponible"
        exit 1
    fi

    log_success "Imagen seleccionada: $available_image"

    log_info "Actualizando Dockerfiles..."
    
    cat > "./$CLIENTE_DIR/Dockerfile" << EOF
FROM $available_image

WORKDIR /app

RUN mkdir -p logs

COPY target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java \\$JAVA_OPTS -jar app.jar"]
EOF

    cat > "./$CUENTAS_DIR/Dockerfile" << EOF
FROM $available_image

WORKDIR /app

RUN mkdir -p logs

COPY target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["sh", "-c", "java \\$JAVA_OPTS -jar app.jar"]
EOF

    log_success "Dockerfiles actualizados con: $available_image"
}

main() {
    if ! command -v mvn &> /dev/null; then
        log_error "Maven no está instalado. Por favor instala Maven primero."
        exit 1
    fi

    log_info " ESTRUCTURA DEL PROYECTO..."
    if [ ! -d "$CLIENTE_DIR" ] || [ ! -d "$CUENTAS_DIR" ]; then
        log_error "Estructura de proyecto incorrecta"
        log_info "Directorios esperados: $CLIENTE_DIR/, $CUENTAS_DIR/"
        exit 1
    fi
    log_success "Estructura correcta encontrada"

    log_info "Compilando microservicios..."
    
    log_info "Compilando $CLIENTE_DIR..."
    cd "$CLIENTE_DIR"
    if mvn clean package -DskipTests; then
        log_success "$CLIENTE_DIR compilado correctamente"
    else
        log_error "Error compilando $CLIENTE_DIR"
        exit 1
    fi
    cd ..

    log_info "Compilando $CUENTAS_DIR..."
    cd "$CUENTAS_DIR"
    if mvn clean package -DskipTests; then
        log_success "$CUENTAS_DIR compilado correctamente"
    else
        log_error "Error compilando $CUENTAS_DIR"
        exit 1
    fi
    cd ..

    log_info "Preparando entorno Docker..."
    mkdir -p "$CLIENTE_DIR/logs" "$CUENTAS_DIR/logs"
    log_success "Directorios de logs creados"

    log_info "Deteniendo contenedores existentes..."
    docker-compose down

    log_info "Limpiando recursos no utilizados..."
    docker system prune -f

    log_info "Verificando imágenes Docker..."
    
    log_info "Construcción inicial..."
    if docker-compose build --no-cache > /dev/null 2>&1; then
        log_success "Imágenes Docker OK - Continuando..."
    else
        log_warning "Problemas con imágenes Docker - Aplicando solución automática..."
        fix_docker_images
    fi

    log_info "Contenedores Docker..."
    if docker-compose up --build -d; then
        log_success "Contenedores levantados correctamente"
    else
        log_error "Error crítico levantando contenedores"
        log_info "Ejecuta este script nuevamente para reparación automática"
        exit 1
    fi

    log_info "Inicialización de servicios..."
    
    log_info "Esperando PostgreSQL..."
    until docker exec bank-postgres pg_isready -U postgresadmin > /dev/null 2>&1; do
        log_info "PostgreSQL en proceso, esperando..."
        sleep 5
    done
    log_success "PostgreSQL arriba"

    log_info "Esperando ActiveMQ..."
    sleep 15

    log_info "Esperando que los microservicios inicien..."
    
    # Esperar un tiempo fijo para que los servicios Spring Boot inicien
    log_info "Dando tiempo para que los servicios inicien (30 segundos)..."
    sleep 30

    log_success "cliente-service debería estar en puerto 8081"
    log_success "cuenta-service debería estar en puerto 8082"

    echo ""
    log_success "Microservicios desplegados"
    echo "================================================"
    echo ""
    echo "SERVICIOS DISPONIBLES:"
    echo "   Cliente Service:    http://localhost:8081"
    echo "   Cuenta Service:     http://localhost:8082" 
    echo "   ActiveMQ Console:   http://localhost:8161"
    echo "   PostgreSQL:         localhost:5432"
    echo ""
    echo "ENDPOINTS PRINCIPALES:"
    echo "   Clientes:           http://localhost:8081/clientes"
    echo "   Cuentas:            http://localhost:8082/cuentas"
    echo "   Movimientos:        http://localhost:8082/movimientos"
    echo ""
    echo "COMANDOS ÚTILES:"
    echo "   Ver logs:          docker-compose logs -f"
    echo "   Ver logs cliente:  docker-compose logs -f cliente-service"
    echo "   Ver logs cuenta:   docker-compose logs -f cuenta-service"
    echo "   Parar servicios:   docker-compose down"
    echo "   Estado servicios:  docker-compose ps"
    echo ""
    echo "CREDENCIALES:"
    echo "   PostgreSQL:     usuario=postgresadmin, password=postgres"
    echo "   ActiveMQ:       usuario=admin, password=admin"
    echo ""
    echo "💡 NOTA: Los servicios pueden tomar hasta 1 minuto en estar completamente operativos"
}

trap 'log_error "Script interrumpido"; exit 1' INT TERM
main "$@"