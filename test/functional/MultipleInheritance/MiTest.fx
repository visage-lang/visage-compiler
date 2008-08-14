/*
 *
 * @test
 * @compilefirst ./FxQueue.fx 
 * @compilefirst ./FxComparator.fx 
 * @compilefirst ./FxPriorityQueue.fx
 * @run
 */
/**
This testcase  is for checking the rules of multiple inheritance in Fx esp to test how duplicate members and duplicate 
method conflicts from the multiple superclasess are resolved.
An FX class can extend multiple other FX classes, and multiple Java interfaces, but not multiple Java classes (abstract or concrete) 
**/

class Employee extends java.lang.Object,java.lang.Cloneable {  //Fx class extending Java Class and a Java interface
	var name :String;
	var exprnce: Integer;
	var salary :Integer;
	function getName():String{
		this.name;
	}
	function getExperience():Integer{
		this.exprnce;
	}
	function getSalary():Integer{
		this.salary;
	}
	function print():Void{
		java.lang.System.out.print("\t Name ==> {name},Experience==>{exprnce} yrs ,salary ==>{salary}");
		java.lang.System.out.println("");
	}
}
class EmployeeNameComparator extends FxComparator{
        init{
                comparatorCalled = "EmployeeNameComparator";
        }
        function compare(one,another):Integer{
                return empCompare(one as Employee,another as Employee);
        }
        function empCompare(oneEmployee:Employee,anotherEmployee:Employee){
                return nameCompare(oneEmployee.getName(),anotherEmployee.getName());
        }
        function nameCompare(oneName:String, anotherName:String):Integer{
                return oneName.compareTo(anotherName);
        }
}

class EmployeeExperienceComparator extends FxComparator{
        init{
                comparatorCalled = "EmployeeExperienceComparator";
        }
        function compare(one,another):Integer{
                return empCompare(one as Employee,another as Employee);
        }
        function empCompare(oneEmployee:Employee,anotherEmployee:Employee){
                return expCompare(oneEmployee.getExperience(),anotherEmployee.getExperience());
        }
        function expCompare(oneExperience:Integer, anotherExperience:Integer):Integer{
                if(oneExperience > anotherExperience){
                        return 1;
                }
                else if(oneExperience == anotherExperience){
                        return 0;
                }
                else{
                        return -1;
                }
        }
}
class EmployeeSalaryComparator extends FxComparator{
        init{
                comparatorCalled = "EmployeeSalaryComparator";
        }
        function compare(one,another):Integer{
                return empCompare(one as Employee,another as Employee);
        }
        function empCompare(oneEmployee:Employee,anotherEmployee:Employee){
                return expCompare(oneEmployee.getSalary(),anotherEmployee.getSalary());
        }
        function expCompare(oneSalary:Integer, anotherSalary:Integer):Integer{
                if(oneSalary> anotherSalary){
                        return 1;
                }
                else if(oneSalary == anotherSalary){
                        return 0;
                }
                else{
                        return -1;
                }
        }
}

/** Multiple superclasses provide default initialization for an attribute(comparatorUsed.comparatorCalled).
The class that appears later in the superclass order wins(EmployeeExperienceComparator in this case), 
and the implementation contributed by the losing class is ignored. 
Similarly attribute 'comparatorCalled' is initialized in all 3 superclasses,but only the initialization
of the class that appears later in the superclass order wins(EmployeeExperienceComparator again)
**/

class Comparator1 extends EmployeeNameComparator,EmployeeExperienceComparator{

/*In this case, duplicate attribute 'comparatorCalled' is initialized in all  the superclasses 
 and duplicate method implementations for 'compare' is provided
*/

}

class Comparator2 extends EmployeeExperienceComparator,EmployeeSalaryComparator,EmployeeNameComparator{
}


class Employeedb{
attribute employeeQueue:Employee[];
attribute comparatorChosen:FxComparator;
attribute empPQueue:FxPriorityQueue = FxPriorityQueue{comparatorUsed:comparatorChosen};
init{
	   java.lang.System.out.println("Comparator chosen=> {empPQueue.comparatorUsed.comparatorCalled}");
}
function putDetails(empInfoArr:Employee[]):Void{
	for(empInfo in empInfoArr){
		empPQueue.put(empInfo);		
		insert empInfo into employeeQueue;		
	}
	
}
function selectEmpForLayOff():Employee{
	java.lang.System.out.print("Select Employee for Layoff from db {
 	if(empPQueue.comparatorUsed.comparatorCalled.equals("EmployeeNameComparator")){"(by just selecting the first alphabetical-ordered name)"}else if (empPQueue.comparatorUsed.comparatorCalled.equals("EmployeeExperienceComparator")){"(Least experienced must leave)"} else if (empPQueue.comparatorUsed.comparatorCalled.equals("EmployeeSalaryComparator")){"(Least paid must leave)"} else { "(Do it Randomly)"}}  :: ");
	var retEmp = empPQueue.peek() as Employee;
	return 	retEmp;
}
function layOff():Employee  {
	 java.lang.System.out.print("Layoff the employee ::");
         var retVal:Employee= empPQueue.poll() as Employee;	  
	 delete retVal from employeeQueue;
         return retVal ;
}
function print (){
	java.lang.System.out.println("Employee details as of now::");
	for (em in employeeQueue){
		em.print();
	}
}
function clear(){
	empPQueue.clear();
	delete employeeQueue;
}
}

var compArr:FxComparator[] =[Comparator1{},Comparator2{}];
for (comp in compArr){
	var employeedb =Employeedb{comparatorChosen:comp};
	var emp1 = Employee{name:"Silly",salary:50000,exprnce:5};
	var emp2 = Employee{name:"Billy",salary:25000,exprnce:20};
	var emp3 = Employee{name:"Hilly",salary:45000,exprnce:9};
	var emp4 = Employee{name:"Willy",salary:95000,exprnce:1};
	var empArr : Employee[]=[emp1,emp2,emp3,emp4];
	employeedb.putDetails(empArr);
	employeedb.print();
	employeedb.selectEmpForLayOff().print();
	employeedb.layOff().print();
	java.lang.System.out.print("Insert a new Employee into DB::");
	var newEmp = Employee{name:"XHill",salary:335000,exprnce:49};
	newEmp.print();
	employeedb.putDetails([newEmp]);
	employeedb.print();
	employeedb.selectEmpForLayOff().print();
	employeedb.layOff().print();
	employeedb.print();
	java.lang.System.out.println("Done,Clear the DataBase");
	employeedb.clear();
	java.lang.System.out.println("**************************************");
}
