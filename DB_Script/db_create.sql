--Script to create DB structure for javsterisk in MySql
-- this script create the DB tables and insert initial data

-- create database
create database javsterisk;

-- create user for database
create user 'javsterisk'@'localhost' identified by 'javsterisk';

-- grant privileges for user on database
grant all privileges on javsterisk.* to 'javsterisk'@'localhost';

-- Create javsterisk DB objects

    create table cdr (
        id integer not null auto_increment,
        accountcode varchar(20),
        amaflags varchar(50),
        answer datetime not null,
        billsec float,
        calldate datetime not null,
        channel varchar(50),
        clid varchar(80) not null,
        dcontext varchar(80) not null,
        disposition varchar(10),
        dst varchar(80) not null,
        dstchannel varchar(50),
        duration float,
        end datetime not null,
        lastapp varchar(200) not null,
        lastdata varchar(200) not null,
        src varchar(80) not null,
        uniqueid varchar(32) not null,
        userfield float,
        primary key (id)
    ) ENGINE=InnoDB;

    create table context (
        id bigint not null auto_increment,
        detail varchar(128) not null,
        name varchar(20) not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table extensions (
        context varchar(20) not null,
        exten varchar(20) not null,
        priority tinyint not null,
        app varchar(20) not null,
        appdata varchar(128),
        id integer not null,
        primary key (context, exten, priority)
    ) ENGINE=InnoDB;

    create table extensionsWizzard (
        id integer not null auto_increment,
        digito varchar(255) not null,
        firstAlert varchar(255),
        firstExtension varchar(255),
        limite bit not null,
        longitud integer not null,
        record bit not null,
        secondAlert varchar(255),
        secondExtension varchar(255),
        timeLimit varchar(255),
        timeWait varchar(255),
        transfer bit not null,
        wait bit not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table logging_event (
        id bigint not null auto_increment,
        date datetime,
        exception varchar(255),
        level varchar(255),
        line varchar(255),
        logger varchar(255),
        message varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

    create table parameter (
        id bigint not null auto_increment,
        description varchar(255),
        name varchar(255) not null,
        value varchar(255) not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table permission (
        id bigint not null auto_increment,
        acl varchar(255) not null,
        role varchar(255) not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table role (
        id bigint not null auto_increment,
        name varchar(255) not null,
        primary key (id)
    ) ENGINE=InnoDB;

    create table sip_buddies (
        id integer not null auto_increment,
        accountcode varchar(20),
        allow varchar(100),
        amaflags varchar(7),
        callerid varchar(80),
        callgroup varchar(10),
        cancallforward varchar(3),
        canreinvite varchar(3),
        context varchar(80) not null,
        defaultip varchar(15),
        defaultuser varchar(80) not null,
        deny varchar(95),
        disallow varchar(100),
        dtmfmode varchar(7),
        fromdomain varchar(80),
        fromuser varchar(80),
        fullcontact varchar(80),
        host varchar(31) not null,
        insecure varchar(4),
        ipaddr varchar(45),
        language varchar(2),
        lastms integer not null,
        mailbox varchar(50),
        mask varchar(95),
        md5secret varchar(80),
        musiconhold varchar(100),
        name varchar(80) not null,
        nat varchar(5) not null,
        permit varchar(95),
        pickupgroup varchar(10),
        port varchar(5),
        qualify varchar(3),
        regexten varchar(80),
        regseconds integer not null,
        regserver varchar(100),
        restrictcid varchar(1),
        rtpholdtimeout varchar(3),
        rtptimeout varchar(3),
        secret varchar(80),
        type varchar(6) not null,
        useragent varchar(255),
        primary key (id)
    ) ENGINE=InnoDB;

    create table user (
        id bigint not null auto_increment,
        email varchar(255) not null,
        locale varchar(255) not null,
        name varchar(255) not null,
        password varchar(255) not null,
        theme varchar(255) not null,
        username varchar(10) not null,
        roleId bigint not null,
        sipBuddies_id integer,
        primary key (id)
    ) ENGINE=InnoDB;

    create table wizzard_extensions (
        wizzardId integer,
        context varchar(20) not null,
        exten varchar(20) not null,
        priority tinyint not null,
        primary key (context, exten, priority)
    ) ENGINE=InnoDB;

    alter table context 
        add constraint UKljevc9gi1h8ack6bfc1e97351 unique (name);

    alter table parameter 
        add constraint UKll10bbifmynb347ajltx7bi60 unique (name);

    alter table permission 
        add constraint UK1id7cy358rf7ngs7kojegpvds unique (role);

    alter table role 
        add constraint UK8sewwnpamngi6b1dwaa88askk unique (name);

    alter table sip_buddies 
        add constraint UK_nvuj5abdub6rhmkrx88jtfddd unique (name);

    alter table user 
        add constraint UKsb8bbouer5wak8vyiiy4pf2bx unique (username);

    alter table user 
        add constraint FK8yhl7wdo39n3ee04f8rpajces 
        foreign key (roleId) 
        references role (id);

    alter table user 
        add constraint FK1nv5rubbkkhlmuto3cxb25rms 
        foreign key (sipBuddies_id) 
        references sip_buddies (id);

    alter table wizzard_extensions 
        add constraint FKamefroux455rvxw8rnr95f1ry 
        foreign key (wizzardId) 
        references extensionsWizzard (id);

    alter table wizzard_extensions 
        add constraint FK18fan131j1lxsuoxs229irg1j 
        foreign key (context, exten, priority) 
        references extensions (context, exten, priority);
        
        
 -- Insert data
        
-- Extension Table 
--insert into Extension (code, value, description) values ('RRHH', '105', 'Recursos Humanos');

-- Role Table
insert into role (name) values ('Administrador');
insert into role (name) values ('Estandar');

-- User Table
insert into user (name, username, password, email, roleId, locale, theme) values ('Administrador Sistema', 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'admin@codar4j.com', 1, 'en', 'aristo');

-- Permission Table
insert into permission (role, acl) values ('Administrador', '*');
insert into permission (role, acl) values ('Estandar', '*');

-- Parameter Table
--insert into parameter (name, value, description) values('config.file.path', 'C:/JavaApps/atorres/config/', 'Path de Config.properties');
insert into parameter (name, value, description) values('cli.host', '192.168.107.150', 'Host de Asterisk');
insert into parameter (name, value, description) values('cli.port', '5060', 'Puerto de Asterisk');
insert into parameter (name, value, description) values('cli.username', 'root', 'Usuario de Asterisk');
insert into parameter (name, value, description) values('asterisk.host', '0926324252', 'Password de Asterisk');
insert into parameter (name, value, description) values('shutdown.os', 'shutdown -h now', 'Comando para apagar el servidor');
insert into parameter (name, value, description) values('restart.os', 'shutdown -r now', 'Comando para reiniciar el servidor');
insert into parameter (name, value, description) values('change.date.os', 'date --set', 'Comando para cambiar la hora del servidor');
insert into parameter (name, value, description) values('start.service.asterisk', 'service asterisk start', 'Comando para iniciar el servicio asterisk');
insert into parameter (name, value, description) values('stop.service.asterisk', 'service asterisk stop', 'Comando para detener el servicio asterisk');
insert into parameter (name, value, description) values('restart.service.asterisk', 'service asterisk restart', 'Comando para reiniciar el servicio asterisk');
insert into parameter (name, value, description) values('asterisk.log.url', 'http://192.168.107.150/', 'Virtual Host de logs de asterisk');
insert into parameter (name, value, description) values('asterisk.logMessages', 'messages', 'Nombre de log de asterisk');
insert into parameter (name, value, description) values('asterisk.logQueue1', 'queue_log', 'Nombre de log de asterisk');
insert into parameter (name, value, description) values('asterisk.logQueue2', 'queue_log_to_file', 'Nombre de log de asterisk');
insert into parameter (name, value, description) values('asterisk.logQueue3', 'queue_log_name', 'Nombre de log de asterisk');
insert into parameter (name, value, description) values('service.mysql', 'mysqld', 'Nombre de proceso de sistema operativo MySql');
insert into parameter (name, value, description) values('service.jboss', 'java', 'Nombre de proceso de sistema operativo jboss');
insert into parameter (name, value, description) values('service.asterisk', 'asterisk', 'Nombre de proceso de sistema operativo asterisk');
insert into parameter (name, value, description) values('asterisk.show.channels', 'core show channels', 'Comando para mostrar los channels');
insert into parameter (name, value, description) values('asterisk.show.peers', 'sip show peers', 'Comando para mostrar los peers');
insert into parameter (name, value, description) values('asterisk.recorder.url', 'http://grabaciones_asterisk', 'Virtual Host de grabaciones de asterisk');
insert into parameter (name, value, description) values('asterisk.recorder.path', '/usr/grabaciones/', 'Path de grabaciones de asterisk');
insert into parameter (name, value, description) values('asterisk.recorder.ext', 'wav', 'Extension de grabaciones');

-- Context Table
--insert into context (name, detail) values('demo', 'es un contexto de prueba');