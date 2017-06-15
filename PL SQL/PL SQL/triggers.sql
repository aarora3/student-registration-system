-----------------------------------
-- *** Q10 ***
-----------------------------------

-- Triggers:

-- For Logs table:

--	1. Logs table entry on insert in enrollments
	
create or replace trigger "ENROLLMENTS_INSERT_LOGS_T1"
AFTER
insert on "ENROLLMENTS"
for each row
begin
insert into logs values (logid.nextval, USER, sysdate, 'enrollments', 'insert', :new.sid || ',' || :new.classid);
end;​
	
	
	
--	2. Logs table entry on delete in enrollments

create or replace trigger "ENROLLMENTS_DELETE_LOGS_T1"
AFTER
delete on "ENROLLMENTS"
for each row
begin
insert into logs values (logid.nextval, USER, sysdate, 'enrollments', 'delete', :old.sid || ',' || :old.classid);
end;​


--	3. Logs table entry on insert in students


create or replace trigger "STUDENTS_INSERT_LOGS_T1"
AFTER
insert on "STUDENTS"
for each row
begin
insert into logs values (logid.nextval, USER, sysdate, 'students', 'insert', :new.sid);
end;​



--	4. Logs table entry on delete in students


create or replace trigger "STUDENTS_DELETE_LOGS_T1"
AFTER
delete on "STUDENTS"
for each row
begin
insert into logs values (logid.nextval, USER, sysdate, 'students', 'delete', :old.sid);
end;​



-- Other dependencies:

-- 	1. delete from enrollments on delete in students

create or replace trigger "STUDENTS_DELETE_ENROLLMENTS_T1"
BEFORE
delete on "STUDENTS"
for each row
begin
delete from enrollments where sid = :old.sid;
end;​


--	2. increment class size on insert in enrollments

create or replace trigger "ENROLLMENTS_INSERT_CLASSES_T1"
AFTER
insert on "ENROLLMENTS"
for each row
begin
update classes 
    set class_size = class_size + 1 
    where classid = :new.classid;
end;​


--	3. decrement class size on delete in enrollments

create or replace trigger "ENROLLMENTS_DELETE_CLASSES_T1"
AFTER
delete on "ENROLLMENTS"
for each row
begin
update classes 
    set class_size = class_size - 1 
    where classid = :old.classid;
end;​