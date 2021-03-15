CREATE TABLE application.guideline (
	id bigserial NOT NULL,
	created_date timestamp NOT NULL,
	modified_date timestamp NULL,
	description varchar(255) NOT NULL,
	CONSTRAINT guideline_pkey PRIMARY KEY (id)
);

CREATE TABLE application."user" (
	id bigserial NOT NULL,
	created_date timestamp NOT NULL,
	modified_date timestamp NULL,
	cpf varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	"password" varchar(255) NOT NULL,
	username varchar(255) NOT NULL,
	CONSTRAINT uk_2qv8vmk5wxu215bevli5derq UNIQUE (cpf),
	CONSTRAINT uk_ob8kqyqqgmefl0aco34akdtpe UNIQUE (email),
	CONSTRAINT uk_sb8bbouer5wak8vyiiy4pf2bx UNIQUE (username),
	CONSTRAINT user_pkey PRIMARY KEY (id)
);

CREATE TABLE application."session" (
	id bigserial NOT NULL,
	created_date timestamp NOT NULL,
	modified_date timestamp NULL,
	expiration_date timestamp NOT NULL,
	kafka_session_status varchar(255) NULL,
	guideline_id int8 NOT NULL,
	CONSTRAINT session_pkey PRIMARY KEY (id),
	CONSTRAINT uk_qi8orx4m8u132ojtw9u2eecb3 UNIQUE (guideline_id),
	CONSTRAINT fk84xkltnivkf6dhhlabx8jpd2t FOREIGN KEY (guideline_id) REFERENCES application.guideline(id)
);


CREATE TABLE application.user_roles (
	user_id int8 NOT NULL,
	roles int4 NULL,
	CONSTRAINT fk55itppkw3i07do3h7qoclqd4k FOREIGN KEY (user_id) REFERENCES application."user"(id)
);

CREATE TABLE application.vote (
	id bigserial NOT NULL,
	created_date timestamp NOT NULL,
	modified_date timestamp NULL,
	vote bool NOT NULL,
	session_id int8 NOT NULL,
	user_id int8 NOT NULL,
	CONSTRAINT ukeepselty6mr6bkck8k20lllmf UNIQUE (user_id, session_id),
	CONSTRAINT vote_pkey PRIMARY KEY (id),
	CONSTRAINT fk52gw0umav8w85f9jjxspuj86f FOREIGN KEY (session_id) REFERENCES application.session(id),
	CONSTRAINT fkcsaksoe2iepaj8birrmithwve FOREIGN KEY (user_id) REFERENCES application."user"(id)
);

INSERT INTO application."user" (created_date,modified_date,cpf,email,"password",username) VALUES
	 ('2021-03-15 00:43:55.967','2021-03-15 00:43:55.967','94935927070','admin@email.com','$2a$12$yWhBrouIICTmRyfDM3fhK.oKZ5ISLlCguswV4A.UhG02X5zfl.66C','admin');
INSERT INTO application.user_roles (user_id,roles) VALUES
	 (1,0);