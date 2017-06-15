
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) {
		
		Scanner line = new Scanner(System.in);
		
		//menu-driven (all the options for menu are here 
		while(true){			
			
			System.out.println("\t1. Show Students");
			System.out.println("\t2. Show Classes");
			System.out.println("\t3. Show Enrollments");
			System.out.println("\t4. Show Courses");
			System.out.println("\t5. Show Prerequisites");
			System.out.println("\t6. Show Logs");
			System.out.println("\t7. Add a student");
			System.out.println("\t8. Show a student's details including his enrolled classes");
			System.out.println("\t9. Show all prerequisites of a course");
			System.out.println("\t10. Show a class detail including students enrolled in that class");
			System.out.println("\t11. Enroll a student");
			System.out.println("\t12. Drop student from class");
			System.out.println("\t13. Delete a student");
			System.out.println("\t14. Exit");
		
			System.out.print("\nEnter choice: ");
			String choice = line.nextLine();
			
			
			//According to the user's choice the objects of the various choices will be made
			switch(choice){
			
			case "1":	ShowStudents s_students = new ShowStudents();
						s_students.showStudents();
						break;
						
			case "2":	ShowClasses s_classes = new ShowClasses();
						s_classes.showClasses();
						break;
			
			case "3":	ShowEnrollments s_enrollments = new ShowEnrollments();
						s_enrollments.showEnrollments();
						break;
			
			case "4":	ShowCourses s_courses = new ShowCourses();
						s_courses.showCourses();
						break;
			
			
			case "5":	ShowPrerequisites s_prereq = new ShowPrerequisites();
						s_prereq.showPrereq();
						break;
			
			case "6":	ShowLogs s_logs = new ShowLogs();
						s_logs.showLogs();
						break;
			
			case "7":	AddStudent add_stu = new AddStudent();
						add_stu.addStudent();
						break;
						
			case "8":	ShowStudentClasses s_stu_classes = new ShowStudentClasses();
						s_stu_classes.showStudentClasses();
						break;
			
			case "9":	ShowPrereqRecur s_prereq_recur = new ShowPrereqRecur();
						s_prereq_recur.showPrereqRecur();
						break;
			
			case "10":	ShowClassesStudents s_classes_stu = new ShowClassesStudents();
						s_classes_stu.showClassesStudents();
						break;
						
			case "11":	EnrollStudentInClass enroll_stu = new EnrollStudentInClass();
						enroll_stu.enrollStdudent();
						break;
			
			case "12":	DropStudentFromClass drop_stu = new DropStudentFromClass();
						drop_stu.dropStudent();
						break;
						
			case "13":	DeleteStudent del_stu = new DeleteStudent();
						del_stu.deleteStudent();
						break;
			
			case "14":	System.exit(0);
			
			default:	System.out.println("Wrong choice. Please try again");
			
			}
			
			System.out.print("\nEnter n to exit or y to continue: ");
			String ch = line.nextLine();
			
			if(ch.equals("n")){
				System.out.println("You selected to end the program.");
				System.exit(0);
			}
			

		}
		
		
		
	}

}
