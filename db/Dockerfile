# Use the latest MySQL image
FROM mysql/mysql-server:latest
COPY world.sql /docker-entrypoint-initdb.d
# Set the root password
ENV MYSQL_ROOT_PASSWORD example
ENV MYSQL_ROOT_HOST=%
