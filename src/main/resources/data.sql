INSERT INTO EMPLOYEES (name, last_Name, city, age, telephone_Number, mail, 	EMPLOYEE_STATUS, ASSIGNED_FOR_JOBS) VALUES ('Anna', 'Nowak', 'Poznań', 30, 792, 'an@gmail.com', 'ACTIVE', false);
INSERT INTO EMPLOYEES (name, last_Name, city, age, telephone_Number, mail, 	EMPLOYEE_STATUS, ASSIGNED_FOR_JOBS) VALUES ('Zofia', 'Kowalska', 'Kraków', 50, 111, 'zk@o2.pl', 'ACTIVE', false);
INSERT INTO EMPLOYEES (name, last_Name, city, age, telephone_Number, mail, 	EMPLOYEE_STATUS, ASSIGNED_FOR_JOBS) VALUES ('Marian', 'Pach', 'Wrocław', 20, 333, 'mp@wp.pl', 'ACTIVE', false);
INSERT INTO EMPLOYEES (name, last_Name, city, age, telephone_Number, mail, 	EMPLOYEE_STATUS, ASSIGNED_FOR_JOBS) VALUES ('Edek', 'Ziuk', 'Gdańsk', 40, 444, 'ez@o2.pl', 'ACTIVE', false);
INSERT INTO EMPLOYEES (name, last_Name, city, age, telephone_Number, mail, 	EMPLOYEE_STATUS, ASSIGNED_FOR_JOBS) VALUES ('Jim', 'Kowal', 'Warszawa', 18, 555, 'jk@o2.pl', 'ACTIVE', false);

INSERT INTO CLIENTS (name, last_Name, city, address, postal_Code, telephone_Number, mail) VALUES ('Ben', 'Benek', 'Koziegłowy', 'Cienka 10a/155', '22-222', '999', 'BB@wp.pl');
INSERT INTO CLIENTS (name, last_Name, city, address, postal_Code, telephone_Number, mail) VALUES ('Zuza', 'Zuz', 'Suchy Las', 'Słoneczna 5', '33-333', '555', 'zz@gmail.pl');

INSERT INTO JOBS (city, date_Of_Job, jobs_Address, jobs_Postal_Code, sort_Of_Job, estimated_Time, number_Of_Children, CLIENT_ID, JOB_STATUS) VALUES ('Jelenia Góra', '2019-01-01', 'Długa 11/11', '00-999', 'WESELE', 6, 10, 1, 'ACTIVE');
INSERT INTO JOBS (city, date_Of_Job, jobs_Address, jobs_Postal_Code, sort_Of_Job, estimated_Time, number_Of_Children, CLIENT_ID, JOB_STATUS) VALUES ('Wrocław', '2019-12-25', 'Krótka 22/22', '99-000', 'URODZINY', 2, 5, 1, 'ACTIVE');
INSERT INTO JOBS (city, date_Of_Job, jobs_Address, jobs_Postal_Code, sort_Of_Job, estimated_Time, number_Of_Children, CLIENT_ID, JOB_STATUS) VALUES ('Poznań', '2019-09-10', 'Długa 33/33', '22-333', 'FESTYN', 1, 3, 1, 'ACTIVE');
INSERT INTO JOBS (city, date_Of_Job, jobs_Address, jobs_Postal_Code, sort_Of_Job, estimated_Time, number_Of_Children, CLIENT_ID, JOB_STATUS) VALUES ('Warszawa', '2019-06-21', 'Słoneczna 5', '44-444', 'WESELE', 5, 9, 2, 'ACTIVE');
INSERT INTO JOBS (city, date_Of_Job, jobs_Address, jobs_Postal_Code, sort_Of_Job, estimated_Time, number_Of_Children, CLIENT_ID, JOB_STATUS) VALUES ('Bydgoszcz', '2019-03-31', 'Miłą 55', '55-555', 'URODZINY', 3, 4, 2, 'ACTIVE');


-- INSERT INTO CLIENTS_JOBS (CLIENT_ID, JOBS_ID) VALUES (1, 1);
-- INSERT INTO CLIENTS_JOBS (CLIENT_ID, JOBS_ID) VALUES (1, 2);
-- INSERT INTO CLIENTS_JOBS (CLIENT_ID, JOBS_ID) VALUES (1, 3);
-- INSERT INTO CLIENTS_JOBS (CLIENT_ID, JOBS_ID) VALUES (2, 4);
-- INSERT INTO CLIENTS_JOBS (CLIENT_ID, JOBS_ID) VALUES (2, 5);

-- INSERT INTO JOBS_EMPLOYEES (WORKED_JOBS_ID, EMPLOYEES_ID) VALUES (1, 1);
-- INSERT INTO JOBS_EMPLOYEES (WORKED_JOBS_ID, EMPLOYEES_ID) VALUES (2, 1);
-- INSERT INTO JOBS_EMPLOYEES (WORKED_JOBS_ID, EMPLOYEES_ID) VALUES (1, 2);

