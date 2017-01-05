-- You can use this file to load seed data into the database using SQL statements

-- Extension Table 
--insert into Extension (code, value, description) values ('RRHH', '105', 'Recursos Humanos')

-- Role Table
insert into role (name) values ('Administrador')
insert into role (name) values ('Estandar')

-- User Table
insert into user (name, username, password, email, roleId, locale, theme) values ('Administrador Sistema', 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'admin@codar4j.com', 1, 'en', 'aristo')

-- Permission Table
insert into permission (role, acl) values ('Administrador', '*')
insert into permission (role, acl) values ('Estandar', '*')

-- Parameter Table
--insert into parameter (name, value, description) values('config.file.path', 'C:/JavaApps/atorres/config/', 'Path de Config.properties')
insert into parameter (name, value, description) values('cli.host', '192.168.107.150', 'Host de Asterisk')
insert into parameter (name, value, description) values('cli.port', '5060', 'Puerto de Asterisk')
insert into parameter (name, value, description) values('cli.username', 'root', 'Usuario de Asterisk')
insert into parameter (name, value, description) values('asterisk.host', '0926324252', 'Password de Asterisk')
insert into parameter (name, value, description) values('shutdown.os', 'shutdown -h now', 'Comando para apagar el servidor')
insert into parameter (name, value, description) values('restart.os', 'shutdown -r now', 'Comando para reiniciar el servidor')
insert into parameter (name, value, description) values('change.date.os', 'date --set', 'Comando para cambiar la hora del servidor')
insert into parameter (name, value, description) values('start.service.asterisk', 'service asterisk start', 'Comando para iniciar el servicio asterisk')
insert into parameter (name, value, description) values('stop.service.asterisk', 'service asterisk stop', 'Comando para detener el servicio asterisk')
insert into parameter (name, value, description) values('restart.service.asterisk', 'service asterisk restart', 'Comando para reiniciar el servicio asterisk')
insert into parameter (name, value, description) values('asterisk.log.url', 'http://192.168.107.150/', 'Virtual Host de logs de asterisk')
insert into parameter (name, value, description) values('asterisk.logMessages', 'messages', 'Nombre de log de asterisk')
insert into parameter (name, value, description) values('asterisk.logQueue1', 'queue_log', 'Nombre de log de asterisk')
insert into parameter (name, value, description) values('asterisk.logQueue2', 'queue_log_to_file', 'Nombre de log de asterisk')
insert into parameter (name, value, description) values('asterisk.logQueue3', 'queue_log_name', 'Nombre de log de asterisk')
insert into parameter (name, value, description) values('service.mysql', 'mysqld', 'Nombre de proceso de sistema operativo MySql')
insert into parameter (name, value, description) values('service.jboss', 'java', 'Nombre de proceso de sistema operativo jboss')
insert into parameter (name, value, description) values('service.asterisk', 'asterisk', 'Nombre de proceso de sistema operativo asterisk')
insert into parameter (name, value, description) values('asterisk.show.channels', 'core show channels', 'Comando para mostrar los channels')
insert into parameter (name, value, description) values('asterisk.show.peers', 'sip show peers', 'Comando para mostrar los peers')
insert into parameter (name, value, description) values('asterisk.recorder.url', 'http://grabaciones_asterisk', 'Virtual Host de grabaciones de asterisk')
insert into parameter (name, value, description) values('asterisk.recorder.path', '/usr/grabaciones/', 'Path de grabaciones de asterisk')
insert into parameter (name, value, description) values('asterisk.recorder.ext', 'wav', 'Extension de grabaciones')
--insert into parameter (name, value, description) values('state.name', 'State.Name.eq', 'Nombre de proceso de sistema operativo MySql')


-- Context Table
insert into context (name, detail) values('demo', 'es un contexto de prueba')