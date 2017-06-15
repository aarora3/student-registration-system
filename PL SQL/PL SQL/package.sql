
-- PACKAGE FOR STORED PROCEDURES

create or replace package Proj2_student_reg_package is
    type data_cursor is ref cursor;
    
-- DECLARATION OF ALL STORED PROCEDURES

procedure show_students(student_ref_cursor OUT data_cursor );

procedure show_courses(courses_ref_cursor OUT data_cursor);

procedure show_prerequisites(prerequisites_ref_cursor OUT data_cursor);

procedure show_classes(classes_ref_cursor OUT data_cursor);

procedure show_enrollments(enrollments_ref_cursor OUT data_cursor);

procedure show_logs(logs_ref_cursor OUT data_cursor);

procedure add_students(sidIn IN students.sid%type, fnameIn IN students.firstname%type,
                        lnameIn IN students.lastname%type, statusIn IN students.status%type,
                        gpaIn IN students.gpa%type, emailIn IN students.email%type,
                        ret_message OUT varchar);

procedure show_students_classes(sidIN IN students.sid%type,ret_message OUT varchar2 ,students_classes_cursor OUT data_cursor);



procedure show_prereq(dept_codeIn in prerequisites.dept_code%type,course_noIn in prerequisites.course_no%type,results out varchar);




procedure show_classes_student_details(classidIn IN classes.classid%type,ret_message OUT varchar2,classes_details_cursor OUT data_cursor
,student_details_cursor OUT data_cursor);


procedure enroll_student_in_class(classidIn in classes.classid%type,sidIn in students.sid%type,results out varchar);



procedure drop_student_from_class(classidIn in classes.classid%type,sidIn in students.sid%type,results out varchar);

procedure delete_student(sidIn in students.sid%type,results out varchar);


end Proj2_student_reg_package;
/
    
	
-- STORED PROCEDURES IMPLEMENTATION
	
create or replace package body Proj2_student_reg_package as

-----------------------------------
-- *** Q2 ***
-----------------------------------

-- SHOW STUDENTS stored procedure

procedure show_students(student_ref_cursor OUT data_cursor )
is
begin
open student_ref_cursor for select * from students;
end show_students;

-- SHOW COURSES stored procedure

procedure show_courses(courses_ref_cursor OUT data_cursor)
is
begin
open courses_ref_cursor for select * from courses;
end show_courses;

-- SHOW PREREQUISITES stored procedure

procedure show_prerequisites(prerequisites_ref_cursor OUT data_cursor)
is
begin
open prerequisites_ref_cursor for select * from prerequisites;
end show_prerequisites;

-- SHOW CLASSES stored procedure

procedure show_classes(classes_ref_cursor OUT data_cursor)
is 
begin
open classes_ref_cursor for select * from classes;
end show_classes;

-- SHOW ENROLLMENTS stored procedure

procedure show_enrollments(enrollments_ref_cursor OUT data_cursor)
is 
begin
open enrollments_ref_cursor for select * from enrollments;
end show_enrollments;

-- SHOW LOGS stored procedure

procedure show_logs(logs_ref_cursor OUT data_cursor)
is
begin
open logs_ref_cursor for select * from logs;
end show_logs;

-----------------------------------
-- *** Q3 ***
-----------------------------------

-- add a student stored procedure

procedure add_students(sidIn IN students.sid%type, fnameIn IN students.firstname%type,
                        lnameIn IN students.lastname%type, statusIn IN students.status%type,
                        gpaIn IN students.gpa%type, emailIn IN students.email%type,
                        ret_message OUT varchar)
is
begin
insert into students values(sidIn ,fnameIn,lnameIn,statusIn,gpaIn,emailIn);
COMMIT;
ret_message := 'success';

EXCEPTION
WHEN OTHERS THEN
ret_message := 'error';
end add_students;


procedure show_students_classes(sidIn IN students.sid%type,ret_message OUT varchar2 ,students_classes_cursor OUT data_cursor)
is
sidCheck number(2);
sidEnrolledCheck number(2);
begin
select count(sid) into sidCheck from students where sid = sidIn;
select count(sid) into sidEnrolledCheck from enrollments where sid = sidIn;

if sidCheck = 0 then	-- check if student exists
ret_message := 'The sid is invalid';
elsif sidEnrolledCheck = 0 then	-- check if sudent has taken any course
ret_message := 'The student has not taken any course';
else
open students_classes_cursor for
select s.sid,s.lastname,s.status,c.classid,c.dept_code||c.course_no as course,co.title,c.year,c.semester from students s,courses co,classes c,enrollments e
where e.sid = s.sid and c.dept_code || c.course_no = co.dept_code || co.course_no and e.classid = c.classid and s.sid = sidIn;
ret_message := 'success';
end if;

end show_students_classes;



-----------------------------------
-- *** Q5 ***
-----------------------------------

-- show prerequisites stored procedure



PROCEDURE show_prereq(dept_codeIn in prerequisites.dept_code%type,course_noIn in prerequisites.course_no%type,results out varchar)
as

dep_code varchar2(4);
cr_no number(3);
temp_res varchar(9000);

cursor prereq_cursor is
(select pre_dept_code, pre_course_no from prerequisites where dept_code = dept_codeIn and course_no = course_noIn);
 prereq_rec prereq_cursor%rowtype;

cursor course_cursor is
(select * from courses where dept_code = dept_codeIn and course_no = course_noIn);

course_rec course_cursor%rowtype;

BEGIN


open course_cursor;
fetch course_cursor into course_rec;

if (course_cursor%notfound) then       -- check if course exists
    results := 'Entered course not found.';
else

    open prereq_cursor;    
    fetch prereq_cursor into prereq_rec;
    
    while prereq_cursor%found loop
            dep_code := prereq_rec.pre_dept_code;
            cr_no := prereq_rec.pre_course_no;
            if (prereq_rec.pre_dept_code IS NOT NULL) then
                IF (results IS not NULL) THEN
                    results := results || chr(10) || dep_code || cr_no;
                end if;
                IF (results IS NULL) THEN
                    results := dep_code || cr_no;
                end if;

                show_prereq(dep_code, cr_no, temp_res);		-- recursive call to stored procedure
            
                IF (temp_res IS not NULL) THEN
                    results := results || chr(10) || temp_res;
                end if;
            else
                return;
            end if;
        
            fetch prereq_cursor into prereq_rec;
     end loop;
             
end if;            
            
END;


-----------------------------------
-- *** Q6 ***
-----------------------------------


-- show class details and enrolled students

procedure show_classes_student_details(classidIn IN classes.classid%type,ret_message OUT varchar2,classes_details_cursor OUT data_cursor,student_details_cursor OUT data_cursor)
is
classid_check number(2);
class_students_check number(2);
begin
select count(classid) into classid_check from classes where classid = classidIn;
select count(sid) into class_students_check from enrollments where classid = classidIn;

if classid_check = 0 then	-- check if course exists
ret_message := 'The classid is invalid';
elsif class_students_check = 0 then	-- check if student enrolled in class
ret_message := 'No student is enrolled in the class';
else
open classes_details_cursor for 
select c.classid,co.title,c.semester,c.year from classes c,courses co where c.dept_code || c.course_no = co.dept_code || co.course_no and c.classid = classidIn;

open student_details_cursor for
select s.sid,s.lastname from students s,enrollments e where e.classid = classidIn and s.sid = e.sid;
ret_message := 'success';
end if;

end show_classes_student_details;



-----------------------------------
-- *** Q7 ***
-----------------------------------


-- enroll student in a class procedure

procedure enroll_student_in_class(classidIn in classes.classid%type,sidIn in students.sid%type, results out varchar)

is

cursor stu_cursor is
select * from students where sid = sidIn;
 stu_rec stu_cursor%rowtype;

 
cursor classes_cursor is
select * from classes where classid = classidIn;
 classes_rec classes_cursor%rowtype;


cursor getEnrolledStu_cursor is
select sid, firstname from students where sid in (select sid from enrollments where classid = classidIn);
 getEnrolledStu_rec getEnrolledStu_cursor%rowtype;

cursor getTitle_cursor is
select title from courses t1, classes t2 where t1.dept_code = t2.dept_code and t1.course_no = t2.course_no and classid = classidIn;
 getTitle_rec getTitle_cursor%rowtype;

cursor Course_cursor is
select (dept_code || course_no) as course from classes where classid = classidIn;
 Course_rec Course_cursor%rowtype;
 
cursor coursesTaken_cursor is
select (dept_code || course_no) as course from classes 
where classid in (select classid from enrollments
where sid = sidIn and 
classid in (select classid from classes where (semester, year) in (select semester, year from classes where classid = classidIn)));
 coursesTaken_rec coursesTaken_cursor%rowtype;

  
cursor enrollCount_cursor is
select count(*) as count from enrollments
where sid = sidIn and 
classid in (select classid from classes where (semester, year) in (select semester, year from classes where classid = classidIn));
 enrollCount_rec enrollCount_cursor%rowtype;
  
cursor prereq_cursor is
(select (pre_dept_code || pre_course_no) as course from prerequisites 
where (dept_code, course_no) in (select dept_code, course_no from classes where classid = classidIn));
 prereq_rec prereq_cursor%rowtype;
 
cursor completecourses_cursor is
select (dept_code || course_no) as course from classes 
where classid in (select classid from enrollments where sid = sidIn and lgrade in ('A','B','C','D'));
 completecourses_rec completecourses_cursor%rowtype;

prereq_flag boolean;

begin

prereq_flag := false; --flag to check if any prereq courses not completed

open stu_cursor;
fetch stu_cursor into stu_rec;

if (stu_cursor%notfound) then
    results := 'This sid is invalid';
else 

    open classes_cursor;
    fetch classes_cursor into classes_rec;
    
    if (classes_cursor%notfound) then
        results := 'This classid is invalid';
    else 
        
        if(classes_rec.class_size >= classes_rec.limit) then
            results := 'Class is closed';  
        else
        
            open Course_cursor;
            fetch Course_cursor into Course_rec;
        
            open coursesTaken_cursor;
            fetch coursesTaken_cursor into coursesTaken_rec;
            
            --check if the student has already enrolled into the course with given classid in its same year and semester
            
            while coursesTaken_cursor%found loop
                
                if (Course_rec.course = coursesTaken_rec.course) then 
                    results := 'The student is already in the class.';
                    return;
                end if;
                fetch coursesTaken_cursor into coursesTaken_rec;
            end loop;
            
            open enrollCount_cursor;
            fetch enrollCount_cursor into enrollCount_rec;
                
            if (enrollCount_cursor%found) then
                if (enrollCount_rec.count = 4) then
                    results := 'Students cannot be enrolled in more than four classes in the same semester';
                    return;
                end if;
                    
                open prereq_cursor;
                fetch prereq_cursor into prereq_rec;
                open completecourses_cursor;
                while prereq_cursor%found loop
                    fetch completecourses_cursor into completecourses_rec;
                    
                    while completecourses_cursor%found loop
                                    
                        if (completecourses_rec.course = prereq_rec.course) then
                            prereq_flag := true;
                        end if;
                            
                        fetch completecourses_cursor into completecourses_rec;
                    end loop;
                        
                        if(not prereq_flag) then
                            results := 'Prerequisite courses have not been completed';
                            return;
                        end if;
                        prereq_flag := false;
                        fetch prereq_cursor into prereq_rec;
                end loop;


                    if(enrollCount_rec.count < 3) then
                        INSERT INTO enrollments VALUES (sidIn, classidIn, null);
                        results := 'Student enrollment successful. ';
                    end if;    
                    
                    
                    if(enrollCount_rec.count = 3) then
                        results := results || 'You are overloaded.';
                        INSERT INTO enrollments VALUES (sidIn, classidIn, null);
                    end if;
                    
                    
                end if;
            end if; 
        end if;
    end if;   
  
end;​


-----------------------------------
-- *** Q8 ***
-----------------------------------


-- drop student from a class stored procedure


procedure drop_student_from_class(classidIn in classes.classid%type, sidIn in students.sid%type, results out varchar)
is

cursor stu_cursor is
select * from students where sid = sidIn;
 stu_rec stu_cursor%rowtype;

cursor classes_cursor is
select * from classes where classid = classidIn;
 classes_rec classes_cursor%rowtype;

cursor enrollments_cursor is
select * from enrollments where sid = sidIn and classid = classidIn;
 enrollments_rec enrollments_cursor%rowtype;

cursor enrollCount_cursor is
select count(*) as count from enrollments where sid = sidIn;
 enrollCount_rec enrollCount_cursor%rowtype;


cursor classSize_cursor is
select class_size from classes
where classid = classidIn;
 classSize_rec classSize_cursor%rowtype;


cursor dependentCourses_cursor is
(select (dept_code || course_no) as course from prerequisites 
where (pre_dept_code || pre_course_no) in (select (dept_code || course_no) from classes where classid = classidIn));
 dependentCourses_rec dependentCourses_cursor%rowtype;


cursor coursesTaken_cursor is
select (dept_code || course_no) as course from classes 
where classid in (select classid from enrollments where sid = sidIn);
coursesTaken_rec coursesTaken_cursor%rowtype;


begin


open stu_cursor;
fetch stu_cursor into stu_rec;

if (stu_cursor%notfound) then
    results := 'The sid is invalid.';
else 
    
    open classes_cursor;
    fetch classes_cursor into classes_rec;
    
    if (classes_cursor%notfound) then
        results := 'This classid is invalid.';
    else 
        open enrollments_cursor;
        fetch enrollments_cursor into enrollments_rec;
           
        if (enrollments_cursor%notfound) then
            results := 'The student is not enrolled in the class.';
                
        else 
            open dependentCourses_cursor;
            fetch dependentCourses_cursor into dependentCourses_rec;

            while dependentCourses_cursor%found loop
        
                open coursesTaken_cursor;
                        
                        
                        fetch coursesTaken_cursor into coursesTaken_rec;
                        
                        
                        while coursesTaken_cursor%found loop
                            
                            
                            if (coursesTaken_rec.course = dependentCourses_rec.course) then
                                results := 'The drop is not permitted because another class uses it as a prerequisite.';
                                return;
                            end if;
                            
                            fetch coursesTaken_cursor into coursesTaken_rec;
                        end loop;
                   
                        
                        fetch dependentCourses_cursor into dependentCourses_rec;
                    end loop;
                    
                    
                        delete from enrollments where sid = sidIn and classid = classidIn;
                        results := 'Course dropped successfully. ';
                        
                           open enrollCount_cursor;
                        
                        
                        fetch enrollCount_cursor into enrollCount_rec;
                        if (enrollCount_cursor%found) then                        
                            if(enrollCount_rec.count = 0) then
                                
                                results := results || 'The student is not enrolled in any classes. ';
                            end if;    
                        end if;
                        
                        
                            open classSize_cursor;
                        
                        fetch classSize_cursor into classSize_rec;
                        if (classSize_cursor%found) then
                            if(classSize_rec.class_size = 0) then
                                results := results || 'The class now has no students.';
                                
                            end if;
                        end if;
                    
            
            end if; 
        
    end if;   

end if;

end;

-----------------------------------
-- *** Q9 ***
-----------------------------------


-- delete a student stored procedure

procedure delete_student(sidIn in students.sid%type,results out varchar)
is
cursor stu_cursor is
select * from students where sid = sidIn;
 stu_rec stu_cursor%rowtype;
begin

       open stu_cursor;

fetch stu_cursor into stu_rec;
if (stu_cursor%notfound) then
    results := 'Student not found.';
   else 
       delete from students where sid = sidIn;
    results := 'Student deleted.';
       
end if;

end;





end Proj2_student_reg_package;
