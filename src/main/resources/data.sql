INSERT INTO EMPLOYEES (name, last_Name, city, age, telephone_Number, mail) VALUES ('Anna', 'Nowak', 'Poznań', 30, 792, 'an@gmail.com');
INSERT INTO EMPLOYEES (name, last_Name, city, age, telephone_Number, mail) VALUES ('Zofia', 'Kowalska', 'Kraków', 50, 111, 'zk@o2.pl');
INSERT INTO EMPLOYEES (name, last_Name, city, age, telephone_Number, mail) VALUES ('Marian', 'Pach', 'Wrocław', 20, 333, 'mp@wp.pl');
INSERT INTO EMPLOYEES (name, last_Name, city, age, telephone_Number, mail) VALUES ('Edek', 'Ziuk', 'Gdańsk', 40, 444, 'ez@o2.pl');
INSERT INTO EMPLOYEES (name, last_Name, city, age, telephone_Number, mail) VALUES ('Jim', 'Kowal', 'Warszawa', 18, 555, 'jk@o2.pl');

INSERT INTO JOBS (client_Name, client_Last_Name, city, date_Of_Job, jobs_Address, jobs_Postal_Code, sort_Of_Job, estimated_Time, number_Of_Children) VALUES ('Marian', 'Pol', 'Jelenia Góra', '2019-01-01', 'Długa 11/11', '00-999', 'WESELE', 6, 10);
INSERT INTO JOBS (client_Name, client_Last_Name, city, date_Of_Job, jobs_Address, jobs_Postal_Code, sort_Of_Job, estimated_Time, number_Of_Children) VALUES ('Marianna', 'Bęc', 'Wrocław', '2019-12-25', 'Krótka 22/22', '99-000', 'URODZINY', 2, 5);

INSERT INTO CLIENTS (name, last_Name, city, address, postal_Code, telephone_Number, mail) VALUES ('Ben', 'Benek', 'Koziegłowy', 'Cienka 10a/155', '22-222', '999', 'BB@wp.pl');
INSERT INTO CLIENTS (name, last_Name, city, address, postal_Code, telephone_Number, mail) VALUES ('Zuza', 'Zuz', 'Suchy Las', 'Słoneczna 5', '33-333', '555', 'zz@gmail.pl');

INSERT INTO JOBS_EMPLOYEES (WORKED_JOBS_ID, EMPLOYEES_ID) VALUES (1, 1);
INSERT INTO JOBS_EMPLOYEES (WORKED_JOBS_ID, EMPLOYEES_ID) VALUES (2, 1);
INSERT INTO JOBS_EMPLOYEES (WORKED_JOBS_ID, EMPLOYEES_ID) VALUES (1, 2);

